package pl.lodz.pl.it.cardio.dto;

import lombok.Data;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;

import javax.validation.constraints.FutureOrPresent;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;

@Data
public class WorkOrderDto {

    @FutureOrPresent
    private Timestamp startDate;

    private Timestamp endDate;

    private String description;

    private WorkOrderType workOrderType;

}
