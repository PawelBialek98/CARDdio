package pl.lodz.pl.it.cardio.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class SimpleStatusDto {

    private String name;

    private String code;

    private String colour;

    private String statusType;
}
