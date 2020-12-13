package pl.lodz.p.it.cardio.service;

import pl.lodz.p.it.cardio.dto.WorkOrderTypeDto;
import pl.lodz.p.it.cardio.entities.WorkOrderType;

import java.util.Collection;

public interface WorkOrderTypeService {
    Collection<WorkOrderType> findAll();
    Collection<WorkOrderTypeDto> getAllMyWorkOrderType();
    Collection<WorkOrderType> findAllByCodes(Collection<String> code);
}
