package pl.lodz.pl.it.cardio.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.Status;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.entities.WorkOrderFlow;
import pl.lodz.pl.it.cardio.repositories.StatusRepository;
import pl.lodz.pl.it.cardio.repositories.WorkOrderFlowRepository;
import pl.lodz.pl.it.cardio.repositories.WorkOrderRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Transactional
@RequiredArgsConstructor
@Controller
@Service
public class WorkOrderScheduler {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderFlowRepository workOrderFlowRepository;

    @Scheduled(cron = "${cron.changeStatus}", zone = "Europe/Warsaw")
    public void changeWorkOrderStatus(){
        workOrderFlowRepository.findAllByCanBeScheduledIsTrue().stream().filter(WorkOrderFlow::getCanBeScheduled)
                .forEach(workOrderFlow -> changeStatus(workOrderFlow.getStatusFrom(), workOrderFlow.getStatusTo()));

    }

    private void changeStatus(Status statusFrom, Status statusTo){
        workOrderRepository.findAllByCurrentStatus_Code(statusFrom.getCode())
                .stream()
                .filter(wo -> wo.getCurrentStatus().getStatusType().equals("BEFORE"))
                .filter(wo -> wo.getStartDateTime().getTime() - new Date().getTime() < 0)
                .forEach(workOrder -> workOrder.setCurrentStatus(statusTo));

        workOrderRepository.findAllByCurrentStatus_Code(statusFrom.getCode())
                .stream()
                .filter(wo -> wo.getCurrentStatus().getStatusType().equals("DURING"))
                .filter(wo -> wo.getStartDateTime().getTime() + wo.getWorkOrderType().getRequiredTime() * 60 * 1000 - new Date().getTime() < 0)
                .forEach(workOrder -> workOrder.setCurrentStatus(statusTo));
    }
}
