package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Data
public class EmployeeDto {

    private Date birth;

    private UserDto user;

    private Collection<WorkOrderTypeDto> workOrderTypes;
}
