package com.wuxian.springbatch.service.impl;

import com.wuxian.springbatch.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wu xian
 * @date 2023/3/9
 */
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private JobLauncher jobLauncher;

    @Resource
    private Job studentBatchJob;

    @Override
    public String importStudentFile() {
        log.info("导入学生信息开始...");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String createTime = sdf.format(new Date());

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("createTime", createTime)
                    .toJobParameters();

            jobLauncher.run(studentBatchJob, jobParameters);
        } catch (Exception e) {
          log.error("导入学生信息异常", e);
        }
        log.info("导入学生信息结束...");
        return "success";
    }
}
