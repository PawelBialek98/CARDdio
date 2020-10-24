package pl.lodz.pl.it.cardio.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class EditAdminUserDto {

    @Pattern(regexp = "[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ.-]{2,31}", message = "{validation.firstName}")
    private String firstName;
    @Pattern(regexp = "[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ.-]{2,31}", message = "{validation.lastName}")
    private String lastName;
    @Pattern(regexp = "\\d{9}", message = "{validation.number}")
    private String phoneNumber;

    private List<String> rolesSet = new ArrayList<>();

    /*public List<RolesDto> getRolesSet() {
        return new ArrayList<>(rolesSet);
    }

    public void setRolesSet(List<RolesDto> roles){
        rolesSet = new HashSet<>(roles);
    }*/

    private boolean administrator;
    private boolean client;
    private boolean office;
    private boolean mechanic;
}
