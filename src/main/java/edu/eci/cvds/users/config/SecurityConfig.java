package edu.eci.cvds.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) Deshabilita CSRF para que los POST/DELETE en tests no peten con 403
                .csrf(csrf -> csrf.disable())
                // 2) Permite libre acceso a todos los /users/**
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/users/**").permitAll()
                        // Si quieres proteger otros endpoints en el futuro:
                        //.anyRequest().authenticated()
                );
        return http.build();
    }
}

