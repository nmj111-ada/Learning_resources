package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    //查询所有的部门数据
    /*
        1.手动结果映射
        @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
        })
        2.起别名：
        @Select("select id,name,create_time as createTime,
        update_time as updateTime from dept order by update_time desc;")

     */

    @Select("select id,name,create_time,update_time from dept order by update_time desc;")
    List<Dept> findAll();

    //根据id删除部门
    @Delete("delete from dept where id = #{id}")
    void deleteById(@Param("id") Integer id);

    //增加
    @Insert("insert into dept(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void add(Dept dept);

    //根据id查询
    @Select("select id,name,create_time,update_time from dept where id = #{id}")
    Dept getInfo(@Param("id") Integer id);

    //根据id修改部门
    @Update("update dept set name = #{name},update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);
}
