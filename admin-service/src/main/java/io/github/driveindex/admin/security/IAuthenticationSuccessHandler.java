package io.github.driveindex.admin.security;

import io.github.driveindex.admin.security.jwt.JwtUtil;
import io.github.driveindex.common.dto.admin.AccessTokenDto;
import io.github.driveindex.common.dto.result.SuccessResult;
import kotlin.text.Charsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:01
 */
@Component
@RequiredArgsConstructor
public class IAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PasswordOnlyToken token = (PasswordOnlyToken) authentication;
        String jwt = JwtUtil.createToken(token.getCredentials());
        AccessTokenDto dto = new AccessTokenDto();
        dto.setToken(jwt);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        writer.write(SuccessResult.of(dto).toString());
        writer.flush(); writer.close();
    }
}
