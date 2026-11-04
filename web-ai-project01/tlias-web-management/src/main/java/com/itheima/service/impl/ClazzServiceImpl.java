package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.ClazzMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import com.itheima.service.ClazzService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;

    //分页查询方法
    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        //1.调用Mapper接口，查询总记录数
        //2.调用mapper接口，查询结果列表
        //3.封装结果pageResult

        //1.设置分页参数
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
        //2.执行查询
        List<Clazz> clazzList = clazzMapper.list(clazzQueryParam);

        //3.解析查询结果并封装
        Page<Clazz> p = (Page<Clazz>) clazzList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    //新增课程信息
    @Override
    public void add(Clazz clazz) {
        //1.先查询班主任信息(在emp里完成了）
        //2.新增课程信息
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.add(clazz);
    }

    //根据id查询课程信息
    @Override
    public Clazz get(Long id) {
        return clazzMapper.get(id);
    }

    //修改课程信息
    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    //根据id删除课程
    @Override
    public void delete(Long id) {
        clazzMapper.delete(id);
    }

    //查询全部课程信息
    @Override
    public List<Clazz> getClazzName() {
        return clazzMapper.getClazzName();
    }
}
