package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class WorkOrderTypeDto {

    @NotBlank(message = "{validation.orderTypeCode}")
    private String code;
    @NotBlank(message = "{validation.orderTypeName}")
    private String name;
    @Min(value = 0, message = "{validation.requiredTimeMin}")
    @Max(value = 480, message = "{validation.requiredTimeMax}")
    private int requiredTime;
    private String businessKey;
    private boolean active;
}
