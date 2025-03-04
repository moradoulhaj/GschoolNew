package com.gschool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF (use cautiously in production)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 🚀 Allow all requests, disabling authentication
                )
                .formLogin(form -> form.disable()) // ❌ Disable default login page
                .httpBasic(httpBasic -> httpBasic.disable()); // ❌ Disable Basic Authentication

        return http.build();
    }
}
