package io.github.driveindex.admin.security;

import io.github.driveindex.admin.security.jwt.JwtSecurityConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.NullSecurityContextRepository;

/**
 * @author sgpublic
 * @Date 2022/8/3 19:44
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";

    public static final String TOKEN_KEY = "DriveIndex-Authentication";
    public static final String SECURITY_HEADER = "DriveIndex-User";

    private final JwtSecurityConfigurer configurer;
    private final IAuthenticationEntryPoint entryPoint;
    private final IAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .headers().frameOptions().disable();
        http.sessionManagement().disable().securityContext()
                .securityContextRepository(new NullSecurityContextRepository());
        http.authorizeHttpRequests()
                .antMatchers("/api/admin/**").authenticated()
                .anyRequest().permitAll();
        http.exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        http.apply(configurer);
        return http.build();
    }
}
