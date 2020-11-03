package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
public interface WorkOrderTypeRepository extends JpaRepository<WorkOrderType, Long> {
    Collection<WorkOrderType> findAllByEmployees_User_Email(String email);
    WorkOrderType findByCode(String code);
    Collection<WorkOrderType> findAllByCodeIn(Collection<String> code);
}
