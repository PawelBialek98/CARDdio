package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewWorkOrderTypeDto {
    private String code;
    private String name;
    private int requiredTime;
}
