package com.jobPortal.careerBridge.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RescheduleInterviewRequest {

    @NotNull(message = "New interview date and time is required")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Interview duration is required")
    @Min(value = 1,message = "Interview duration mus be at least 1 minute")
    @Max(value = 480,message = "Interview duration cannot exceed 480 minutes")
    private Integer durationMinutes;

}
