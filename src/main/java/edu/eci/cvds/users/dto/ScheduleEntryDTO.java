package edu.eci.cvds.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for staff schedule entries showing availability periods.
 *
 * @author Jesús Pinzón (Team Bismuto)
 * @version 1.1
 * @since 2025-05-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Staff availability time slot")
public class ScheduleEntryDTO {
    
    /**
     * Unique identifier for the schedule entry
     */
    @Schema(description = "Unique ID of the schedule entry", example = "1")
    private Long id;
    
    /**
     * Start date and time of the availability period
     */
    @NotNull(message = "Start time cannot be null")
    @FutureOrPresent(message = "Start time must be in the present or future")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Start time of availability", 
            example = "2025-06-01T09:00:00")
    private LocalDateTime startTime;
    
    /**
     * End date and time of the availability period
     */
    @NotNull(message = "End time cannot be null")
    @FutureOrPresent(message = "End time must be in the present or future")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "End time of availability", 
            example = "2025-06-01T12:00:00")
    private LocalDateTime endTime;
}
