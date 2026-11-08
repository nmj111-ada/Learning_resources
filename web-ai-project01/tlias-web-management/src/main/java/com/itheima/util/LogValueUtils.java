package com.itheima.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 将参数和返回值转为可读 JSON，并在落库前去掉敏感字段。 */
@Component
@RequiredArgsConstructor
public class LogValueUtils {
    private final ObjectMapper objectMapper;
    private static final Set<String> SENSITIVE = Set.of(
            "password", "passwd", "pwd", "token", "authorization", "secret", "apikey",
            "accesskey", "idcard", "phone", "address");

    // 参数转换在业务执行前调用，避免业务修改对象后丢失原始请求信息。
    public String serialize(Object value) {
        try {
            String json = objectMapper.writeValueAsString(sanitize(value));
            // 教程日志字段长度有限，超长内容截断，防止普通操作因日志过长而失败。
            return json.length() <= 1900 ? json : json.substring(0, 1897) + "...";
        } catch (RuntimeException e) {
            // 转换失败不回退到 toString，避免意外输出密码或文件内容。
            return "[无法序列化]";
        }
    }

    // 逐层处理对象、数组和集合；文件只记录元信息，不保存文件内容。
    private Object sanitize(Object value) {
        if (value == null) { return null; }
        if (value instanceof MultipartFile file) {
            return Map.of("fileSize", file.getSize(), "contentType",
                    file.getContentType() == null ? "unknown" : file.getContentType());
        }
        if (value instanceof Object[] array) {
            List<Object> values = new ArrayList<>();
            for (Object item : array) { values.add(sanitize(item)); }
            return values;
        }
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> values = new LinkedHashMap<>();
            map.forEach((key, item) -> {
                String name = key.toString();
                String normalized = name.replace("_", "").replace("-", "").toLowerCase(java.util.Locale.ROOT);
                values.put(name, SENSITIVE.contains(normalized) ? "***" : sanitize(item));
            });
            return values;
        }
        if (value instanceof Iterable<?> iterable) {
            List<Object> values = new ArrayList<>();
            for (Object item : iterable) { values.add(sanitize(item)); }
            return values;
        }
        if (value instanceof String || value instanceof Number || value instanceof Boolean) { return value; }
        return sanitize(objectMapper.convertValue(value, Object.class));
    }
}
