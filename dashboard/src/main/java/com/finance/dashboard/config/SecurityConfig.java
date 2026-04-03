package com.finance.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// security configuration for role-based access
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/records/**").hasAnyRole("ADMIN","ANALYST","VIEWER")
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        return http.build();
    }
    // authentication provider using UserDetailsService
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl service,
                                                         BCryptPasswordEncoder encoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(service);
        authProvider.setPasswordEncoder(encoder);

        return authProvider;
    }
}