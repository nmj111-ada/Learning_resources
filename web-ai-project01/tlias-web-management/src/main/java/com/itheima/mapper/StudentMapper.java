package com.itheima.mapper;

import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    // 根据条件查询列，分页查询
    List<Student> list(StudentQueryParam studentQueryParam);

    //查询课程名
    @Select("SELECT clazz_name FROM clazz WHERE id = #{id}")
    Student getClazzName();

    //新增学生
    @Insert("INSERT INTO " +
            "student (name, gender, id_card, address, graduation_date, no, phone, is_college, degree, clazz_id, create_time, update_time, violation_count, violation_score) " +
            "VALUES (#{name}, #{gender}, #{idCard}, #{address}, #{graduationDate}, #{no}, #{phone}, #{isCollege}, #{degree}, #{clazzId}, #{createTime}, #{updateTime}, 0, 0)")
    void add(Student student);

    //根据id查询学生信息
    @Select("SELECT * FROM student WHERE id = #{id}")
    Student get(Long id);

    //修改学生信息
    @Update("UPDATE " +
            "student SET name = #{name}, gender = #{gender}, id_card = #{idCard}, address = #{address}, graduation_date = #{graduationDate}, no = #{no}, phone = #{phone}, is_college = #{isCollege}, degree = #{degree}, clazz_id = #{clazzId}, update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void update(Student student);

    //删除学生信息
    @Delete("DELETE FROM student WHERE id = #{id}")
    void delete(Long id);

    //学生违纪处理,违纪次数+1，违纪分数为违纪扣分+前端传入的分数
    @Update("UPDATE " +
            "student " +
            "SET violation_count = coalesce(violation_count, 0) + 1, violation_score = coalesce(violation_score, 0) + #{score}, update_time = current_timestamp " +
            "WHERE id = #{id}")
    void violation(Long id, Integer score);

    // 删除班级前先检查是否仍有学生。
    @Select("select count(*) from student where clazz_id = #{id}")
    int countByClazzId(Long id);

    void deleteByIds(@Param("ids") List<Long> ids);

    //统计学生学历信息
    List<Map<String, Object>> countStudentDegreeData();

}
