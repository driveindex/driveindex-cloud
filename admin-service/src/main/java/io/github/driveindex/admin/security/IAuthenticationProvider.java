package io.github.driveindex.admin.security;

import io.github.driveindex.common.manager.ConfigManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        assert authentication instanceof PasswordOnlyToken;
        PasswordOnlyToken token = (PasswordOnlyToken) authentication;
        if (token.getCredentials().equals(ConfigManager.getAdminPassword())) {
            return PasswordOnlyToken.authenticated(token.getCredentials(), SecurityConfig.AUTH_ADMIN);
        }
        throw new BadCredentialsException("密码错误");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PasswordOnlyToken.class);
    }
}
