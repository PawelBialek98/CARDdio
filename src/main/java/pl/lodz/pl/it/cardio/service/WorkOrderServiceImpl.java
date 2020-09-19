package pl.lodz.pl.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.dto.AssignWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;
import pl.lodz.pl.it.cardio.repositories.EmployeeRepository;
import pl.lodz.pl.it.cardio.repositories.StatusRepository;
import pl.lodz.pl.it.cardio.repositories.WorkOrderRepository;
import pl.lodz.pl.it.cardio.repositories.WorkOrderTypeRepository;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.transaction.Transactional;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderTypeRepository workOrderTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final StatusRepository statusRepository;

    @Override
    public Collection<WorkOrder> getAllWorkOrdersForClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Logger.getGlobal().log(Level.INFO, currentPrincipalName);
        return workOrderRepository.findAllByCustomer_Email(authentication.getName());
    }

    @Override
    public Collection<WorkOrder> getAllWorkOrdersForEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Logger.getGlobal().log(Level.INFO, currentPrincipalName);
        return workOrderRepository.findAllByWorker_User_Email(authentication.getName());
    }

    @Override
    public Collection<String> getAllWorkOrderTypeNames(){
        return workOrderTypeRepository.findAll().stream()
                .map(WorkOrderType::getName).collect(Collectors.toList());
    }

    @Override
    public void addWorkOrder(NewWorkOrderDto newWorkOrderDto) {
        WorkOrder wo = new WorkOrder();
        wo.setStartDate(newWorkOrderDto.getStartDate());
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            wo.setStartTime(new Time(formatter.parse(newWorkOrderDto.getStartTime()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        wo.setWorker(employeeRepository.findByUser_Email(SecurityContextHolder.getContext().getAuthentication().getName()));
        wo.setWorkOrderType(workOrderTypeRepository.findByCode(newWorkOrderDto.getWorkOrderTypeCode()));
        wo.setCurrentStatus(statusRepository.findByCode("WAITING"));
        //workOrderRepository.save(ObjectMapper.map(newWorkOrderDto, WorkOrder.class));
        workOrderRepository.save(wo);
    }

    @Override
    public Collection<AssignWorkOrderDto> getAllUnAssignedWorkOrders(){
        //return workOrderRepository.findAllByCustomerIsNullAndStartDateGreaterThanEqual(new Date());
        return ObjectMapper.mapAll(workOrderRepository.findAllByCustomerIsNullAndStartDateGreaterThanEqual(new Date()), AssignWorkOrderDto.class);
    }
}
