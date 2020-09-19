package pl.lodz.pl.it.cardio.dto;

import lombok.Data;
import pl.lodz.pl.it.cardio.entities.User;

import java.util.Date;

@Data
public class EmployeeDto {

    private Date birth;

    private UserDto user;
}
