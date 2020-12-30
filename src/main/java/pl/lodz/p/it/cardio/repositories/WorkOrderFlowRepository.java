package pl.lodz.p.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.p.it.cardio.entities.WorkOrderFlow;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional(Transactional.TxType.MANDATORY)
public interface WorkOrderFlowRepository extends JpaRepository<WorkOrderFlow, Long> {
    @Query("select wof from WorkOrderFlow wof where wof.canBeScheduled = true")
    Collection<WorkOrderFlow> findAllByCanBeScheduledIsTrue();

}
