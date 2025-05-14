package reservations.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;
import reservations.model.Employee;
import reservations.model.MeetingRoom;
import reservations.model.Reservation;
import reservations.model.command.ReservationCommand;
import reservations.repository.EmployeeRepository;
import reservations.repository.MeetingRoomRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationCommandToReservationConverterTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    MeetingRoomRepository meetingRoomRepository;
    @InjectMocks
    ReservationCommandToReservationConverter converter;
    @SuppressWarnings("")
    MappingContext<ReservationCommand, Reservation> mappingContext;

    @BeforeEach
    void setUp() {
        mappingContext = mock(MappingContext.class);
    }

    @Test
    void convert_ShouldMapFields() {
        //given
        ReservationCommand reservationCommand = new ReservationCommand(
                1L, 2L, "Subject: Test", LocalDateTime.of(2025, 5, 20, 12, 0), 60);
        Employee employee = new Employee(1L, "Piotr", "Testowy", null);
        MeetingRoom meetingRoom = new MeetingRoom(2L, "MR Test", null);

        //when
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(meetingRoomRepository.findById(2L)).thenReturn(Optional.of(meetingRoom));
        when(mappingContext.getSource()).thenReturn(reservationCommand);

        Reservation newReservation=converter.convert(mappingContext);

        //then
        assertThat(newReservation.getEmployee()).isSameAs(employee);
        assertThat(newReservation.getMeetingRoom()).isSameAs(meetingRoom);
        assertThat(newReservation.getSubject()).isEqualTo("Subject: Test");
        assertThat(newReservation.getStartDateTime()).isEqualTo(reservationCommand.getStartDateTime());
        assertThat(newReservation.getDurationMin()).isEqualTo(60);

        verify(employeeRepository).findById(1L);
        verify(meetingRoomRepository).findById(2L);

    }

}
