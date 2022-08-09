package io.github.driveindex.admin.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:01
 */
@Component
@RequiredArgsConstructor
public class IAuthenticationProvider implements AuthenticationProvider {
    private final PasswordOnlyUserService user;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        assert authentication instanceof PasswordOnlyToken;
        PasswordOnlyToken token = (PasswordOnlyToken) authentication;
        UserDetails user = this.user.loadUserByUsername(token.getCredentials());
        if (token.getCredentials().equals(user.getPassword())) {
            return PasswordOnlyToken.authenticated(token.getCredentials(), user.getAuthorities());
        }
        throw new BadCredentialsException("密码错误");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PasswordOnlyToken.class);
    }
}
