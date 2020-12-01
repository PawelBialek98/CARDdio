package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.dto.AssignWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.WorkOrderDto;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.exception.AppTransactionFailureException;
import pl.lodz.pl.it.cardio.exception.TooLateCancellationException;

import java.util.Collection;
import java.util.UUID;

public interface WorkOrderService {
    Collection<WorkOrderDto> getAllWorkOrdersForClient();

    Collection<WorkOrder> getAllWorkOrdersForEmployee();

    Collection<String> getAllWorkOrderTypeNames();

    void addWorkOrder(NewWorkOrderDto newWorkOrderDto) throws AppBaseException;

    Collection<AssignWorkOrderDto> getAllUnAssignedWorkOrders();

    WorkOrder getWorkOrderByBusinessKey(UUID workOrderBusinessKey) throws AppNotFoundException;

    void assignUserToWorkOrder(UUID orderBusinessKey) throws AppNotFoundException, AppTransactionFailureException;

    void unassignUserFromWorkOrder(UUID orderBusinessKey) throws AppNotFoundException, TooLateCancellationException, AppTransactionFailureException;

    void changeStatus(UUID fromString, String statusCode) throws AppNotFoundException, AppTransactionFailureException;

    Collection<WorkOrderDto> getAllWorkOrders();

    int countAllFinishedWorkOrders();
}
