package reservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reservations.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
