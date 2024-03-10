package com.wuxian.springbatch.controller;

import com.wuxian.springbatch.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wu xian
 * @date 2023/3/9
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @PostMapping("import")
    @ResponseBody
    public String importStudentFile() {
        return studentService.importStudentFile();
    }

}
