package com.itheima.controller;

import com.itheima.anno.Log;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@Slf4j //日志
@RestController
@RequiredArgsConstructor // Lombok 自动生成构造器
public class DeptController {

    private final DeptService deptService;

    //@RequestMapping(value = "/depts",method = RequestMethod.GET)//method指定请求方式
    //查询全部部门
    @GetMapping({"/depts", "/depts/list"})
    public Result list(){
        log.info("查询全部部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    //删除部门
    // 此写操作成功返回后，由日志切面统一记录操作人、参数和耗时。
    @Log
    @DeleteMapping("/depts")
    /*
    方式1：手动转换参数，通过request对象获取参数
    public Result delete(HttpServletRequest request){
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        System.out.println("删除部门数据"+id);
        return Result.success();
    }
     */
    //方式2：使用@RequestParam注解，获取参数
    public Result delete(@RequestParam("id") Integer deptId){
        System.out.println("删除部门数据"+deptId);
        deptService.deleteById(deptId);
        return Result.success();
    }

    //增加
    // 此写操作成功返回后，由日志切面统一记录操作人、参数和耗时。
    @Log
    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept){
        System.out.println("增加部门数据"+dept);
        deptService.add(dept);
        return Result.success();
    }

    //接受参数
    @GetMapping("/depts/{id}")
    public Result get(@PathVariable("id") Integer id){
        log.info("根据id查询部门:{}", id);
        Dept dept = deptService.getInfo(id);
        return Result.success(dept);
    }

    //修改部门
    // 此写操作成功返回后，由日志切面统一记录操作人、参数和耗时。
    @Log
    @PutMapping("/depts")
    public Result update(@RequestBody Dept dept){
        System.out.println("修改部门数据"+dept);
        deptService.update(dept);
        return Result.success();
    }
}
