package com.itheima.controller;

import com.itheima.pojo.*;
import com.itheima.service.ClazzService;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/clazzs")
@RestController
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    //分页查询班级信息
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam) {
        log.info("分页查询：{}", clazzQueryParam);
        PageResult<Clazz> pageResult = clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    //新增课程信息接口
    @PostMapping
    public Result add(@RequestBody Clazz clazz) {
        log.info("新增班级信息：{}", clazz);
        clazzService.add(clazz);
        return Result.success();
    }

    //修改课程信息
    //1.根据ID查询
    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) {
        log.info("根据ID查询：{}", id);
        Clazz clazz = clazzService.get(id);
        return Result.success(clazz);
    }
    //2.修改信息
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        log.info("修改课程信息：{}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    //删除课程信息
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        log.info("删除课程信息：{}", id);
        clazzService.delete(id);
        return Result.success();
    }
}
