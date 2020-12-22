package pl.lodz.p.it.cardio.dto.WorkOrderDto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewWorkOrderDto {

    @NotNull
    private String startDate;

    @NotNull
    private String startTime;

    @NotNull
    private String workOrderTypeCode;

    private String employeeCode;
}
