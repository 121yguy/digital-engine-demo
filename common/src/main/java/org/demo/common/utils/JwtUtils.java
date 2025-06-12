package org.demo.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtUtils {

    public static String createJwt(Integer uid, String secret, long exp) {
        Map<String,Object> claim = new HashMap<>();
        claim.put("userId",uid);
        return JWT.create()
                .withClaim("user", claim)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + exp * 1000))
                .sign(Algorithm.HMAC256(secret));
    }

    public static String createJwtWithUidAndPermissions(Integer uid, List<String> permissions, String secret, long exp) {
        Map<String,Object> claim = new HashMap<>();
        claim.put("userId",uid);
        claim.put("permissions", permissions);
        return JWT.create()
                .withClaim("user", claim)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + exp * 1000))
                .sign(Algorithm.HMAC256(secret));
    }


    /**
     *
     * @param token JWT令牌
     * @return 解析后的JWT令牌，以哈希表的形式返回，哈希表的值是Claim对象
     */
    public static Map<String, Claim> getJwtPayload(String token) {
        try {
            // 解析 JWT，解码并不进行签名验证
            DecodedJWT decodedJWT = JWT.decode(token);
            // 返回载体部分的 Claims
            return decodedJWT.getClaims();
        } catch (JWTDecodeException e) {
            throw new RuntimeException("无效的JWT格式", e);
        }
    }



    public static Map<String, Claim> parseJwt(String token, String secret) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getClaims();
        }catch (Exception e){
            throw new RuntimeException("JWT解析错误");
        }
    }
}
