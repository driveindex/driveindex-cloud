package io.github.driveindex.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.driveindex.common.dto.admin.LoginDto;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sgpublic
 * @Date 2022/8/6 8:55
 */
@Component
public class PasswordOnlyAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public PasswordOnlyAuthenticationProcessingFilter(
            IAuthenticationSuccessHandler onSuccess,
            IAuthenticationFailureHandler onFailed,
            IAuthenticationProvider provider,
            ObjectMapper mapper) {
        super("/api/login", new ProviderManager(provider));
        this.mapper = mapper;
        super.setAuthenticationSuccessHandler(onSuccess);
        super.setAuthenticationFailureHandler(onFailed);
    }

    private final ObjectMapper mapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        LoginDto dto = mapper.readValue(request.getInputStream(), LoginDto.class);
        String password = dto.getPassword();
        PasswordOnlyToken token = PasswordOnlyToken.unauthenticated(password);
        return getAuthenticationManager().authenticate(token);
    }
}
