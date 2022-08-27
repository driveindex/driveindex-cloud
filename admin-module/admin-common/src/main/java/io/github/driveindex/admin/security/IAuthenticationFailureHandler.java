package io.github.driveindex.admin.security;

import io.github.driveindex.admin.exception.WrongPasswordException;
import io.github.driveindex.common.dto.result.FailedResult;
import kotlin.text.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:01
 */
@Slf4j
@Component
public class IAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Map<Class<? extends AuthenticationException>, FailedResult> result = new HashMap<>();
    static {
        result.put(WrongPasswordException.class, FailedResult.WRONG_PASSWORD);
        result.put(AuthenticationServiceException.class, FailedResult.UNSUPPORTED_REQUEST);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.debug("登录失败", exception);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        FailedResult failed = result.getOrDefault(exception.getClass(), FailedResult.INTERNAL_SERVER_ERROR);
        PrintWriter writer = response.getWriter();
        writer.write(failed.toString());
        writer.flush(); writer.close();
    }
}
