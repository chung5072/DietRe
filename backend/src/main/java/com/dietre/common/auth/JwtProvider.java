package com.dietre.common.auth;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private String expiration;

    public String createToken(String subject, Long id) {
        Date now = new Date();

        Map<String, Object> headers = new HashMap<>();
        headers.put(Header.TYPE, Header.JWT_TYPE);
        headers.put("alg", "HS256");
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("userId", id);

        System.out.println(expiration + "EXPIRATION");
        System.out.println(secretKey + "secretKEY");
        String jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setExpiration(new Date(now.getTime() + Integer.parseInt(expiration)))
                .setSubject(subject) // 토큰 제목
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .compact();

        return jwt;
    }

    public Claims parseToken(String token) throws UnsupportedJwtException, MalformedJwtException,
            ExpiredJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

}
