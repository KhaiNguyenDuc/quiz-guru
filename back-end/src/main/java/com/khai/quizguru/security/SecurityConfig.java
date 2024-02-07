package com.khai.quizguru.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khai.quizguru.filter.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for security settings.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    /**
     * Configures the authentication manager.
     *
     * @param config The authentication configuration.
     * @return The configured authentication manager.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates a bean for the JWT authentication filter.
     *
     * @return The JWT authentication filter bean.
     */
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Configures the security filter chain.
     *
     * @param http The HttpSecurity object.
     * @return The configured security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/member/**").authenticated()
                        .requestMatchers("/api/v1/quiz/**").authenticated()
                        .requestMatchers("/api/v1/users/**").authenticated()
                        .requestMatchers("/api/v1/records/**").authenticated()
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    /**
     * Creates a bean for the password encoder.
     *
     * @return The password encoder bean.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a bean for the ModelMapper.
     *
     * @return The ModelMapper bean.
     */
    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

    /**
     * Creates a bean for the ObjectMapper.
     *
     * @return The ObjectMapper bean.
     */
    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
