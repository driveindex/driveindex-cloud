package io.github.driveindex.admin.security;

import io.github.driveindex.admin.security.jwt.JwtUtil;
import io.github.driveindex.common.dto.admin.AccessTokenDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.github.driveindex.common.manager.ConfigManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        try {
            String jwt = JwtUtil.createToken(ConfigManager.Password.getValue());
            AccessTokenDto dto = new AccessTokenDto();
            dto.setToken(jwt);
            writer.write(SuccessResult.of(dto).toString());
        } catch (NoSuchAlgorithmException e) {
            log.warn("token 创建失败", e);
            writer.write(FailedResult.INTERNAL_SERVER_ERROR.toString());
        }
        writer.flush(); writer.close();
    }
}
