package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.pl.it.cardio.entities.Status;
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
    Collection<WorkOrder> findAllByCustomerIsNullAndStartDateTimeGreaterThanEqual(Date date);
    Optional<WorkOrder> findByBusinessKeyAndCustomerIsNull(UUID workOrderBusinessKey);
    Optional<WorkOrder> findByBusinessKey(UUID workOrderBusinessKey);
    Collection<WorkOrder> findAllByCurrentStatus_Code(String statusCode);
}
