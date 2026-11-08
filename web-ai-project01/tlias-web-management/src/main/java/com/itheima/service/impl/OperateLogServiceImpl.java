package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import com.itheima.service.OperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperateLogServiceImpl implements OperateLogService {
    private final OperateLogMapper operateLogMapper;

    // startPage 只对紧接着的一次查询分页；try-with-resources 确保分页上下文被释放。
    @Override
    public PageResult<OperateLog> page(Integer page, Integer pageSize) {
        try (Page<OperateLog> result = PageHelper.startPage(page, pageSize)) {
            operateLogMapper.list();
            return new PageResult<>(result.getTotal(), result.getResult());
        }
    }
}
