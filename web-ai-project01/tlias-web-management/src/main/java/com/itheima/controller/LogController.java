package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.service.OperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 与现有前端日志管理页对接，返回 data.total 和 data.rows。 */
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {
    private final OperateLogService operateLogService;

    // 缺省页码为 1、每页 10 条；限制单页数量，避免一次查询全部日志。
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        if (page < 1 || pageSize < 1 || pageSize > 100) {
            return Result.error("页码需大于 0，每页条数需在 1 到 100 之间");
        }
        return Result.success(operateLogService.page(page, pageSize));
    }
}
