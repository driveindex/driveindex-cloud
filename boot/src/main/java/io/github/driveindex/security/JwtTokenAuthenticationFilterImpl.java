package io.github.driveindex.security;

import io.github.driveindex.admin.security.PasswordOnlyToken;
import io.github.driveindex.admin.security.SecurityConfig;
import io.github.driveindex.admin.security.jwt.JwtTokenAuthenticationFilter;
import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.gateway.util.JwtChecker;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author sgpublic
 * @Date 2022/8/27 16:58
 */
@Component
public class JwtTokenAuthenticationFilterImpl extends JwtTokenAuthenticationFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith("/api/login") || !requestURI.startsWith("/api/admin")) {
            chain.doFilter(request, response);
            return;
        }
        String token = req.getHeader(DriveIndexCommon.TOKEN_KEY);
        String tag = JwtChecker.findTag(token);
        if (tag != null) {
            SecurityContextHolder.getContext().setAuthentication(
                    PasswordOnlyToken.authenticated(tag, SecurityConfig.AUTH_ADMIN)
            );
            chain.doFilter(request, response);
            return;
        }
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        try (
                PrintWriter writer = response.getWriter()
        ) {
            writer.write(FailedResult.EXPIRED_TOKEN.toString());
        }
    }
}
