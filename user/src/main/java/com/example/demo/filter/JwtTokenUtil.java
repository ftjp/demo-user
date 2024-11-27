package com.example.demo.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author LJP
 * @date 2024/11/27 14:19
 */
@Component
public class JwtTokenUtil {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    public String generateToken(String accessToken) {


        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationDate);

        return Jwts.builder()
//                .setSubject(userPrincipal.getName())
                .setSubject(accessToken)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getAccessTokenFromJWT(String authorization) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(authorization)
                .getBody();

        return claims.getSubject();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            // MalformedJwtException - Invalid JWT token
        } catch (ExpiredJwtException ex) {
            // ExpiredJwtException - Expired JWT token
        } catch (UnsupportedJwtException ex) {
            // UnsupportedJwtException - Unsupported JWT token
        } catch (IllegalArgumentException ex) {
            // IllegalArgumentException - JWT claims string is empty or null
        }
        return false;
    }
}