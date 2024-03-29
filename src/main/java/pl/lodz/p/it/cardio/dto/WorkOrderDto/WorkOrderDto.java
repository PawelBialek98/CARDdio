package pl.lodz.p.it.cardio.dto.WorkOrderDto;

import lombok.Data;
import pl.lodz.p.it.cardio.dto.UserDto.EmployeeDto;
import pl.lodz.p.it.cardio.dto.StatusDto;
import pl.lodz.p.it.cardio.dto.UserDto.UserDto;
import pl.lodz.p.it.cardio.dto.WorkOrderTypeDto;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;

@Data
public class WorkOrderDto {

    private int id;

    private String businessKey;

    @NotNull
    private Date startDate;

    private Date endDate;

    private Time startTime;

    private StatusDto currentStatus;

    private UserDto customer;

    private EmployeeDto employee;

    private WorkOrderTypeDto workOrderType;

    public boolean isFinalStatus(){
        return currentStatus.getCode().equals("FINISHED");
    }

    public boolean canBeCancalled(){
        return !currentStatus.getStatusType().equals("AFTER");
    }
}
