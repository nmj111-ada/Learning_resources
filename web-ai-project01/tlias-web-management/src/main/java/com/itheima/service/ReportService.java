package com.itheima.service;

import com.itheima.pojo.JobOption;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public interface ReportService {
    //获取职位数据
    JobOption getEmpJobData();
    //获取性别数据
    List<Map<String, Object>> getEmpGenderData();
}
