package reservations.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;
import reservations.model.Employee;
import reservations.model.MeetingRoom;
import reservations.model.Reservation;
import reservations.model.dto.ReservationDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationToReservationDtoConverterTest {

    @InjectMocks
    ReservationToReservationDtoConverter converter;

    @SuppressWarnings("")
    MappingContext<Reservation, ReservationDto> mappingContext;

    @BeforeEach
    void setUp() {
        mappingContext = mock(MappingContext.class);
    }

    @Test
    void convert_ShouldBuildDto() {
        //given
        Employee employee = new Employee(1L, "Piotr", "Testowy", null);
        MeetingRoom meetingRoom = new MeetingRoom(2L, "Test Room", null);
        Reservation reservation = new Reservation(
                3L, employee, meetingRoom, "Test subject", LocalDateTime.of(2025, 5, 25, 10, 30), 90);

        //when
        when(mappingContext.getSource()).thenReturn(reservation);

        ReservationDto reservationDto=converter.convert(mappingContext);

        // then
        assertEquals(3L, reservationDto.getId());
        assertEquals("Testowy Piotr", reservationDto.getEmployeeFullName());
        assertEquals("Test Room", reservationDto.getMeetingRoom());
        assertEquals("Test subject", reservationDto.getSubject());
        assertEquals(reservation.getStartDateTime(), reservationDto.getStartDateTime());
        assertEquals(90, reservationDto.getDurationMin());
    }

}
