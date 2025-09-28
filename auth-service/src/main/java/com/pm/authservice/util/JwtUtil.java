package com.pm.authservice.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] encodedKey = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(encodedKey);
    }

    public String generateToken(String email,String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*10)) // 10 hours
                .signWith(key)
                .compact();
    }
    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey)  key)
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException e){
            throw new JwtException("Invalid JWT");
        }
    }
}
