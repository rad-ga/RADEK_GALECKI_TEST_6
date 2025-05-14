package reservations.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reservations.mapper.ReservationCommandToReservationConverter;
import reservations.mapper.ReservationToReservationDtoConverter;
import reservations.model.Reservation;
import reservations.model.command.ReservationCommand;
import reservations.model.dto.ReservationDto;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(
            ReservationToReservationDtoConverter toReservationDtoConverter,
            ReservationCommandToReservationConverter toReservationConverter) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(toReservationDtoConverter, Reservation.class, ReservationDto.class);
        modelMapper.addConverter(toReservationConverter, ReservationCommand.class, Reservation.class);
        return modelMapper;
    }

}
