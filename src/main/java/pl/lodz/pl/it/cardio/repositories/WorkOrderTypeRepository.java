package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;

import javax.transaction.Transactional;

@Transactional
public interface WorkOrderTypeRepository extends JpaRepository<WorkOrderType, Long> {
}
