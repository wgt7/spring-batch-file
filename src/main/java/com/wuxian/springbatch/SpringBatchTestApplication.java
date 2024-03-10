package com.wuxian.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wu xian
 * @date 2023/3/9
 */
@EnableBatchProcessing//开启批处理支持
@SpringBootApplication
public class SpringBatchTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchTestApplication.class, args);
    }
}
