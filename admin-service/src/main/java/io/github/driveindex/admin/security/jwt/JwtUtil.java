package io.github.driveindex.admin.security.jwt;

import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.manager.ConfigManager;
import io.github.driveindex.common.util.MD5Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author sgpublic
 * @Date 2022/8/9 8:29
 */

@Slf4j
@Component
@Import(ConfigManager.class)
@DependsOn("ConfigManager")
public class JwtUtil {
    private static Key secretKey;
    private static Long expired;

    @PostConstruct
    protected void init() {
        byte[] base = ConfigManager.getTokenSecurityKey();
        secretKey = Keys.hmacShaKeyFor(base);
        expired = ConfigManager.getTokenExpired();
    }

    public static String createToken(String password) throws NoSuchAlgorithmException {
        Claims claims = Jwts.claims();
        claims.setIssuer(DriveIndexCommon.APPLICATION_BASE_NAME);
        long now = new Date().getTime();
        claims.put(DriveIndexCommon.JWT_TAG, MD5Util.createTag(password, now));
        Date validity = new Date(now + expired);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }
}