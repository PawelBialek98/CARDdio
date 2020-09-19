package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.dto.AssignWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.pl.it.cardio.entities.WorkOrder;

import java.util.Collection;

public interface WorkOrderService {
    Collection<WorkOrder> getAllWorkOrdersForClient();

    Collection<WorkOrder> getAllWorkOrdersForEmployee();

    Collection<String> getAllWorkOrderTypeNames();

    void addWorkOrder(NewWorkOrderDto newWorkOrderDto);

    Collection<AssignWorkOrderDto> getAllUnAssignedWorkOrders();
}
