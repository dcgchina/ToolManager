package com.example.demojwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
@Service
public class JwtService {

    private final Key securtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long keyOut = 3600000; // 一小时

    // 加密
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(securtKey)
                .setExpiration(new Date(System.currentTimeMillis()+ keyOut))
                .compact();
    }

    // 解析
    public String vaildateTokenAndGetUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(securtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
