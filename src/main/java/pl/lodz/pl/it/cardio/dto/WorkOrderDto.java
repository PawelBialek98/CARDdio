package pl.lodz.pl.it.cardio.dto;

import lombok.Data;
import pl.lodz.pl.it.cardio.entities.Status;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;

@Data
public class WorkOrderDto {

    private int id;

    private String businessKey;

    @NotNull
    private Date startDate;

    private Time startTime;

    private StatusDto currentStatus;

    private WorkOrderTypeDto workOrderType;

    public boolean isFinalStatus(){
        return currentStatus.getCode().equals("FINISHED");
    }

    public boolean canBeCancalled(){
        return !currentStatus.getStatusType().equals("AFTER");
    }
}
