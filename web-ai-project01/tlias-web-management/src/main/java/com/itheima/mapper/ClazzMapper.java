package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ClazzMapper {
    //查询所有课程数据
    List<Clazz> list(ClazzQueryParam clazzQueryParam);

    //查询班主任名字即查询所有员工的名字
    @Select("select emp.name from emp")
    Emp getEmpName();

    //新增课程信息
    @Insert("insert into " +
            "clazz (name, room, begin_date, end_date, master_id, subject, create_time, update_time) " +
            "values (#{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject}, #{createTime}, #{updateTime})")
    void add(Clazz clazz);

    //根据id查询课程信息
    @Select("select " +
            "id, name, room, begin_date, end_date, master_id, subject " +
            "from clazz where id = #{id}")
    Clazz get(Long id);

    //修改课程信息
    @Update("update clazz " +
            "set name = #{name}, room = #{room}, begin_date = #{beginDate}, end_date = #{endDate}, master_id = #{masterId}, subject = #{subject}, update_time = #{updateTime} " +
            "where id = #{id}")
    void update(Clazz clazz);

    //根据id删除课程信息
    @Delete("delete from clazz where id = #{id}")
    void delete(Long id);

    //查询全部课程名字
    @Select("select id, name from clazz")
    List<Clazz> getClazzName();

    //统计班级人数
    List<Map<String, Object>> countStudentNum();
}
