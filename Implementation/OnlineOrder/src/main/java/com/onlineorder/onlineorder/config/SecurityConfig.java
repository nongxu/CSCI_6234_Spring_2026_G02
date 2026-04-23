package com.onlineorder.onlineorder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)  // disable built-in logout filter to avoid redirect interference
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/signup", "/login", "/logout", "/me", "/restaurants/**", "/owner/signup", "/owner/login", "/owner/restaurant", "/owner/restaurants", "/cart", "/cart/summary", "/cart/confirm", "/orders", "/orders/*/cancel", "/uploads/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
