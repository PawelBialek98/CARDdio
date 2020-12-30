package pl.lodz.p.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.cardio.dto.WorkOrderTypeDto;
import pl.lodz.p.it.cardio.exception.AppNotFoundException;
import pl.lodz.p.it.cardio.exception.AppTransactionFailureException;
import pl.lodz.p.it.cardio.exception.ValueNotUniqueException;
import pl.lodz.p.it.cardio.repositories.WorkOrderTypeRepository;
import pl.lodz.p.it.cardio.utils.ObjectMapper;
import pl.lodz.p.it.cardio.entities.WorkOrderType;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Service
@Transactional(Transactional.TxType.REQUIRES_NEW)
@RequiredArgsConstructor
public class WorkOrderTypeServiceImpl implements WorkOrderTypeService {

    private final WorkOrderTypeRepository workOrderTypeRepository;

    @Override
    public Collection<WorkOrderTypeDto> findAll() {
        return ObjectMapper.mapAll(workOrderTypeRepository.findAll(),WorkOrderTypeDto.class);
    }

    @Override
    public Collection<WorkOrderTypeDto> getAllMyWorkOrderType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectMapper.mapAll(workOrderTypeRepository.findAllByEmployees_User_Email(authentication.getName()),WorkOrderTypeDto.class);
    }

    @Override
    public void changeActivity(UUID orderTypeBusinessKey) throws AppNotFoundException, AppTransactionFailureException {
        WorkOrderType wot = workOrderTypeRepository.findByBusinessKey(orderTypeBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderTypeNotFoundException);
        wot.setActive(!wot.isActive());
        try {
            workOrderTypeRepository.saveAndFlush(wot);
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
    }

    @Override
    public void addWorkOrderType(WorkOrderTypeDto workOrderTypeDto) throws ValueNotUniqueException, AppTransactionFailureException {
        WorkOrderType workOrderType = new WorkOrderType();
        workOrderType.setCode(workOrderTypeDto.getCode());
        workOrderType.setName(workOrderTypeDto.getName());
        workOrderType.setRequiredTime(workOrderTypeDto.getRequiredTime());
        if(workOrderTypeRepository.existsByCode(workOrderType.getCode())){
            throw ValueNotUniqueException.createWorkOrderTypeCodeNotUniqueException(workOrderType);
        }
        try{
            workOrderTypeRepository.save(workOrderType);
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
    }
}
