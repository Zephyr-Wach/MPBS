package com.zephyr.mpbscommon.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
/**
 * JWT 工具类，负责生成和解析 JSON Web Token（JWT），
 * 以及验证Token的有效性。
 * <p>
 * 使用配置文件中的 secretKey 和 expirationTime 初始化。
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationTime;

    private static String SECRET_KEY;
    private static long EXPIRATION_TIME;
    private static long REFRESH_EXPIRATION_TIME;

    /**
     * 初始化静态常量，用于静态方法访问配置的密钥和过期时间。
     * 在Spring容器加载后调用。
     */
    @PostConstruct
    public void init() {
        SECRET_KEY = this.secretKey;
        EXPIRATION_TIME = this.expirationTime;
        REFRESH_EXPIRATION_TIME = this.refreshExpirationTime;
    }

    /**
     * 生成JWT Token，包含用户ID和权限信息，设置过期时间。
     *
     * @param userId         用户ID，作为Token主体（sub）
     * @param userPermission 用户权限，存放在Claim中
     * @return 生成的JWT字符串
     */
    public static String generateToken(String userId, String userPermission) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("permission", userPermission)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * 从Token中解析出用户ID（subject）。
     *
     * @param token JWT字符串
     * @return 用户ID
     */
    public static String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 从Token中解析出用户权限信息。
     *
     * @param token JWT字符串
     * @return 用户权限字符串
     */
    public static String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("permission", String.class);
    }

    /**
     * 验证Token的有效性，检查签名和过期时间。
     *
     * @param token JWT字符串
     * @return 如果有效返回true，否则返回false
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    /**
     * 生成 Refresh Token，有效期固定为 7 天（或你可以改为从配置读取）。
     *
     * @param userId 用户ID
     * @return refreshToken 字符串
     */
    public static String generateRefreshToken(String userId, String userPermission) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("permission", userPermission)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * 解析任意 JWT Token，返回 Claims（包含 userId、权限、过期时间等）
     *
     * @param token JWT 字符串
     * @return Claims 对象
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}

