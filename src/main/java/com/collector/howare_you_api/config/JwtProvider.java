package com.collector.howare_you_api.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}") //
    private String secretKey;

    @Value("${jwt.expiration}") //
    private long expiration;


    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String create(String userId) {
        Claims claims = Jwts.claims().setSubject(userId).build();
        Date now = new Date();

        System.out.println("@@@@@@@2"+expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 서명 알고리즘 적용
                .compact();
    }
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String getUserId(String token) {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
}

