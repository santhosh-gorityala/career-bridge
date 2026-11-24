package com.jobPortal.careerBridge.dto.request;

import com.jobPortal.careerBridge.enums.InterviewMode;
import com.jobPortal.careerBridge.enums.InterviewType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ScheduleInterviewRequest {

    @NotBlank(message = "Round name is required")
    @Size(max = 100)
    private String roundName;

    @NotNull(message = "Interview type is required")
    private InterviewType interviewType;

    @NotNull(message = "Interview date and time is required")
    @Future(message = "Interview must be scheduled in future")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Duration is required")
    @Min(value = 1,message = "Duration must be at least 1 minute")
    @Max(value = 480,message = "Duration cannot exceed 8 hours")
    private Integer durationMinutes;

    @NotBlank(message = "Timezone is required")
    @Size(max = 50)
    private String timezone;

    @NotNull(message = "Interview mode is required")
    private InterviewMode mode;

    @Size(max = 1000)
    private String meetingLink;

    @Size(max=500)
    private String location;

    @Size(max = 2000)
    private String instructions;

    @NotNull(message = "Interviewer ID is required")
    private Long interviewerId;

}
