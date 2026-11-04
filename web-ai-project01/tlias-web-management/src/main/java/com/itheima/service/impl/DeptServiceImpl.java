package com.itheima.service.impl;

import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
    //查询所有部门数据
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    //根据id删除部门数据
    public void deleteById(Integer id) {
        deptMapper.deleteById(id);
    }

    //添加
    @Override
    public void add(Dept dept) {
        //补全基础属性 - createTime, updateTime
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        //调用Mapper方法插入数据
        deptMapper.add(dept);
    }

    //根据id查询部门
    @Override
    public Dept getInfo(Integer id) {
        return deptMapper.getInfo(id);
    }

    //根据id修改部门
    @Override
    public void update(Dept dept){
        //1.补全基础属性-updateTime
        dept.setUpdateTime(LocalDateTime.now());
        //2.调用Mapper接口方法
        deptMapper.update(dept);
    }
}
