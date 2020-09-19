package pl.lodz.pl.it.cardio.dto;

import lombok.Data;
import pl.lodz.pl.it.cardio.entities.WorkOrderType;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class NewWorkOrderDto {

    @FutureOrPresent
    private Date startDate;

    @NotNull
    private String startTime;

    @NotNull
    private String workOrderTypeCode;
}
