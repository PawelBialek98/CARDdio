package pl.lodz.p.it.cardio.service;

import pl.lodz.p.it.cardio.dto.WorkOrderTypeDto;
import pl.lodz.p.it.cardio.entities.WorkOrderType;
import pl.lodz.p.it.cardio.exception.AppNotFoundException;
import pl.lodz.p.it.cardio.exception.AppTransactionFailureException;
import pl.lodz.p.it.cardio.exception.ValueNotUniqueException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.UUID;

public interface WorkOrderTypeService {
    Collection<WorkOrderTypeDto> findAll();
    Collection<WorkOrderTypeDto> getAllMyWorkOrderType();
    void changeActivity(UUID orderTypeBusinessKey) throws AppNotFoundException, AppTransactionFailureException;

    void addWorkOrderType(WorkOrderTypeDto workOrderTypeDto) throws ValueNotUniqueException, AppTransactionFailureException;
}
