package pl.lodz.pl.it.cardio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
