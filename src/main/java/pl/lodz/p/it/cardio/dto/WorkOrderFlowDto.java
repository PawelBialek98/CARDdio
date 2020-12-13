package pl.lodz.p.it.cardio.dto;

import lombok.Data;

@Data
public class WorkOrderFlowDto {

    private SimpleStatusDto statusFrom;

    private SimpleStatusDto statusTo;

    private Boolean canBeScheduled;
}
