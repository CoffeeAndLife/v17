package com.hgz.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

/**
 * @author huangguizhao
 */
public class JwtTest {

    @Test
    public void createJwttokenTest(){
        JwtBuilder builder = Jwts.builder()
                .setId("666").setSubject("行走在牛A的路上")
                .setIssuedAt(new Date())
                //添加自定义属性
                /*.claim("role","admin")
                .claim("other","other")*/
                .setExpiration(new Date(new Date().getTime()+600000))
                .signWith(SignatureAlgorithm.HS256,"huangguizhao");

        String jwtToken = builder.compact();
        System.out.println(jwtToken);

        //eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLooYzotbDlnKjniZtB55qE6Lev5LiKIiwiaWF0IjoxNTczNjk1OTk4LCJleHAiOjE1NzM2OTYwMjh9.270kxBqf6fhQMrZJMpnZWsevtkmHZi62tr6F9s2xSZg
    }

    @Test
    public void parseToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGiOiI2NjYiLCJzdWIiOiLooYzotbDlnKjniZtB55qE6Lev5LiKIiwiaWF0IjoxNTczNjk2MTEyLCJleHAiOjE1NzM2OTY3MTJ9.ZmBT8rt7KyMPG6WLikYjFT9-G8-dpm4zH6Xl-kQbVG4";
        Claims claims = Jwts.parser().setSigningKey("huangguizhao")
                .parseClaimsJws(token).getBody();

        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.getExpiration());
        //获取属性
        //System.out.println(claims.get("role"));

        //过期的时候，会抛出异常：ExpiredJwtException
        //令牌存在问题的时候，会抛出异常：SignatureException

    }
}
