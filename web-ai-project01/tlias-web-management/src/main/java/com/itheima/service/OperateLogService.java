package com.itheima.service;

import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;

/** 日志列表只读查询，与 AOP 的日志写入分开。 */
public interface OperateLogService {
    // 根据页码和每页条数查询日志及总记录数。
    PageResult<OperateLog> page(Integer page, Integer pageSize);
}
