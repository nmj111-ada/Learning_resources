package com.itheima.aop;

import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.util.CurrentHolder;
import com.itheima.util.LogValueUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/** 只记录带 @Log 注解的增删改方法；登录和日志查询不会再次生成操作日志。 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {
    private final OperateLogMapper operateLogMapper;
    private final LogValueUtils logValueUtils;

    // 环绕通知负责采集一次操作的上下文、入参、返回值和执行耗时。
    @Around("@annotation(com.itheima.anno.Log)")
    public Object record(ProceedingJoinPoint joinPoint) throws Throwable {
        OperateLog operation = new OperateLog();
        operation.setOperateEmpId(CurrentHolder.getCurrentId());
        operation.setOperateTime(LocalDateTime.now());
        operation.setClassName(joinPoint.getTarget().getClass().getName());
        operation.setMethodName(joinPoint.getSignature().getName());
        operation.setMethodParams(logValueUtils.serialize(joinPoint.getArgs()));
        long start = System.nanoTime();

        // 原方法只执行一次；抛异常时保持原有异常处理，本版只记录正常返回的操作。
        Object result = joinPoint.proceed();

        operation.setCostTime(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        operation.setReturnValue(logValueUtils.serialize(result));
        try {
            operateLogMapper.insert(operation);
        } catch (RuntimeException e) {
            // 业务已执行完毕，日志写入失败不能让前端误以为业务失败并重复提交。
            log.error("操作日志写入失败，方法：{}，异常类型：{}", operation.getMethodName(), e.getClass().getSimpleName());
        }
        return result;
    }
}
