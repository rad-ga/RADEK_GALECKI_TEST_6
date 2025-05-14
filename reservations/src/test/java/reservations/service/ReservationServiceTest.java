package reservations.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reservations.model.MeetingRoom;
import reservations.model.Reservation;
import reservations.model.command.ReservationCommand;
import reservations.repository.MeetingRoomRepository;
import reservations.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationServiceTest {

    @MockitoBean
    MeetingRoomRepository meetingRoomRepository;

    @MockitoBean
    ReservationRepository reservationRepository;

    @MockitoBean
    ModelMapper modelMapper;

    @Autowired
    ReservationService service;

    @Test
    void saveReservation_shouldSaveAndReturnTrue() {
        //given
        ReservationCommand reservationCommand = new ReservationCommand(1L, 1L, "Meeting test", LocalDateTime.now().plusDays(1), 30);
        MeetingRoom meetingRoom = new MeetingRoom(1L, "MR Test", null);

        when(meetingRoomRepository.findByIdWithLock(1L)).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findByMeetingRoom(meetingRoom)).thenReturn(Collections.emptyList());

        Reservation newSavedReservation = new Reservation();

        when(modelMapper.map(reservationCommand, reservations.model.Reservation.class)).thenReturn(newSavedReservation);

        //when
        boolean result = service.saveReservation(reservationCommand);

        //then
        assertThat(result).isTrue();
        verify(reservationRepository).save(newSavedReservation);
    }

    @Test
    void saveReservation_shouldReturnFalse_meetingRoomBooked() {
        //given
        LocalDateTime reservationStart = LocalDateTime.of(2025, 5, 30, 10, 0);
        ReservationCommand reservationCommand = new ReservationCommand(
                2L, 2L, "Meeting test2", reservationStart.plusMinutes(30), 30);
        MeetingRoom meetingRoom = new MeetingRoom(2L, "MR Test2", null);
        Reservation existingReservation = new Reservation(
                1L, null, meetingRoom, "Subject Test", reservationStart, 60);


        when(meetingRoomRepository.findByIdWithLock(2L)).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findByMeetingRoom(meetingRoom)).thenReturn(List.of(existingReservation));

        //when
        boolean result = service.saveReservation(reservationCommand);

        //then
        assertFalse(result, "Should return false, reservation time conflict");
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void saveReservation_shouldThrowRoomNotFound() {
        //given
        var reservationCommand = new reservations.model.command.ReservationCommand(
                3L, 3L, "Meeting test2", LocalDateTime.now().plusDays(1), 30);

        when(meetingRoomRepository.findByIdWithLock(3L)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> service.saveReservation(reservationCommand)).isInstanceOf(EntityNotFoundException.class);
    }

}
