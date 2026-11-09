package com.itheima.exception;


import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("异常信息{}",e);
        return Result.error("服务器异常，操作失败");
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.error("异常信息{}",e);
        return Result.error("名称、用户名、学号或手机号已存在，请检查后重试");
    }
    // 可预期的业务校验直接向前端返回提示。
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleBusinessException(IllegalArgumentException e) {
        return Result.error(e.getMessage());
    }
    // 删除部门时如果部门内有员工就提示
    @ExceptionHandler(DeptHasEmpException.class)
    public Result handleDeptHasEmpException(DeptHasEmpException e){
        log.error("异常信息", e);
        return Result.error(e.getMessage());
    }
}
