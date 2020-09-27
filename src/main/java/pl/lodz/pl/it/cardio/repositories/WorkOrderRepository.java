package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.WorkOrder;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Collection<WorkOrder> findAllByCustomer_Email(String email);
    Collection<WorkOrder> findAllByWorker_User_Email(String email);
    Collection<WorkOrder> findAllByCustomerIsNullAndStartDateGreaterThanEqual(Date date);
    Optional<WorkOrder> findByBusinessKey(UUID workOrderBusinessKey);
}
