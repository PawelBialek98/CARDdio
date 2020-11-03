package pl.lodz.pl.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.dto.AssignWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.exception.OrderInterfereException;
import pl.lodz.pl.it.cardio.repositories.*;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
        return workOrderRepository.findAllByWorker_User_Email(authentication.getName());
    }

    @Override
    public Collection<String> getAllWorkOrderTypeNames(){
        return workOrderTypeRepository.findAll().stream()
                .map(WorkOrderType::getName).collect(Collectors.toList());
    }

    @Override
    public void addWorkOrder(NewWorkOrderDto newWorkOrderDto) throws AppBaseException {
        WorkOrder wo = new WorkOrder();

        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.parse(newWorkOrderDto.getStartDate()), LocalTime.parse(newWorkOrderDto.getStartTime()));
        wo.setStartDateTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        try{
            wo.setWorker(employeeRepository.findByUser_Email(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(AppNotFoundException::createUserNotFoundException));
        } catch (AppNotFoundException e){
            Logger.getGlobal().log(Level.INFO, "The currently logged in user was not found");
        }

        wo.setWorkOrderType(workOrderTypeRepository.findByCode(newWorkOrderDto.getWorkOrderTypeCode()));
        wo.setEndDateTime(Date.from(localDateTime.plusMinutes(wo.getWorkOrderType().getRequiredTime()).atZone(ZoneId.systemDefault()).toInstant()));
        wo.setCurrentStatus(statusRepository.findByCode("WAITING"));
        //ZAMIENIC DATE NA LOCALDATE?
        if(workOrderRepository.countInterfere(wo.getStartDateTime(),
                wo.getEndDateTime(),
                wo.getCurrentStatus().getCode(),
                wo.getWorker().getUser().getBusinessKey()) > 0){
            throw OrderInterfereException.createOrderInterfereException();
        }
        if(localDateTime.getHour() < 8 || localDateTime.getHour() > 16){
            //throw OrderWorkingHourException
        }
        workOrderRepository.save(wo);
    }

    @Override
    public Collection<WorkOrder> getAllUnAssignedWorkOrders(){
        //return workOrderRepository.findAllByCustomerIsNullAndStartDateGreaterThanEqual(new Date());
        return workOrderRepository.findAllUnassignedWorkOrders();
        //return ObjectMapper.mapAll(workOrderRepository.findAllByCustomerIsNullAndStartDateTimeGreaterThanEqual(new Date()), AssignWorkOrderDto.class);
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
    public Collection<WorkOrder> getAllWorkOrders() {
        return workOrderRepository.findAll();
    }
}
