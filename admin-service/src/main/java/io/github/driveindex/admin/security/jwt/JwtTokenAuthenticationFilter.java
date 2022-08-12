package io.github.driveindex.admin.security.jwt;

import io.github.driveindex.admin.security.PasswordOnlyToken;
import io.github.driveindex.admin.security.SecurityConfig;
import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.manager.ConfigManager;
import io.github.driveindex.common.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
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
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            String token = request.getHeader(DriveIndexCommon.SECURITY_HEADER);
            if (token != null) {
                String password = ConfigManager.getAdminPassword();
                String[] security = ((HttpServletRequest) req)
                        .getHeader(DriveIndexCommon.SECURITY_HEADER)
                        .split(",");
                String tagCheck = MD5Util.createTag(password, Long.parseLong(security[1]));
                if (tagCheck.equals(security[0])) {
                    PasswordOnlyToken auth = PasswordOnlyToken.authenticated(password, SecurityConfig.AUTH_ADMIN);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(req, res);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter writer = response.getWriter();
            writer.write(FailedResult.EXPIRED_TOKEN.toString());
            writer.flush();
        }
    }
}