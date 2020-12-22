package pl.lodz.p.it.cardio.dto;

import lombok.Data;
import pl.lodz.p.it.cardio.utils.FieldMatch.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@FieldMatch(first = "password", second = "passwordConfirm", message = "{validation.passwordMatch}")
public class ChangeUserPasswordDto {

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message = "{validation.password}")
    private String password;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message = "{validation.password}")
    private String passwordConfirm;
    @Email(message = "{validation.email}")
    private String email;
    private String token;
}
