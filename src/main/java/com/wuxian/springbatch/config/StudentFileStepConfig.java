package com.wuxian.springbatch.config;

import com.wuxian.springbatch.entity.StudentEntity;
import com.wuxian.springbatch.entity.StudentFile;
import com.wuxian.springbatch.processor.StudentFileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;

/**
 * @author wu xian
 * @date 2023/3/9
 */
@Slf4j
@Configuration
public class StudentFileStepConfig {

    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private StudentFileProcessor studentFileProcessor;

    @Resource
    private DataSource dataSource;

    /**
     * 配置ItemWriter
     *
     * @return ItemWriter
     */
    @Bean
    public JdbcBatchItemWriter studentJdbcBatchItemWriter() {
        //便用JdbcBatchItemWriter通过JDBC将数据写到数据库中
        JdbcBatchItemWriter writer = new JdbcBatchItemWriter();

        //设置数据源
        writer.setDataSource(dataSource);
        //设置插入更新的SQL，注意占位符的写法 ："属性名"
        writer.setSql("insert into student(id, name, age, sex, address, phone, createTime) values (:id, :name, :age, :sex, :address, :phone, :createTime)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        return writer;
    }

    /**
     * 配置ItemReader
     *
     * @return ItemReader
     */
    @Bean
    @StepScope
    public FlatFileItemReader<StudentFile> studentFileReader() {
        //FlatFileItemReader 是一个加载普通文件的 ItemReader
        FlatFileItemReader<StudentFile> reader = new FlatFileItemReader<>();
        //第一行为标题跳过，若没有标题，注释掉即可
        // reader.setLinesToSkip(1);
        String fileName = "studentinfo";
        String filePath = "C:\\test";
        String fullName = filePath + File.separator + fileName;
        FileSystemResource fileResource = new FileSystemResource(fullName);
        reader.setEncoding("GBK");
        reader.setResource(fileResource);
        reader.setLineMapper(new DefaultLineMapper<StudentFile>() {
            {
                setLineTokenizer(
                        //固定长度分割
                        studentFixedLengthTokenizer()
                );
                setFieldSetMapper(new BeanWrapperFieldSetMapper() {
                    {
                        setTargetType(StudentFile.class);
                    }
                });
            }
        });
        return reader;
    }

    /**
     * 定长分割，文件一行长度固定，每个字段固定长度，长度不足的，数字前补0，字符串后补空格
     *
     * @return
     */
    @Bean
    public FixedLengthTokenizer studentFixedLengthTokenizer() {
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        //设置文件中一共有多少列，分别是哪些列
        //需要读取的列
        tokenizer.setNames("name", "age", "sex", "address", "phone");
        tokenizer.setColumns(
                // new Range(1, 20),//id 20
                new Range(21, 70),//姓名 50
                new Range(71, 75),//年龄 5
                new Range(76, 80),//性别 5
                new Range(81, 180),//地址 100
                new Range(181, 200)//联系电话 20
        );

        /*
          是否严格匹配。设置读取一行的部分列时，必须设置为false。
          例如一行里有30个字段，但是只读取其中10个字段，其他字段忽略不需要读取。
          可以容忍标记比较少的行，并用空列填充，标记较多的行将被简单地截断。默认为true
        */
        tokenizer.setStrict(false);
        return tokenizer;
    }

    /**
     * 分割符分割，文件一行长度不固定，字段之间使用指定的分隔符进行分割
     *
     * @return
     */
    @Bean
    public DelimitedLineTokenizer studentDelimitedLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("name", "age", "sex", "address", "phone");
        tokenizer.setDelimiter(" ");
        return tokenizer;
    }

    /**
     * 配置Step
     *
     * @return Step
     */
    @Bean
    public Step studentStep() {
        return stepBuilderFactory.get("studentStep")//通过get获取一个stepBuilder，参数为step的name
                .<StudentFile, StudentEntity>chunk(5000)//配置chunk，每读取5000条，就执行一次writer操作
                .reader(studentFileReader())//配置reader
                .processor(studentFileProcessor)//配置processor，进行数据的预处理
                .writer(studentJdbcBatchItemWriter())//配置writer
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    /**
     * 配置Job
     *
     * @return Job
     */
    @Bean
    public Job studentBatchJob() {
        return jobBuilderFactory.get("studentJob")//通过 jobBuilderFactory 构建一个 Job. get参数为 Job 的name
                .start(studentStep())//配置该Job的Step
                //.next(studentStep2())//多个Step时，通过next()进行配置
                .build();
    }

}
