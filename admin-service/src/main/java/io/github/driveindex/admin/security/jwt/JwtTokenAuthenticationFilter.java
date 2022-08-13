package io.github.driveindex.admin.security.jwt;

import io.github.driveindex.admin.security.PasswordOnlyToken;
import io.github.driveindex.admin.security.SecurityConfig;
import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.manager.ConfigManager;
import io.github.driveindex.common.util.MD5Util;
import kotlin.text.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author sgpublic
 * @Date 2022/8/5 11:34
 */
@Slf4j
@Component
public class JwtTokenAuthenticationFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Charsets.UTF_8.name());

        String tag = request.getHeader(DriveIndexCommon.SECURITY_HEADER);
        if (tag != null) {
            String password = ConfigManager.getAdminPassword();
            String[] security = ((HttpServletRequest) req)
                    .getHeader(DriveIndexCommon.SECURITY_HEADER)
                    .split(",");
            String tagCheck;
            try {
                tagCheck = MD5Util.createTag(password, Long.parseLong(security[1]));
            } catch (Exception e) {
                log.warn("校验认证 tag 出错", e);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                PrintWriter writer = response.getWriter();
                writer.write(FailedResult.EXPIRED_TOKEN.toString());
                writer.flush();
                return;
            }
            if (tagCheck.equals(security[0])) {
                PasswordOnlyToken auth = PasswordOnlyToken.authenticated(password, SecurityConfig.AUTH_ADMIN);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(req, res);
    }
}