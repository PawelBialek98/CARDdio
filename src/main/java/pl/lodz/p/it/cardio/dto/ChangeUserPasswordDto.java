package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class ChangeUserPasswordDto {

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message = "{validation.password}")
    private String password;
    @Pattern(regexp = "^[^\\s\\\\@]+@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.){1,11}[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?$",
            message = "{validation.email}")
    @Email
    private String email;
}
