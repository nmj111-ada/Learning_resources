package com.itheima.util;

/** 保存当前请求的员工 ID；请求结束必须清理，避免线程复用造成用户串号。 */
public final class CurrentHolder {
    private static final ThreadLocal<Integer> CURRENT_ID = new ThreadLocal<>();

    private CurrentHolder() {}

    // Token 校验通过后，保存当前请求的操作人。
    public static void setCurrentId(Integer id) { CURRENT_ID.set(id); }

    // 操作日志切面从同一请求线程中读取操作人。
    public static Integer getCurrentId() { return CURRENT_ID.get(); }

    // 无论业务成功还是异常，都释放线程中的用户信息。
    public static void remove() { CURRENT_ID.remove(); }
}
