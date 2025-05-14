package reservations.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reservations.model.MeetingRoom;
import reservations.model.Reservation;
import reservations.model.command.ReservationCommand;
import reservations.model.dto.ReservationDto;
import reservations.repository.MeetingRoomRepository;
import reservations.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ReservationService {
    MeetingRoomRepository meetingRoomRepository;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    public List<ReservationDto> filterAndSort(Specification<Reservation> spec, Sort sort) {
        return reservationRepository.findAll(spec, sort).stream()
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .collect(Collectors.toList());
    }

    public boolean saveReservation(ReservationCommand reservationCommand) {
        MeetingRoom meetingRoom = meetingRoomRepository.findByIdWithLock(reservationCommand.getMeetingRoomId()).orElseThrow(() ->
                new EntityNotFoundException("Meeting room with ID " + reservationCommand.getMeetingRoomId() + " not found"));
        List<Reservation> reservations = reservationRepository.findByMeetingRoom(meetingRoom);
        LocalDateTime start = reservationCommand.getStartDateTime();
        LocalDateTime end = start.plusMinutes(reservationCommand.getDurationMin());

        if (reservationConflict(reservations,start,end)) {
            return false;
        }

        Reservation newReservation=modelMapper.map(reservationCommand, Reservation.class);
        reservationRepository.save(newReservation);
        return true;
    }

    private boolean reservationConflict(List<Reservation> reservations, LocalDateTime start, LocalDateTime end) {
        return reservations.stream().anyMatch(r ->
                r.getStartDateTime().isBefore(end) &&
                        r.getStartDateTime().plusMinutes(r.getDurationMin()).isAfter(start));
    }


}
