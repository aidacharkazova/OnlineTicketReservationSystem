package com.example.onlineticketreservationsystem.config;

import com.example.onlineticketreservationsystem.filter.JwtAuthFilter;
import com.example.onlineticketreservationsystem.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter authFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthFilter authFilter, CustomUserDetailsService customUserDetailsService) {
        this.authFilter = authFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", // Home page
                                "/login", // Login page
                                "/register", // Register page
                                "/css/**", // Allow CSS
                                "/js/**", // Allow JavaScript
                                "/images/**", // If you add an images folder
                                "/h2-console/**", // If you're using H2 console
                                "/auth/login",
                                "/auth/signUp",
                                "/auth/refresh", // Refresh token endpoint
                                "/api/events", // All events (public)
                                "/api/events/{id}", // Individual event details (public)
                                "/api/venues", // All venues (public)
                                "/api/venues/{id}", // Individual venue details (public)
                                "/api/schedules", // All schedules (public)
                                "/api/schedules/{id}", // Individual schedule details (public)
                                "/api/seats", // Seats by schedule (public for viewing, booking requires auth)
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}