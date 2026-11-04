package com.itheima.service;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.PageResult;

public interface ClazzService {
    //分页查询
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);
    //新增
    void add(Clazz clazz);
    //根据ID查询
    Clazz get(Long id);
    //修改
    void update(Clazz clazz);
    //删除
    void delete(Long id);
}
