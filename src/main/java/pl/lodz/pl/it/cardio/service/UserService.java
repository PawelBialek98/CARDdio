package pl.lodz.pl.it.cardio.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import pl.lodz.pl.it.cardio.dto.ChangeUserPasswordDto;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.exception.AppTransactionFailureException;
import pl.lodz.pl.it.cardio.exception.ValueNotUniqueException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

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

    void editUser(User user) throws AppNotFoundException, AppTransactionFailureException;

    void adminEditUser(User user) throws AppBaseException;

    User getUser(UUID userBusinessKey) throws AppNotFoundException;

    Employee getEmployee(UUID employeeBusinessKey) throws AppBaseException;

    boolean isEmployee(UUID userBusinessKey);

    void adminEditEmployee(Employee employeeState);

    int countAllActiveUsers();

    int countAllEmployees();
}
