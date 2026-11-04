package com.itheima.controller;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/students")
@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;
    //学生条件分页查询
    @GetMapping
    public Result page(StudentQueryParam studentQueryParam) {
        log.info("分页查询：{}", studentQueryParam);
        PageResult<Student> pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }

    //新增学生
    //显示班级进行选择
    @PostMapping
    public Result add(@RequestBody Student student) {
        log.info("新增学生信息： {}", student);
        studentService.add(student);
        return Result.success();
    }

    //根据id查询学生信息
    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) {
        log.info("根据ID查询：{}", id);
        Student student = studentService.get(id);
        return Result.success(student);
    }

    //修改学生信息
    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("修改学生信息： {}", student);
        studentService.update(student);
        return Result.success();
    }

    //删除学生信息
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        log.info("删除学生信息：{}", id);
        studentService.delete(id);
        return Result.success();
    }

    //学生违纪处理
    @PutMapping("/violation/{id}/{score}")
    public Result violation(@PathVariable Long id, @PathVariable Integer score) {
        log.info("学生违纪处理：{}", id);
        studentService.violation(id, score);
        return Result.success();
    }
}
