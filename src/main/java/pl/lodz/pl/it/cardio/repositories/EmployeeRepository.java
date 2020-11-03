package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUser_Email(String email);
    Optional<Employee> findByUser_BusinessKey(UUID userBusinessKey);
    boolean existsByUser_BusinessKey(UUID userBusinessKey);
}
