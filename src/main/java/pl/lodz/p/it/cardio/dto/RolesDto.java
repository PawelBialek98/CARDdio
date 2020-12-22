package pl.lodz.p.it.cardio.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
public class RolesDto {
    @NotNull
    private String name;
    @NotNull
    private String code;
    @NotNull
    private String businessKey;
}
