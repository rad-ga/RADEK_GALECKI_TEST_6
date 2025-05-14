package reservations.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import reservations.model.Employee;
import reservations.model.MeetingRoom;
import reservations.model.Reservation;
import reservations.model.command.ReservationCommand;
import reservations.repository.EmployeeRepository;
import reservations.repository.MeetingRoomRepository;

@Component
@AllArgsConstructor
public class ReservationCommandToReservationConverter implements Converter<ReservationCommand, Reservation> {
    private final EmployeeRepository employeeRepository;
    private final MeetingRoomRepository meetingRoomRepository;


    @Override
    public Reservation convert(MappingContext<ReservationCommand, Reservation> mappingContext) {
        ReservationCommand command = mappingContext.getSource();
        Reservation reservation = new Reservation();

        Employee employee = employeeRepository.findById(command.getEmployeeId()).orElseThrow(() ->
                new EntityNotFoundException("Employee " + command.getEmployeeId() + " not found"));

        MeetingRoom meetingRoom = meetingRoomRepository.findById(command.getMeetingRoomId()).orElseThrow(() ->
                new EntityNotFoundException("Meeting room " + command.getMeetingRoomId() + " not found"));

        reservation.setEmployee(employee);
        reservation.setMeetingRoom(meetingRoom);
        reservation.setSubject(command.getSubject());
        reservation.setStartDateTime(command.getStartDateTime());
        reservation.setDurationMin(command.getDurationMin());

        return reservation;
    }
}
