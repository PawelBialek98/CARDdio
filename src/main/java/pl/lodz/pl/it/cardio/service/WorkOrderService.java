package pl.lodz.pl.it.cardio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;
import pl.lodz.pl.it.cardio.repositories.WorkOrderRepository;
import pl.lodz.pl.it.cardio.repositories.WorkOrderTypeRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderTypeRepository workOrderTypeRepository;

    @Autowired
    public WorkOrderService(WorkOrderRepository workOrderRepository, WorkOrderTypeRepository workOrderTypeRepository) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderTypeRepository = workOrderTypeRepository;
    }

    public Collection<WorkOrder> getAllWorkOrdersForClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Logger.getGlobal().log(Level.INFO, currentPrincipalName);
        return workOrderRepository.findAllByCustomer_Email(authentication.getName());
    }

    public Collection<WorkOrder> getAllWorkOrdersForEmployee(int employeeId) {
        return workOrderRepository.findAllByWorkerId(employeeId);
    }

    public Collection<String> getAllWorkOrderTypeNames(){
        return workOrderTypeRepository.findAll().stream()
                .map(WorkOrderType::getName).collect(Collectors.toList());
    }
}
