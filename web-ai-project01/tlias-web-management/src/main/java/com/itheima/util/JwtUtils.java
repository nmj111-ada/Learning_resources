package com.itheima.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/** 统一签发、验证 JWT；不把密码放入令牌，也不在源码中保存签名密钥。 */
@Slf4j
@Component
public class JwtUtils {
    private final SecretKey key;

    public JwtUtils(@Value("${tlias.jwt.secret:}") String secret) {
        // 学习环境未配置密钥时生成随机密钥；重启后旧 Token 自动失效，需重新登录。
        if (secret == null || secret.isBlank()) {
            key = Jwts.SIG.HS256.key().build();
            log.info("JWT 使用本次启动生成的临时密钥，重启后请重新登录");
        } else {
            // 固定密钥通过本地 tlias.jwt.secret 或 TLIAS_JWT_SECRET 环境变量设置，至少 32 字节。
            key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    // 登录成功后签发有效期为 12 小时的令牌，只携带操作人 ID。
    public String generateToken(Integer empId) {
        Instant now = Instant.now();
        return Jwts.builder().subject(empId.toString()).issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(12 * 60 * 60)))
                .signWith(key, Jwts.SIG.HS256).compact();
    }

    // 验证签名、过期时间和用户 ID；前端传来的 ID 本身不能作为登录依据。
    public Integer parseEmployeeId(String token) {
        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        if (claims.getExpiration() == null || claims.getSubject() == null) {
            throw new JwtException("令牌缺少有效期或用户信息");
        }
        int id = Integer.parseInt(claims.getSubject());
        if (id <= 0) { throw new JwtException("令牌用户 ID 无效"); }
        return id;
    }
}
