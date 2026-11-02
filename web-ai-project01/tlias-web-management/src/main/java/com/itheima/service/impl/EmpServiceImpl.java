package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    //分页查询方法
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
//        //1.调用Mapper接口，查询总记录数
//        Long total = empMapper.count();
//        //2.调用mapper接口，查询结果列表
//        Integer start = (page-1) * pageSize;
//        List<Emp> rows = empMapper.list(start, pageSize, name, gender, begin, end);
//        //3.封装结果pageResult
//        return new PageResult<Emp>(total, rows);
        //1.设置分页参数
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());

        //2.执行查询
        List<Emp> empList = empMapper.list(empQueryParam);

        //3.解析查询结果并封装
        Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Transactional(rollbackFor = {Exception.class})//默认出现RuntimeException出现时回滚，加rollbackFor = {Exception.class}为全部异常回滚
    @Override
    public void save(Emp emp) {
        //1.保存员工基本信息
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);

        //2.保存员工工作经历信息
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            //遍历集合，为empId赋值
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(exprList);
        }
    }


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void deleteByIds(List<Integer> ids) {
        //1.批量删除员工基本信息
        empMapper.deleteByIds(ids);
        //2.批量删除员工的工作经历信息
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }

    //修改员工信息
    @Override
    public void update(Emp emp) {
        //1.根据id修改员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);
        //2.根据id修改员工的工作经历信息
        //2.1先删除
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        //2.2后添加
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(exprList);
        }


    }
}
