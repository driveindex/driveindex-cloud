package io.github.driveindex.admin.security;

import io.github.driveindex.admin.exception.WrongPasswordException;
import io.github.driveindex.common.dto.result.FailedResult;
import kotlin.text.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author sgpublic
 * @Date 2022/8/6 13:51
 */
@Slf4j
@Component
public class IAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.debug("登陆失败", authException);
        FailedResult result = (authException instanceof WrongPasswordException) ?
                FailedResult.WRONG_PASSWORD : FailedResult.ANONYMOUS_DENIED;
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        try (
                PrintWriter writer = response.getWriter()
        ) {
            writer.write(result.toString());
        }
    }
}
