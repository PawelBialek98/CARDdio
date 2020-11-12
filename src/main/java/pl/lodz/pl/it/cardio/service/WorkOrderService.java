package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.dto.AssignWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;

import java.util.Collection;
import java.util.UUID;

public interface WorkOrderService {
    Collection<WorkOrder> getAllWorkOrdersForClient();

    Collection<WorkOrder> getAllWorkOrdersForEmployee();

    Collection<String> getAllWorkOrderTypeNames();

    void addWorkOrder(NewWorkOrderDto newWorkOrderDto) throws AppBaseException;

    Collection<WorkOrder> getAllUnAssignedWorkOrders();

    WorkOrder getWorkOrderByBusinessKey(UUID workOrderBusinessKey) throws AppNotFoundException;

    void assignUserToWorkOrder(UUID orderBusinessKey) throws AppNotFoundException;

    void unassignUserFromWorkOrder(UUID orderBusinessKey) throws AppNotFoundException;

    void changeStatus(UUID fromString, String statusCode) throws AppNotFoundException;

    Collection<WorkOrder> getAllWorkOrders();

    int countAllFinishedWorkOrders();
}
