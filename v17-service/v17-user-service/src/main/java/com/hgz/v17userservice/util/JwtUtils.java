package com.hgz.v17userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author huangguizhao
 */
public class JwtUtils {

    //密钥由调用方来决定
    private String secretKey;

    //有效期也由调用方来决定
    private long ttl;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String createJwtToken(String id,String subject){
        long now = System.currentTimeMillis();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(id).setSubject(subject)
                .setIssuedAt(new Date(now))
                .signWith(SignatureAlgorithm.HS256,secretKey);
        if(ttl > 0){
            jwtBuilder.setExpiration(new Date(now+ttl));
        }
        return jwtBuilder.compact();
    }

    public Claims parseJwtToken(String jwtToken){
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(jwtToken).getBody();
    }
}
