package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.pl.it.cardio.entities.WorkOrderFlow;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
public interface WorkOrderFlowRepository extends JpaRepository<WorkOrderFlow, Long> {
    @Query("select wof from WorkOrderFlow wof where wof.canBeScheduled = true")
    Collection<WorkOrderFlow> findAllByCanBeScheduledIsTrue();

}
