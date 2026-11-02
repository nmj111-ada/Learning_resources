package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {

    //分页查询方法
    PageResult<Emp> page(EmpQueryParam empQueryParam);

    //新增员工信息
    void save(Emp emp);


    //批量删除员工的工作经历
    void deleteByIds(List<Integer> ids);

    //根据id查询员工信息
    Emp getInfo(Integer id);

    //修改员工
    void update(Emp emp);
}
