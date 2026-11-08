package com.itheima.mapper;

import com.itheima.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OperateLogMapper {

    //插入日志数据
    @Insert("insert into operate_log (operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    public void insert(OperateLog log);

    // LEFT JOIN 保留已删除员工的历史日志，按 ID 倒序显示最新操作。
    @Select("select l.*, e.name as operate_emp_name from operate_log l " +
            "left join emp e on l.operate_emp_id = e.id order by l.id desc")
    List<OperateLog> list();
}
