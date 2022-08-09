package io.github.driveindex.admin.security.jwt;

import io.github.driveindex.admin.security.SecurityConfig;
import io.github.driveindex.common.DriveIndexCommon;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * @author sgpublic
 * @Date 2022/8/9 8:29
 */

@Slf4j
@Component
public class JwtUtil {
    private static final Key secretKey;
    private static final JwtParser parser;
    private static final Long expired = 3600_000L;

    static {
        byte[] base = DriveIndexCommon.JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        if (base.length < 128) {
            byte[] clone = base.clone();
            base = new byte[128];
            for (int i = 0; i < 128; i++) {
                base[i] = clone[i % clone.length];
            }
        }
        secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(base));
        parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public static String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", SecurityConfig.ROLE_ADMIN);

        Date now = new Date();
        Date validity = new Date(now.getTime() + expired);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public static String resolveUser(HttpServletRequest req) {
        return req.getHeader(SecurityConfig.SECURITY_HEADER);
    }
}