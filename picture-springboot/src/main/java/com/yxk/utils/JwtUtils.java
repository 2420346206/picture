package com.yxk.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * description: JWT工具类（生成、验证、刷新判断）
 */
@Component
@Data
@ConfigurationProperties(prefix = "jwt")
@Slf4j
public class JwtUtils {

    /** token header*/
    private String header;

    /** token 前缀 */
    private String tokenPrefix;

    /** 签名密钥 */
    private String secret;

    /** 有效期（毫秒） */
    private long expireTime;

    /**
     * 生成 token（返回纯 token，不拼前缀）
     */
    public String createToken(Long userId) {
        return JWT.create()
                .withClaim("userId", userId) // 保存用户标识
                .withIssuedAt(new Date()) // 签发时间
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime)) // 过期时间
                .sign(Algorithm.HMAC512(secret));
    }

    /**
     * 校验 token，成功返回 subject，失败抛异常
     */
    public Long getUserId(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token)
                    .getClaim("userId").asLong();
        } catch (TokenExpiredException e) {
            log.warn("token 已过期");
            throw e;
        } catch (Exception e) {
            log.warn("token 验证失败");
            throw e;
        }
    }

    /**
     * 检查 token 是否需要更新
     * @param token JWT
     * @return true 表示建议刷新
     */
    public boolean isNeedUpdate(String token) {
        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token);
            Date expiresAt = decoded.getExpiresAt();
            return (expiresAt.getTime() - System.currentTimeMillis()) < (expireTime >> 1);
        } catch (TokenExpiredException e) {
            return true; // 已过期，肯定需要更新
        } catch (Exception e) {
            log.warn("token 验证失败，无法判断是否需要更新");
            return false;
        }
    }
}
