package reservations.model.command;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationCommand {
    @NotNull(message = "Select employee")
    private Long employeeId;

    @NotNull(message = "Select Meeting Room")
    private Long meetingRoomId;

    @Size(min = 3, message = "Minimum {min} characters")
    @NotBlank(message = "Meeting subject is required")
    private String subject;

    @NotNull(message = "Date and time is required")
    @Future(message = "Date and time must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDateTime;

    @NotNull (message = "Duration is required")
    @Min(value = 15, message = "The minimum time for a reservation is 15 minutes.")
    private Integer durationMin;

}
