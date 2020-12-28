package pl.lodz.p.it.cardio.service;

import pl.lodz.p.it.cardio.dto.WorkOrderDto.AssignWorkOrderDto;
import pl.lodz.p.it.cardio.dto.WorkOrderDto.NewWorkOrderDto;
import pl.lodz.p.it.cardio.dto.WorkOrderDto.WorkOrderDto;
import pl.lodz.p.it.cardio.entities.WorkOrder;
import pl.lodz.p.it.cardio.exception.*;

import java.util.Collection;
import java.util.UUID;

public interface WorkOrderService {
    Collection<WorkOrderDto> getAllWorkOrdersForClient();

    Collection<WorkOrder> getAllWorkOrdersForEmployee();

    Collection<String> getAllWorkOrderTypeNames();

    void addWorkOrder(NewWorkOrderDto newWorkOrderDto) throws AppBaseException;

    Collection<AssignWorkOrderDto> getAllUnAssignedWorkOrders();

    WorkOrderDto getWorkOrderByBusinessKey(UUID workOrderBusinessKey) throws AppNotFoundException;

    void assignUserToWorkOrder(UUID orderBusinessKey) throws AppNotFoundException, AppTransactionFailureException, EmailException;

    void unassignUserFromWorkOrder(UUID orderBusinessKey) throws AppNotFoundException, TooLateCancellationException, AppTransactionFailureException, EmailException;

    void changeStatus(UUID fromString, String statusCode) throws AppNotFoundException, AppTransactionFailureException, EmailException;

    Collection<WorkOrderDto> getAllWorkOrders();

    int countAllFinishedWorkOrders();
}
