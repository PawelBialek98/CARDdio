package pl.lodz.p.it.cardio.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.cardio.entities.Status;
import pl.lodz.p.it.cardio.entities.WorkOrder;
import pl.lodz.p.it.cardio.repositories.WorkOrderFlowRepository;
import pl.lodz.p.it.cardio.repositories.WorkOrderRepository;

import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkOrderScheduler {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderFlowRepository workOrderFlowRepository;

    @Scheduled(cron = "${cron.changeStatus}", zone = "Europe/Warsaw")
    public void changeWorkOrderStatus(){
        Logger.getGlobal().log(Level.INFO, "Change Work Order Status script - started");

        workOrderFlowRepository.findAllByCanBeScheduledIsTrue()
                .forEach(workOrderFlow -> changeStatus(workOrderFlow.getStatusFrom(), workOrderFlow.getStatusTo()));

        Logger.getGlobal().log(Level.INFO, "Change Work Order Status script - finished");

    }

    //TODO REFActoring
    private void changeStatus(Status statusFrom, Status statusTo){
        Collection<WorkOrder> workOrdersBefore = workOrderRepository.findAllByCurrentStatus_CodeAndCurrentStatus_StatusType(statusFrom.getCode(), "BEFORE");
        workOrdersBefore = workOrdersBefore.stream().filter(wo -> wo.getStartDateTime().getTime() - new Date().getTime() < 0).collect(Collectors.toList());
        workOrdersBefore.forEach(workOrder -> workOrder.setCurrentStatus(statusTo));
        workOrderRepository.saveAll(workOrdersBefore);


        Collection<WorkOrder> workOrdersDuring = workOrderRepository.findAllByCurrentStatus_CodeAndCurrentStatus_StatusType(statusFrom.getCode(), "DURING");
        workOrdersDuring = workOrdersDuring.stream().filter(wo -> wo.getEndDateTime().getTime() - new Date().getTime() < 0).collect(Collectors.toList());
        workOrdersDuring.forEach(workOrder -> workOrder.setCurrentStatus(statusTo));
        workOrderRepository.saveAll(workOrdersDuring);
    }

    @Scheduled(cron = "${cron.removeCancalled}", zone = "Europe/Warsaw")
    public void removeCancalledWorkOrders(){

        Logger.getGlobal().log(Level.INFO, "Remove Cancelled Work Orders script - started");

        Collection<WorkOrder> workOrders = workOrderRepository.findAllByCurrentStatus_Code("CANCELLED");

        int oldTime = 1000 * 60 * 60 * 24 * 7;
        workOrders = workOrders.stream()
                .filter(wo -> new Date().getTime() - wo.getStartDateTime().getTime() > oldTime)
                .collect(Collectors.toList());

        workOrderRepository.deleteAll(workOrders);

        Logger.getGlobal().log(Level.INFO, "Remove Cancelled Work Orders script - finished");
    }
}
