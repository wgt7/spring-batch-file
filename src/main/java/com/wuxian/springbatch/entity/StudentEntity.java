package com.wuxian.springbatch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生信息实体类
 *
 * @author wu xian
 * @date 2023/3/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id 20
     */
    private String id;

    /**
     * 姓名 50
     */
    private String name;

    /**
     * 年龄 5
     */
    private int age;

    /**
     * 性别 5
     */
    private String sex;

    /**
     * 地址 100
     */
    private String address;

    /**
     * 联系电话 20
     */
    private String phone;

    /**
     * 创建时间 20
     */
    private String createTime;

}
