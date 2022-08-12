package io.github.driveindex.admin.security;

import io.github.driveindex.admin.security.jwt.JwtTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;

import java.util.Collections;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/3 19:44
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final List<SimpleGrantedAuthority> AUTH_ADMIN =
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + ROLE_ADMIN));

    private final IAuthenticationEntryPoint entryPoint;
    private final IAccessDeniedHandler accessDeniedHandler;
    private final JwtTokenAuthenticationFilter jwt;
    private final PasswordOnlyAuthenticationProcessingFilter password;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .headers().frameOptions().disable();
        http.addFilterBefore(password, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwt, PasswordOnlyAuthenticationProcessingFilter.class);
        http.sessionManagement().disable().securityContext()
                .securityContextRepository(new NullSecurityContextRepository());
        http.authorizeHttpRequests()
                .antMatchers("/api/admin/**").hasRole(ROLE_ADMIN)
                .anyRequest().permitAll();
        http.exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        return http.build();
    }
}
