package io.github.driveindex.admin.security;

import io.github.driveindex.admin.h2.repository.UserRepository;
import io.github.driveindex.common.DriveIndexCommon;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/3 20:12
 */
@RequiredArgsConstructor
@Component
public class PasswordOnlyUserService implements UserDetailsService {
    private final UserRepository user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || user.findUserByName(username) == null)
            throw new UsernameNotFoundException(username);
        String password = user.getUserPassword(username);
        if (password == null) // 传入默认密码
            password = DriveIndexCommon.DEFAULT_PASSWORD;
        return new User(DriveIndexCommon.APPLICATION_BASE_NAME, password, List.of(
                new SimpleGrantedAuthority(SecurityConfig.ROLE_ADMIN)
        ));
    }
}