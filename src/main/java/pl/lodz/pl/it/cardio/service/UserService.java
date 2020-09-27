package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.dto.ChangeUserPasswordDto;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.exception.ValueNotUniqueException;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserService {
    List<User> getAllUsers();

    List<Employee> getAllEmployee();

    void addUser(UserDto userDto) throws AppNotFoundException, ValueNotUniqueException;

    void createVerificationToken(UserDto user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    void saveRegisteredUser(User user);

    UserDto findByEmail(String email) throws AppNotFoundException;

    void setNewPassword(User changeUserPasswordDto) throws AppNotFoundException;

    User getCurrentUser() throws AppNotFoundException;

    void editUser(User user) throws AppNotFoundException;
}
