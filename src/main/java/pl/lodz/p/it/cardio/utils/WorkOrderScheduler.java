package pl.lodz.p.it.cardio.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.cardio.entities.Status;
import pl.lodz.p.it.cardio.entities.WorkOrder;
import pl.lodz.p.it.cardio.exception.AppTransactionFailureException;
import pl.lodz.p.it.cardio.repositories.WorkOrderFlowRepository;
import pl.lodz.p.it.cardio.repositories.WorkOrderRepository;
import pl.lodz.p.it.cardio.service.WorkOrderService;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkOrderScheduler {

    private final WorkOrderService workOrderService;
    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderFlowRepository workOrderFlowRepository;

    @Scheduled(cron = "${cron.changeStatus}", zone = "Europe/Warsaw")
    public void changeWorkOrderStatus() {
        Logger.getGlobal().log(Level.INFO, "Change Work Order Status script - started");

        try {
            workOrderService.changeWorkOrderStatus();
        } catch (AppTransactionFailureException e) {
            Logger.getGlobal().log(Level.INFO, "Remove inactivated accounts script - failed!\nMessage:\n" + e.getMessage());
        }
        Logger.getGlobal().log(Level.INFO, "Change Work Order Status script - finished");

    }

    @Scheduled(cron = "${cron.removeCancalled}", zone = "Europe/Warsaw")
    public void removeCancelledWorkOrders() {

        Logger.getGlobal().log(Level.INFO, "Remove Cancelled Work Orders script - started");

        workOrderService.removeCancelledWorkOrders();

        Logger.getGlobal().log(Level.INFO, "Remove Cancelled Work Orders script - finished");
    }
}
