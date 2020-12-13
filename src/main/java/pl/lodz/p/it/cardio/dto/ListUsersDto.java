package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
public class ListUsersDto {

    private int id;
    private UUID businessKey;
    @Email
    private String email;
    @Pattern(regexp = "[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ.-]{2,31}", message = "{validation.firstName}")
    private String firstName;
    @Pattern(regexp = "[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ.-]{2,31}", message = "{validation.lastName}")
    private String lastName;
    @Pattern(regexp = "\\d{9}", message = "{validation.number}")
    private String phoneNumber;

    private Date createDate;

    private Collection<RolesDto> roles;
}
