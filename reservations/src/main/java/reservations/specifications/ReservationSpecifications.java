package reservations.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import reservations.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationSpecifications {

    public static Specification<Reservation> filterBy(String subject, Long employeeId, Long meetingRoomId, String dateFrom, String dateTo) {

        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (StringUtils.hasText(subject)) {
                p = cb.and(p, cb.like(cb.lower(root.get("subject")), "%" + subject.toLowerCase() + "%"));
            }

            if (employeeId != null) {
                p = cb.and(p, cb.equal(root.get("employee").get("id"), employeeId));
            }

            if (meetingRoomId != null) {
                p = cb.and(p, cb.equal(root.get("meetingRoom").get("id"), meetingRoomId));
            }

            if (StringUtils.hasText(dateFrom)) {
                try {
                    LocalDateTime from = LocalDate.parse(dateFrom).atStartOfDay();
                    p = cb.and(p, cb.greaterThanOrEqualTo(root.get("startDateTime"), from));
                } catch (Exception ignored) {
                }
            }

            if (StringUtils.hasText(dateTo)) {
                try {
                    LocalDateTime to = LocalDate.parse(dateTo).atTime(LocalTime.MAX);
                    p = cb.and(p, cb.lessThanOrEqualTo(root.get("startDateTime"), to));
                } catch (Exception ignored) {
                }
            }

            return p;
        };
    }
}
