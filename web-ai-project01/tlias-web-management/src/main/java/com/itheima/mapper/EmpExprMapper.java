package com.itheima.mapper;


import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//员工工作经历接口
@Mapper
public interface EmpExprMapper {
    //批量保存员工工作经历信息
    void insertBatch(List<EmpExpr> exprList);

    //根据ID批量删除员工的基本信息
    void deleteByEmpIds(List<Integer> ids);


}
