package pl.lodz.pl.it.cardio.dto;

import lombok.Data;
import lombok.Getter;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.WorkOrder;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
public class AssignWorkOrderDto {

    @NotNull
    private String id;

    @FutureOrPresent
    private Date startDateTime;

    @NotNull
    private WorkOrderTypeDto workOrderType;

    @NotNull
    private EmployeeDto employee;

    @NotNull
    private UUID businessKey;
}