package pl.lodz.p.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.cardio.dto.WorkOrderDto.AssignWorkOrderDto;
import pl.lodz.p.it.cardio.dto.WorkOrderDto.NewWorkOrderDto;
import pl.lodz.p.it.cardio.dto.WorkOrderDto.WorkOrderDto;
import pl.lodz.p.it.cardio.entities.Status;
import pl.lodz.p.it.cardio.events.statusChange.OrderStatusChangeEvent;
import pl.lodz.p.it.cardio.exception.*;
import pl.lodz.p.it.cardio.repositories.*;
import pl.lodz.p.it.cardio.utils.CustomMailSender;
import pl.lodz.p.it.cardio.utils.ObjectMapper;
import pl.lodz.p.it.cardio.entities.WorkOrder;
import pl.lodz.p.it.cardio.entities.WorkOrderType;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
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
    private final CustomMailSender mailSender;

    @Override
    public Collection<WorkOrderDto> getAllWorkOrdersForClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectMapper.mapAll(workOrderRepository.findAllByCustomer_Email(authentication.getName()),WorkOrderDto.class);
    }

    @Override
    public Collection<WorkOrder> getAllWorkOrdersForEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return workOrderRepository.findAllByEmployee_User_Email(authentication.getName());
    }

    @Override
    public Collection<String> getAllWorkOrderTypeNames(){
        return workOrderTypeRepository.findAll().stream()
                .map(WorkOrderType::getName).collect(Collectors.toList());
    }

    @Override
    //TODO ogarnąć wyjątki tutaj
    public void addWorkOrder(NewWorkOrderDto newWorkOrderDto) throws AppBaseException {
        WorkOrder wo = new WorkOrder();

        LocalDateTime localDateTime = null;
        try{
            localDateTime = LocalDateTime.of(LocalDate.parse(newWorkOrderDto.getStartDate()), LocalTime.parse(newWorkOrderDto.getStartTime()));
            wo.setStartDateTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        } catch (DateTimeParseException e){

        }

        wo.setStartDateTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        try{
            wo.setEmployee(employeeRepository.findByUser_Email(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(AppNotFoundException::createUserNotFoundException));
        } catch (AppNotFoundException e){
            Logger.getGlobal().log(Level.INFO, "The currently logged in user was not found");
        }

        wo.setWorkOrderType(workOrderTypeRepository.findByCode(newWorkOrderDto.getWorkOrderTypeCode()));
        wo.setEndDateTime(Date.from(localDateTime.plusMinutes(wo.getWorkOrderType().getRequiredTime()).atZone(ZoneId.systemDefault()).toInstant()));
        wo.setCurrentStatus(statusRepository.findByCode("WAITING").orElseThrow(AppNotFoundException::createStatusNotFoundException));
        //ZAMIENIC DATE NA LOCALDATE?
        if(workOrderRepository.countInterfere(wo.getStartDateTime(),
                wo.getEndDateTime(),
                wo.getCurrentStatus().getCode(),
                wo.getEmployee().getUser().getBusinessKey()) > 0){
            throw OrderInterfereException.createOrderInterfereException();
        }
        if(localDateTime.getHour() < 8 || localDateTime.getHour() > 16){
            //throw OrderWorkingHourException
        }
        workOrderRepository.save(wo);
    }

    @Override
    public Collection<AssignWorkOrderDto> getAllUnAssignedWorkOrders(){
        return ObjectMapper.mapAll(workOrderRepository.findAllUnassignedWorkOrders(new Date()), AssignWorkOrderDto.class);
    }

    @Override
    public WorkOrderDto getWorkOrderByBusinessKey(UUID workOrderBusinessKey) throws AppNotFoundException {
        return ObjectMapper.map(workOrderRepository.findByBusinessKey(workOrderBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderNotFoundException),WorkOrderDto.class);
    }

    @Override
    public void assignUserToWorkOrder(UUID orderBusinessKey) throws AppNotFoundException, AppTransactionFailureException, EmailException {
        WorkOrder workOrder = workOrderRepository.findByBusinessKeyAndCustomerIsNull(orderBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderNotFoundException);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        workOrder.setCustomer(userRepository.findByEmailAndLockedIsFalse(authentication.getName())
                .orElseThrow(AppNotFoundException::createUserNotFoundException));
        workOrder.setCurrentStatus(statusRepository.findByCode("ASSIGNED").orElseThrow(AppNotFoundException::createStatusNotFoundException));
        try {
            workOrderRepository.save(workOrder);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
        mailSender.orderStatusChange(workOrder,
                LocaleContextHolder.getLocale(), "assigned");
    }

    @Override
    public void unassignUserFromWorkOrder(UUID orderBusinessKey) throws AppNotFoundException, TooLateCancellationException, AppTransactionFailureException, EmailException {
        WorkOrder workOrder = workOrderRepository.findByBusinessKey(orderBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderNotFoundException);
        if (workOrder.canBeCancelled()) {

            workOrder.setCustomer(null);
            workOrder.setCurrentStatus(statusRepository.findByCode("WAITING").orElseThrow(AppNotFoundException::createStatusNotFoundException));
            try {
                workOrderRepository.save(workOrder);
            } catch (ObjectOptimisticLockingFailureException e) {
                throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
            }
            mailSender.orderStatusChange(workOrder,
                    LocaleContextHolder.getLocale(), "unassigned");
        } else {
            throw TooLateCancellationException.createTooLateCancellationException(workOrder);
        }
    }

    @Override
    public void changeStatus(UUID orderBusinessKey, String statusCode) throws AppNotFoundException, AppTransactionFailureException, EmailException {
        WorkOrder workOrder = workOrderRepository.findByBusinessKey(orderBusinessKey).orElseThrow(AppNotFoundException::createWorkOrderNotFoundException);
        Status status = statusRepository.findByCode(statusCode).orElseThrow(AppNotFoundException::createStatusNotFoundException);
        workOrder.setCurrentStatus(status);
        try {
            workOrderRepository.save(workOrder);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
        mailSender.orderStatusChange(workOrder,
                LocaleContextHolder.getLocale(), "statusChanged");
    }

    @Override
    public Collection<WorkOrderDto> getAllWorkOrders() {
        return ObjectMapper.mapAll(workOrderRepository.findAll(),WorkOrderDto.class);
    }

    @Override
    public int countAllFinishedWorkOrders() {
        return workOrderRepository.countAllFinished();
    }
}
