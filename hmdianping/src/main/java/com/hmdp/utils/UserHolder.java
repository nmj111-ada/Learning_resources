package com.hmdp.utils;

import com.hmdp.dto.UserDTO;

/**
 * 基于 ThreadLocal 保存当前请求的登录用户，
 * 同一线程（同一次请求）内可通过 getUser() 随时获取登录用户
 */
public class UserHolder {
    //以当前线程为 key 存储用户，各线程之间互不影响
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    //保存登录用户到当前线程
    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    //获取当前线程中的登录用户
    public static UserDTO getUser(){
        return tl.get();
    }

    //请求结束后移除用户，防止内存泄漏及线程复用导致的数据串号
    public static void removeUser(){
        tl.remove();
    }
}
