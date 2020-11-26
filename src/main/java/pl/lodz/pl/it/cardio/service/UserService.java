package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.dto.ResetMailDto;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Transactional
public interface UserService {
    List<User> getAllUsers();

    List<Employee> getAllEmployee();

    void addUser(UserDto userDto, HttpServletRequest request) throws AppNotFoundException, ValueNotUniqueException;

    void createVerificationToken(UserDto user, String token,  String type);

    void activateAccount(String token) throws AppNotFoundException, TokenExpiredException;

    User findByEmail(String email) throws AppNotFoundException;

    void setNewPassword(User changeUserPasswordDto) throws AppNotFoundException;

    User getCurrentUser() throws AppNotFoundException;

    void editUser(User user) throws AppTransactionFailureException;

    void adminEditUser(User user) throws AppBaseException;

    User getUser(UUID userBusinessKey) throws AppNotFoundException;

    Employee getEmployee(UUID employeeBusinessKey) throws AppBaseException;

    boolean isEmployee(UUID userBusinessKey);

    void adminEditEmployee(Employee employeeState);

    int countAllActiveUsers();

    int countAllEmployees();

    void resetPassword(ResetMailDto userEmail, HttpServletRequest request) throws AppNotFoundException;

    User verifyToken(String token) throws AppNotFoundException, TokenExpiredException;

    void removeInactivatedAccounts();
}
