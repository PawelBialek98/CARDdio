package pl.lodz.pl.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.dto.WorkOrderTypeDto;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;
import pl.lodz.pl.it.cardio.repositories.WorkOrderTypeRepository;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkOrderTypeServiceImpl implements WorkOrderTypeService {

    private final WorkOrderTypeRepository workOrderTypeRepository;

    @Override
    public Collection<WorkOrderType> findAll() {
        return workOrderTypeRepository.findAll();
    }

    @Override
    public Collection<WorkOrderTypeDto> getAllMyWorkOrderType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectMapper.mapAll(workOrderTypeRepository.findAllByEmployees_User_Email(authentication.getName()),WorkOrderTypeDto.class);
    }

    @Override
    public Collection<WorkOrderType> findAllByCodes(Collection<String> code) {
        return workOrderTypeRepository.findAllByCodeIn(code);
    }
}
