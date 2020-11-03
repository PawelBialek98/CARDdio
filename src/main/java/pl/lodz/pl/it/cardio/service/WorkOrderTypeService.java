package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.entities.Skill;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;

import java.util.Collection;

public interface WorkOrderTypeService {
    Collection<WorkOrderType> findAll();
    Collection<WorkOrderType> getAllMyWorkOrderType();
    Collection<WorkOrderType> findAllByCodes(Collection<String> code);
}
