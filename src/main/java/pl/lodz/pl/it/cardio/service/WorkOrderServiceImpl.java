package pl.lodz.pl.it.cardio.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.dto.AssignWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.repositories.*;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.transaction.Transactional;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
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
    private final UserRepository userRepository;

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
        //Logger.getGlobal().log(Level.INFO, currentPrincipalName);
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


        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.parse(newWorkOrderDto.getStartDate()), LocalTime.parse(newWorkOrderDto.getStartTime()));
        wo.setStartDateTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        wo.setWorker(employeeRepository.findByUser_Email(SecurityContextHolder.getContext().getAuthentication().getName()));
        wo.setWorkOrderType(workOrderTypeRepository.findByCode(newWorkOrderDto.getWorkOrderTypeCode()));
        wo.setCurrentStatus(statusRepository.findByCode("WAITING"));
        //workOrderRepository.save(ObjectMapper.map(newWorkOrderDto, WorkOrder.class));
        workOrderRepository.save(wo);
    }

    @Override
    public Collection<AssignWorkOrderDto> getAllUnAssignedWorkOrders(){
        //return workOrderRepository.findAllByCustomerIsNullAndStartDateGreaterThanEqual(new Date());
        return ObjectMapper.mapAll(workOrderRepository.findAllByCustomerIsNullAndStartDateTimeGreaterThanEqual(new Date()), AssignWorkOrderDto.class);
    }

    @Override
    public WorkOrder getWorkOrderByBusinessKey(UUID workOrderBusinessKey) throws AppNotFoundException {
        return workOrderRepository.findByBusinessKeyAndCustomerIsNull(workOrderBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderNotFoundException);
    }

    @Override
    public void assignUserToWorkOrder(UUID orderBusinessKey) throws AppNotFoundException {
        WorkOrder workOrder = workOrderRepository.findByBusinessKeyAndCustomerIsNull(orderBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderNotFoundException);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        workOrder.setCustomer(userRepository.findByEmail(authentication.getName())
                .orElseThrow(AppNotFoundException::createUserNotFoundException));
        workOrderRepository.save(workOrder);
    }

    @Override
    public void unassignUserFromWorkOrder(UUID orderBusinessKey) throws AppNotFoundException {
        WorkOrder workOrder = workOrderRepository.findByBusinessKey(orderBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderNotFoundException);
        if(workOrder.canBeCancelled()){
            Logger.getGlobal().log(Level.INFO, new Date().toString());
            Logger.getGlobal().log(Level.INFO, String.valueOf(workOrder.getStartDateTime().getTime() - new Date().getTime()));
            workOrder.setCustomer(null);
            workOrderRepository.save(workOrder);
        }
    }

    @Override
    public void changeStatus(UUID orderBusinessKey, String statusCode) throws AppNotFoundException {
        WorkOrder workOrder = workOrderRepository.findByBusinessKey(orderBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderNotFoundException);
        workOrder.setCurrentStatus(statusRepository.findByCode(statusCode));
        workOrderRepository.save(workOrder);
    }

    @Override
    public Collection<WorkOrder> getAllWorkOrders() throws AppNotFoundException {
        return workOrderRepository.findAll();
    }
}
