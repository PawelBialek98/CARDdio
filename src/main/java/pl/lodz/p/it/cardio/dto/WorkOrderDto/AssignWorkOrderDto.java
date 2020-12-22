package pl.lodz.p.it.cardio.dto.WorkOrderDto;

import lombok.Data;
import pl.lodz.p.it.cardio.dto.UserDto.EmployeeDto;
import pl.lodz.p.it.cardio.dto.WorkOrderTypeDto;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
public class AssignWorkOrderDto {

    @NotNull
    private String id;

    @NotNull
    private Date startDateTime;

    @NotNull
    private WorkOrderTypeDto workOrderType;

    @NotNull
    private EmployeeDto employee;

    @NotNull
    private UUID businessKey;
}