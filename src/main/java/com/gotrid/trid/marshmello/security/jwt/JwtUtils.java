package com.gotrid.trid.marshmello.security.jwt;

import com.example.metamall.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getId().toString())) // Store user ID as subject
                .claim("username", userPrincipal.getUsername()) // Add username as claim
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Claims getClaimsFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserNameFromJwtToken(String token) {
        return getClaimsFromJwtToken(token).get("username", String.class);
    }

    public Long getUserIdFromJwtToken(String token) {
        return Long.parseLong(getClaimsFromJwtToken(token).getSubject());
    }

    public boolean validateJwtToken(String authToken) {
        String tokenPrefix = getTokenPrefix(authToken);

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            logger.debug("Valid JWT token: {}", tokenPrefix);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Malformed token {}: {}", tokenPrefix, e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.warn("Expired token {}: Expired at {}", tokenPrefix, e.getClaims().getExpiration());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported token {}: {}", tokenPrefix, e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Empty or null token: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error validating token {}: {}", tokenPrefix, e.getMessage());
        }
        return false;
    }

    private String getTokenPrefix(String token) {
        if (token == null) return "[null-token]";
        if (token.length() <= 10) return token;
        return token.substring(0, 10) + "...";
    }
}