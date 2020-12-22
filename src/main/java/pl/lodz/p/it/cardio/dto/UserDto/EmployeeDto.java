package pl.lodz.p.it.cardio.dto.UserDto;

import lombok.Data;
import pl.lodz.p.it.cardio.dto.WorkOrderTypeDto;

import java.util.Collection;
import java.util.Date;

@Data
public class EmployeeDto {

    private Date birth;

    private UserDto user;

    private Collection<WorkOrderTypeDto> workOrderTypes;
}
