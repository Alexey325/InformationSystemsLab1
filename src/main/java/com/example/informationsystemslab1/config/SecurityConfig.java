package com.example.informationsystemslab1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ID_PATTERN = "{id:[0-9]+}";
    private static final String[] RESOURCES = {"labworks", "disciplines", "locations", "persons"};

    private static final String[] GET_BY_ID_PATHS = Arrays.stream(RESOURCES)
            .map(resource -> "/api/" + resource + "/" + ID_PATTERN)
            .toArray(String[]::new);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, GET_BY_ID_PATHS).denyAll()
                        .anyRequest().permitAll()
                )

                // включённый CSRF требует токен в каждом POST/PUT/DELETE, которого фронтенд не шлёт
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
