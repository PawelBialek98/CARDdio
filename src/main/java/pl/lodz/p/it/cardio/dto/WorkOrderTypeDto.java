package pl.lodz.p.it.cardio.dto;

import lombok.Data;

@Data
public class WorkOrderTypeDto {

    private String code;

    private String name;

    private int requiredTime;
}
