package io.github.driveindex.gateway.util;

import io.github.driveindex.common.DriveIndexCommon;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/9 8:37
 */
public class JwtChecker {
    public static final String TOKEN_KEY = "DriveIndex-Authentication";
    public static final String SECURITY_HEADER = "DriveIndex-User";
    private static final JwtParser parser;

    static {
        byte[] base = DriveIndexCommon.JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        if (base.length < 128) {
            byte[] clone = base.clone();
            base = new byte[128];
            for (int i = 0; i < 128; i++) {
                base[i] = clone[i % clone.length];
            }
        }
        Key secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(base));
        parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    /**
     * 从 ServerHttpRequest 中获取 token
     * @return 返回 token 字符串，可能为 null
     */
    @Nullable
    public static String resolveToken(@NonNull ServerHttpRequest req) {
        List<String> list = req.getHeaders().get(TOKEN_KEY);
        if (list == null || list.isEmpty()) return null;
        return list.get(0);
    }

    public static String getUsername(String token) {
        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 校验 token
     * @param token token 字符串，不可为 null
     * @return 返回是否有效
     */
    public static boolean validateToken(@NonNull String token) {
        try {
            Jws<Claims> claims = parser.parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
