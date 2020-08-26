package pl.lodz.pl.it.cardio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.repositories.WorkOrderRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;

    @Autowired
    public WorkOrderService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    public Collection<WorkOrder> getAllWorkOrdersForClient(int clientId) {
        return workOrderRepository.findAllByCustomerId(clientId);
    }

    public Collection<WorkOrder> getAllWorkOrdersForEmployee(int employeeId) {
        return workOrderRepository.findAllByWorkerId(employeeId);
    }
}
