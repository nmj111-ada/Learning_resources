package com.itheima.pojo;

import lombok.Getter;
import lombok.Setter;

// 不生成 toString，防止旧密码和新密码被意外打印。
@Getter
@Setter
public class PasswordChange {
    private String oldPassword;
    private String newPassword;
}
