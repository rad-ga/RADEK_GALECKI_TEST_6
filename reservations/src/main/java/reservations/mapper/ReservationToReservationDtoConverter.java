package reservations.mapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import reservations.model.Reservation;
import reservations.model.dto.ReservationDto;

@Component
public class ReservationToReservationDtoConverter implements Converter<Reservation, ReservationDto> {
    @Override
    public ReservationDto convert(MappingContext<Reservation, ReservationDto> mappingContext) {

        Reservation source = mappingContext.getSource();
        ReservationDto dto = new ReservationDto();
        dto.setId(source.getId());
        dto.setEmployeeFullName(source.getEmployee().getLastName() + " " + source.getEmployee().getFirstName());
        dto.setMeetingRoom(source.getMeetingRoom().getName());
        dto.setSubject(source.getSubject());
        dto.setStartDateTime(source.getStartDateTime());
        dto.setDurationMin(source.getDurationMin());

        return dto;
    }
}
