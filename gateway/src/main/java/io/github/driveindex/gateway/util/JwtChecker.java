package io.github.driveindex.gateway.util;

import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.manager.ConfigManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

/**
 * @author sgpublic
 * @Date 2022/8/9 8:37
 */
@Slf4j
@Component
public class JwtChecker {
    private static JwtParser parser;

    @PostConstruct
    protected void init() {
        byte[] base = ConfigManager.getTokenSecurityKey();
        Key secretKey = Keys.hmacShaKeyFor(base);
        parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    /**
     * 获取 token 中的 tag 信息
     * @param token token 字符串，不可为 null
     * @return 返回 tag，解析失败返回 null
     */
    @Nullable
    public static String findTag(@NonNull String token) {
        try {
            Claims claims = parser.parseClaimsJws(token).getBody();
            if (claims.getExpiration().before(new Date())) {
                log.debug("token 过期");
                return null;
            }
            if (!DriveIndexCommon.APPLICATION_BASE_NAME.equals(claims.getIssuer())) {
                log.debug("未知的 token 签发者");
                return null;
            }
            String tag = claims.get(DriveIndexCommon.JWT_TAG, String.class);
            return tag + "," + claims.getIssuedAt().getTime();
        } catch (JwtException | IllegalArgumentException ignore) { }
        return null;
    }
}
