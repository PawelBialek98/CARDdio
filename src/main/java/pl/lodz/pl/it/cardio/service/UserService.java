package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.dto.ChangeUserPasswordDto;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.exception.ValueNotUniqueException;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    List<Employee> getAllEmployee();

    void addUser(UserDto userDto) throws AppNotFoundException, ValueNotUniqueException;

    void createVerificationToken(UserDto user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    void saveRegisteredUser(User user);

    UserDto findByEmail(String email) throws AppNotFoundException;

    void setNewPassword(ChangeUserPasswordDto changeUserPasswordDto) throws AppNotFoundException;
}
