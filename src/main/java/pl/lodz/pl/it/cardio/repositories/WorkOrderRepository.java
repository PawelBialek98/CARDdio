package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.pl.it.cardio.entities.WorkOrder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Query("SELECT count(wo) " +
            "FROM WorkOrder wo " +
            "WHERE wo.startDateTime < :endDate " +
            "AND wo.endDateTime > :startDate " +
            "AND wo.currentStatus.code = :currentStatusCode " +
            "AND wo.worker.user.businessKey = :employeeBusinessKey")
    int countInterfere(Date startDate, Date endDate, String currentStatusCode, UUID employeeBusinessKey);
    @Query("SELECT wo FROM WorkOrder wo WHERE wo.currentStatus.code = 'WAITING'")
    Collection<WorkOrder> findAllUnassignedWorkOrders();
}
