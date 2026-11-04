package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import java.awt.*;
import java.util.Date;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class JobOption {
    private List jobList;//职位列表
    private List dataList;//数据列表
}
