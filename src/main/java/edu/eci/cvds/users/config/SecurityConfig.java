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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> 
                auth.anyRequest().permitAll()
            );
            
        return http.build();
    }
}


// package edu.eci.cvds.users.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import edu.eci.cvds.users.security.JwtAuthenticationFilter;
// import edu.eci.cvds.users.security.JwtTokenProvider;
// import edu.eci.cvds.users.model.enums.Role;

// import java.util.Arrays;
// import java.util.List;

// /**
// * Security configuration for the User Administration microservice.
// * Defines authentication, authorization, CORS, and other security settings.
// * 
// * @author Jesús Pinzón (Team Bismuto)
// * @version 1.1
// * @since 2025-05-09
// */
// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//    @Value("${app.jwt.secret}")
//    private String jwtSecret;
   
//    @Value("${app.cors.allowed-origins}")
//    private List<String> allowedOrigins;
   
//    @Value("${app.auth.public-paths:/swagger-ui/**,/v3/api-docs/**,/actuator/health}")
//    private String[] publicPaths;
   
//    private final JwtTokenProvider tokenProvider;
   
//    public SecurityConfig(JwtTokenProvider tokenProvider) {
//        this.tokenProvider = tokenProvider;
//    }
   
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//            .csrf(csrf -> csrf.disable())
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .sessionManagement(session -> 
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(auth -> auth
//                // Public endpoints
//                .requestMatchers(publicPaths).permitAll()
//                // Admin only endpoints
//                .requestMatchers("/users/admin/**").hasAuthority(Role.ADMINISTRATOR.name())
//                // Staff endpoints
//                .requestMatchers("/users/staff/**").hasAnyAuthority(
//                    Role.ADMINISTRATOR.name(), 
//                    Role.MEDICAL_STAFF.name(),
//                    Role.MEDICAL_SECRETARY.name(),
//                    Role.WELLNESS_STAFF.name(),
//                    Role.TRAINER.name()
//                )
//                // Student specific endpoints
//                .requestMatchers("/users/students/**").hasAnyAuthority(
//                    Role.ADMINISTRATOR.name(),
//                    Role.STUDENT.name()
//                )
//                // Admin and staff can manage all users
//                .requestMatchers(
//                    "/users",
//                    "/users/**"
//                ).hasAnyAuthority(
//                    Role.ADMINISTRATOR.name(),
//                    Role.WELLNESS_STAFF.name()
//                )
//                // All other requests need authentication
//                .anyRequest().authenticated()
//            )
//            .addFilterBefore(
//                new JwtAuthenticationFilter(tokenProvider),
//                UsernamePasswordAuthenticationFilter.class
//            )
//            .build();
//    }
   
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(allowedOrigins);
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList(
//            "Authorization", "Content-Type", "X-Requested-With", 
//            "Accept", "Origin", "Access-Control-Request-Method", 
//            "Access-Control-Request-Headers"
//        ));
//        configuration.setExposedHeaders(Arrays.asList(
//            "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"
//        ));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
       
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
// }
