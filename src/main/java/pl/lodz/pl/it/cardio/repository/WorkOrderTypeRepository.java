package pl.lodz.pl.it.cardio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;

public interface WorkOrderTypeRepository extends JpaRepository<WorkOrderType, Long> {
}
