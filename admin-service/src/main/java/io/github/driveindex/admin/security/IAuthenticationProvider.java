package io.github.driveindex.admin.security;

import io.github.driveindex.common.manager.ConfigManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:01
 */
@Component
@RequiredArgsConstructor
public class IAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        assert authentication instanceof PasswordOnlyToken;
        PasswordOnlyToken token = (PasswordOnlyToken) authentication;
        try {
            if (token.getCredentials().equals(ConfigManager.getAdminPassword())) {
                return PasswordOnlyToken.authenticated(token.getCredentials(), SecurityConfig.AUTH_ADMIN);
            }
            throw new BadCredentialsException("密码错误");
        } catch (IOException e) {
            throw new BadCredentialsException("登录处理出错", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PasswordOnlyToken.class);
    }
}
