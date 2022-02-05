package com.wq.util.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-02-05-12:22
 */
public class JwtUtils {
    /**
     * JwtToken 的过期时间, 30 天
     */
    private static final long EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;

    /**
     * 校验 JwtToken 是否将要过期, 1 天
     */
    private static final long NEED_REFRESH_TIME = 1000 * 60 * 60 * 24;


    /**
     * 生成 JwtToken
     * <p> JwtToken 内存储的信息为 用户 id, 用户名, 为 String 类型的数据</p>
     * <p>传入的签名为用户加密后的密码</p>
     * @param secret 用户加密后的密码
     * @param payload 有效载荷
     * @return JwtToken
     */
    public static String getJwtToken (String secret, Map<String, String> payload) {

        // 获取当前时间
        Date nowTime = new Date();

        // 设置过期时间
        Date expireTime = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        // 对密匙进行哈希加密
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                // 存入用户 id
                .withClaim("userId", payload.get("userId"))
                // 存入用户名
                .withClaim("userName", payload.get("userName"))
                // 存入生成 jwtToken 的时间
                .withIssuedAt(nowTime)
                // 存入 JwtToken 的过期时间
                .withExpiresAt(expireTime)
                // 设置签名
                .sign(algorithm);

    }


    /**
     * 校验 jwtToken 是否正确
     * @param token JwtToken
     * @param secret 加密后的密码
     * @return true : 正确 | false : 错误
     */
    public static boolean verifyToken (String token, String secret) {

        try {
            // 加密签名
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // 校验
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (IllegalArgumentException | JWTVerificationException e) {
            return false;
        }
    }


    /**
     * 获取 JwtToken 中的用户名
     * 如果 JwtToken 有误, 则返回 null
     * @param token JwtToken
     * @return 用户名
     */
    public static String getUserName (String token) {

        DecodedJWT decode = JWT.decode(token);

        return decode.getClaim("userName").asString();
    }


    /**
     * 获取 JwtToken 中的用户 id
     * 如果 JwtToken 有误, 则返回 null
     * @param token JwtToken
     * @return 用户 id
     */
    public static Long getUserId (String token) {

        DecodedJWT decode = JWT.decode(token);

        return Long.parseLong (decode.getClaim("userId").asString());
    }


    /**
     * 判断 JwtToken 是否过期
     * @param token JwtToken
     * @return true : 以过期 | false : 未过期
     */
    public static boolean isExpired (String token) {

        DecodedJWT decode = JWT.decode(token);

        return decode.getExpiresAt().getTime() < System.currentTimeMillis();
    }


    /**
     * 检查是否需要重新生成 JwtToken
     * JwtToken 剩余时间还剩一天即返回 true
     * @param token JwtToken
     * @return true : 需要 | false : 不需要
     */
    public static boolean judgeRefresh (String token) {

        DecodedJWT decode = JWT.decode(token);

        return decode.getExpiresAt().getTime() - NEED_REFRESH_TIME < System.currentTimeMillis();

    }
}
