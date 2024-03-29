package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class ResetMailDto {
    @Email(message = "{validation.email}")
    private String email;
}
