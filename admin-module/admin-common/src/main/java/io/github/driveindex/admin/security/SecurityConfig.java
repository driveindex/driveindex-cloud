package io.github.driveindex.admin.security;

import io.github.driveindex.admin.security.jwt.JwtTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable();
        http.httpBasic().disable();
        http.addFilterBefore(password, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwt, PasswordOnlyAuthenticationProcessingFilter.class);
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests()
                .antMatchers("/api/admin/**").hasRole(ROLE_ADMIN)
                .anyRequest().permitAll();
        http.exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
