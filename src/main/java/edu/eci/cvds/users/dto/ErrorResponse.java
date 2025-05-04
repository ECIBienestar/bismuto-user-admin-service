package edu.eci.cvds.users.dto;

import java.time.LocalDateTime;

import lombok.Setter;
import lombok.Getter;

@Getter @Setter
public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
    private String path;
}
