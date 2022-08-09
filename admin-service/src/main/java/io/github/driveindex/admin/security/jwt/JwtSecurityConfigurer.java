package io.github.driveindex.admin.security.jwt;

import io.github.driveindex.admin.security.PasswordOnlyAuthenticationProcessingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author sgpublic
 * @Date 2022/8/5 11:32
 */
@RequiredArgsConstructor
@Component
public class JwtSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenAuthenticationFilter jwt;
    private final PasswordOnlyAuthenticationProcessingFilter password;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(password, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwt, PasswordOnlyAuthenticationProcessingFilter.class);
    }
}