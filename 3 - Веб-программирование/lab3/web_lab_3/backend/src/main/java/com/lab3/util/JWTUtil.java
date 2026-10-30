package com.lab3.util;

import com.lab3.Exceptions.ServerException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import java.security.Key;
import java.util.Date;

import java.util.logging.Logger;

@ApplicationScoped
public class JWTUtil {
    private static final Logger logger = Logger.getLogger(JWTUtil.class.getName());

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30 минут


    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }


    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            logger.warning("Token validation failed: " + e.getMessage());
            throw new ServerException("Error in token validation");
        }
    }
}