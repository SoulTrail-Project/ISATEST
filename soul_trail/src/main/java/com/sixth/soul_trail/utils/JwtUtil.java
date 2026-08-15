package com.sixth.soul_trail.utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "ST2026";   // 后续移到 yaml 用 @Value 注入
    private static final long EXPIRE_MS = 1000L * 60 * 60 * 24;          // 24 小时
    public static final long EXPIRE_SECONDS = EXPIRE_MS / 1000;  // = 86400秒

    //接收业务数据,生成token并返回
    public static String genToken(Long userId) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + EXPIRE_MS);
        return JWT.create()
                .withClaim("userId", userId)
                .withIssuedAt(now)
                .withExpiresAt(expire)
                .sign(Algorithm.HMAC256(SECRET));
    }

    //接收token,验证token,并返回业务数据
    public static Long parseToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("userId").asLong();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

}