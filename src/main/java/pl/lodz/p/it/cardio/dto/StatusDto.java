package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class StatusDto {

    private String name;

    private String code;

    private String colour;

    private String statusType;

    private Collection<WorkOrderFlowDto> workOrderFlow;

}
