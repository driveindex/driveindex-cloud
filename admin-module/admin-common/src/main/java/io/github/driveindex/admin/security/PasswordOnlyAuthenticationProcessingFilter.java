package io.github.driveindex.admin.security;

import io.github.driveindex.common.dto.admin.LoginDto;
import io.github.driveindex.common.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author sgpublic
 * @Date 2022/8/6 8:55
 */
@Slf4j
@Component
public class PasswordOnlyAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public PasswordOnlyAuthenticationProcessingFilter(
            IAuthenticationSuccessHandler onSuccess,
            IAuthenticationFailureHandler onFailed,
            IAuthenticationProvider provider) {
        super(new RequestMatcher() {
            private static final String url = "/api/login";
            private static final String method = HttpMethod.POST.name();

            @Override
            public boolean matches(HttpServletRequest request) {
                return method.equals(request.getMethod())
                        && url.contentEquals(request.getRequestURI());
            }

            @Override
            public MatchResult matcher(HttpServletRequest request) {
                return matches(request) ? MatchResult.match() : MatchResult.notMatch();
            }
        }, new ProviderManager(provider));
        super.setAuthenticationSuccessHandler(onSuccess);
        super.setAuthenticationFailureHandler(onFailed);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        ServletInputStream inputStream = request.getInputStream();
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        LoginDto dto = GsonUtil.fromJson(content, LoginDto.class);
        String password = dto.getPassword();
        PasswordOnlyToken token = PasswordOnlyToken.unauthenticated(password);
        return getAuthenticationManager().authenticate(token);
    }
}
