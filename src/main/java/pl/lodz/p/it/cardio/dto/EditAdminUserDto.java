package pl.lodz.p.it.cardio.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.*;

@Data
public class EditAdminUserDto {

    @Pattern(regexp = "[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ.-]{2,31}", message = "{validation.firstName}")
    private String firstName;
    @Pattern(regexp = "[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ.-]{2,31}", message = "{validation.lastName}")
    private String lastName;
    @Pattern(regexp = "\\d{9}", message = "{validation.number}")
    private String phoneNumber;

    private Boolean activated;

    private Boolean locked;

    private Map<String, Boolean> rolesMap;

    private String dateBirth;

    private Map<String, Boolean> workOrderTypeMap;

    private Collection<String> workOrderType;
}
