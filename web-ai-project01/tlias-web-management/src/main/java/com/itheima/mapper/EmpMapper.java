package com.itheima.mapper;


import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//操作员工基本信息
@Mapper
public interface EmpMapper {
    //查询记录数
    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
    public Long count();

    //@Select("select e.*, d.name as deptName from emp e left join dept d on e.dept_id = d.id " +"order by e.update_time desc limit #{start}, #{pageSize}")
    public List<Emp> list(Integer start, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end);

    List<Emp> list(EmpQueryParam empQueryParam);

    //新增员工基本信息
    @Options(useGeneratedKeys = true, keyProperty = "id") //获取到生成的主键：主键返回
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)\n" +
            "values(#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    //根据ID批量删除员工基本信息
    void deleteByIds(@Param("ids") List<Integer> ids);

    //根据员工ID批量删除工作经历
    void deleteByEmpIds(@Param("ids") List<Integer> ids);

    //根据员工ID查询员工基本信息以及工作经历信息
    Emp getById(Integer id);

    //根据ID修改员工基本信息
    void updateById(Emp emp);

    //统计员工职位人数
    List<Map<String,Object>> countEmpJobDate();

    //统计员工性别人数
    List<Map<String,Object>> countEmpGenderData();

    //查询全部员工姓名
    @Select("select id, name from emp")
    List<Emp> getAllName();
}
