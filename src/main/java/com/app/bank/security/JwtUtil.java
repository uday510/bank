package com.app.bank.security;

import com.app.bank.util.EnvConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtil {

    private static final String SECRET = EnvConfig.get("JWT_SECRET");
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(Authentication authentication) {
        return Jwts.builder()
                .issuer("Bank App")
                .subject(authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(EnvConfig.get("JWT_EXPIRATION"))))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims validateToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static Authentication getAuthentication(Claims claims) {
        String username = claims.getSubject();
        String authorities = claims.get("authorities", String.class);

        if (username == null || authorities == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(
                username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
        );
    }
}