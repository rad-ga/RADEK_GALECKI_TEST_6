package reservations.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationDto {
    private Long id;
    private String employeeFullName;
    private String meetingRoom;
    private String subject;
    private LocalDateTime startDateTime;
    private int durationMin;

}
