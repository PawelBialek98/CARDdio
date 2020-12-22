package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class SimpleStatusDto {
    @NotNull
    private String name;
    @NotNull
    private String code;
    @NotNull
    private String colour;
    @NotNull
    private String statusType;
}
