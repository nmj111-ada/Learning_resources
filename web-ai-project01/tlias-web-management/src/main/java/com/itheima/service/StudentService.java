package com.itheima.service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import org.springframework.stereotype.Service;

public interface StudentService {
    /**
     * 根据条件查询分页列表
     * @param studentQueryParam 查询条件
     * @return 分页列表
     */
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    //新增学生
    void add(Student student);

    //根据id查询学生信息
    Student get(Long id);

    //修改学生信息
    void update(Student student);

    //删除学生信息
    void delete(Long id);

    //学生违纪处理
    void violation(Long id, Integer score);
}
