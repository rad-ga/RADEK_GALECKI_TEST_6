package reservations.controller;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reservations.model.Reservation;
import reservations.model.command.ReservationCommand;
import reservations.model.dto.ReservationDto;
import reservations.repository.EmployeeRepository;
import reservations.repository.MeetingRoomRepository;
import reservations.service.ReservationService;
import reservations.specifications.ReservationSpecifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/reservations")

public class ReservationController {
    private final ReservationService reservationService;
    private final EmployeeRepository employeeRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    @GetMapping
    public String reservationForm(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long meetingRoomId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "startDateTime,asc") String sort,
            Model model
    ) {
        model.addAttribute("reservationCommand", new ReservationCommand());
        populateLists(model);

        Specification<Reservation> spec = ReservationSpecifications.filterBy(
                subject, employeeId, meetingRoomId, dateFrom, dateTo
        );

        String[] sortParams = sort.split(",");
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Sort springSort = Sort.by(order);

        List<ReservationDto> reservations = reservationService.filterAndSort(spec, springSort);

        model.addAttribute("reservations", reservations);
        model.addAttribute("subject", subject);
        model.addAttribute("selectedEmployeeId", employeeId);
        model.addAttribute("selectedRoomId", meetingRoomId);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        model.addAttribute("sort", sort);

        return "reservations";
    }

    @PostMapping
    public String addReservation(
            @Valid @ModelAttribute ReservationCommand reservationCommand, BindingResult bindingResult, RedirectAttributes rdr, Model model) {
        if (bindingResult.hasErrors()) {
            populateLists(model);
            return "reservations";
        }

        boolean reservationSaved = reservationService.saveReservation(reservationCommand);

        if (!reservationSaved) {
            bindingResult.reject("conflict", "The meeting room is reserved for this period.");
            populateLists(model);
            return "reservations";
        }

        rdr.addFlashAttribute("msg", "Reservation successfully added");
        return "redirect:/reservations";
    }

    private void populateLists(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("meetingRooms", meetingRoomRepository.findAll());
    }

}
