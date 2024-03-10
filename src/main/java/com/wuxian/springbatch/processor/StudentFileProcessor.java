package com.wuxian.springbatch.processor;

import com.wuxian.springbatch.entity.StudentEntity;
import com.wuxian.springbatch.entity.StudentFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * 学生信息处理
 *
 * @author wu xian
 * @date 2023/3/9
 */
@Slf4j
@Component
public class StudentFileProcessor implements ItemProcessor<StudentFile, StudentEntity> {

    private JobParameters jobParameters;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
    }

    //通过jobParameters接收参数
    public String getHandleDate() {
        return jobParameters.getString("createTime");
    }

    @Override
    public StudentEntity process(StudentFile student) throws Exception {
        if (Objects.isNull(student)) {
            //跳过空行
            return null;
        }

        //数据处理 此处简单赋值
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(UUID.randomUUID().toString().replace("-","").substring(0,20));
        studentEntity.setName(student.getName());
        studentEntity.setAge(student.getAge());
        studentEntity.setSex(student.getSex());
        studentEntity.setAddress(student.getAddress());
        studentEntity.setPhone(student.getPhone());

        studentEntity.setCreateTime(getHandleDate());

        return studentEntity;
    }
}
