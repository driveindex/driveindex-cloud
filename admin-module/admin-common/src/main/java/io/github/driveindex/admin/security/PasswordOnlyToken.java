package io.github.driveindex.admin.security;

import io.github.driveindex.common.DriveIndexCommon;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:09
 */
public class PasswordOnlyToken extends UsernamePasswordAuthenticationToken {
    private PasswordOnlyToken(String password) {
        super(DriveIndexCommon.APPLICATION_BASE_NAME, password);
    }

    private PasswordOnlyToken(String password, Collection<? extends GrantedAuthority> authorities) {
        super(DriveIndexCommon.APPLICATION_BASE_NAME, password, authorities);
    }

    @Override
    public String getCredentials() {
        return (String) super.getCredentials();
    }

    public static PasswordOnlyToken unauthenticated(String password) {
        return new PasswordOnlyToken(password);
    }

    public static PasswordOnlyToken authenticated(String password, Collection<? extends GrantedAuthority> authorities) {
        return new PasswordOnlyToken(password, authorities);
    }
}
