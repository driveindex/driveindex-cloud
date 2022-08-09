package io.github.driveindex.admin.security.jwt;

import io.github.driveindex.admin.security.PasswordOnlyToken;
import io.github.driveindex.admin.security.PasswordOnlyUserService;
import io.github.driveindex.common.dto.result.FailedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequiredArgsConstructor
@Component
public class JwtTokenAuthenticationFilter extends GenericFilterBean {
    private final PasswordOnlyUserService user;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            String authedUser = JwtUtil.resolveUser(request);
            if (authedUser != null) {
                UserDetails details = user.loadUserByUsername(authedUser);
                Authentication auth = PasswordOnlyToken.authenticated(
                        details.getPassword(), details.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter writer = response.getWriter();
            writer.write(FailedResult.EXPIRED_TOKEN.toString());
            writer.flush();
            return;
        }

        filterChain.doFilter(req, res);
    }
}