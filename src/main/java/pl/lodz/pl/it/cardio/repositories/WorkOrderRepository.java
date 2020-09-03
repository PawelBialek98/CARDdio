package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.WorkOrder;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Collection<WorkOrder> findAllByCustomer_Email(String email);
    Collection<WorkOrder> findAllByWorkerId(int id);
}
