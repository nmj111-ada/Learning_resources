package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//登录controller
@Slf4j
@RestController
public class LoginController {
    @Autowired
    private EmpService empService;
    //登录方法
    @org.springframework.web.bind.annotation.PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        // 登录对象包含密码，只记录登录事件，不打印整个对象。
        log.info("收到登录请求");
        LoginInfo info = empService.login(emp);
        if(info != null){
            return Result.success(info);
        }
        return Result.error("用户名或密码错误");
    }
}
