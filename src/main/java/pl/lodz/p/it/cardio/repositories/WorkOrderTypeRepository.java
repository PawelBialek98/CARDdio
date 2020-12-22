package pl.lodz.p.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.cardio.entities.WorkOrderType;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface WorkOrderTypeRepository extends JpaRepository<WorkOrderType, Long> {
    Collection<WorkOrderType> findAllByEmployees_User_Email(String email);
    WorkOrderType findByCode(String code);
    Collection<WorkOrderType> findAllByCodeIn(Collection<String> code);
    Collection<WorkOrderType> findAllByActiveIsTrueAndCodeIn(Collection<String> code);
    Optional<WorkOrderType> findByBusinessKey(UUID businessKey);
    Collection<WorkOrderType> findAllByActiveIsTrue();
    boolean existsByCode(String code);
}
