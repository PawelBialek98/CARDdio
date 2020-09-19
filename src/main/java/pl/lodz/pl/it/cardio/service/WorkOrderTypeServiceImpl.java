package pl.lodz.pl.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;
import pl.lodz.pl.it.cardio.repositories.WorkOrderTypeRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkOrderTypeServiceImpl implements WorkOrderTypeService {

    private final WorkOrderTypeRepository workOrderTypeRepository;

    @Override
    public Collection<WorkOrderType> getAllMyWorkOrderType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String currentPrincipalName = authentication.getName();
        //Logger.getGlobal().log(Level.INFO, currentPrincipalName);
        return workOrderTypeRepository.findAllByEmployees_User_Email(authentication.getName());
        //return workOrderTypeRepository.findAll();
    }
}
