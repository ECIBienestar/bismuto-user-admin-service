package edu.eci.cvds.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main entry point for the User Administration microservice.
 * 
 * This Spring Boot application provides user management functionalities for the
 * ECI-Bienestar system.
 * It uses OpenFeign for declarative REST client integration with other
 * microservices.
 * 
 * The service handles user creation, authentication, role management, and
 * related operations
 * for the Bismuto application ecosystem.
 */
@EnableFeignClients
@SpringBootApplication
public class UserAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAdminApplication.class, args);
    }
}
