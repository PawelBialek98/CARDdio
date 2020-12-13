package pl.lodz.p.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.cardio.entities.Employee;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUser_Email(String email);
    Optional<Employee> findByUser_BusinessKey(UUID userBusinessKey);
    boolean existsByUser_BusinessKey(UUID userBusinessKey);
    int countAllByUser_LockedIsFalse();
}
