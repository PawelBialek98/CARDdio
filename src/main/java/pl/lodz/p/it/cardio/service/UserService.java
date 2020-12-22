package pl.lodz.p.it.cardio.service;

import pl.lodz.p.it.cardio.dto.UserDto.EditAdminUserDto;
import pl.lodz.p.it.cardio.dto.UserDto.EmployeeDto;
import pl.lodz.p.it.cardio.dto.ResetMailDto;
import pl.lodz.p.it.cardio.dto.UserDto.UserDto;
import pl.lodz.p.it.cardio.entities.VerificationToken;
import pl.lodz.p.it.cardio.exception.*;
import pl.lodz.p.it.cardio.entities.Employee;
import pl.lodz.p.it.cardio.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Transactional
public interface UserService {
    List<User> getAllUsers();

    List<EmployeeDto> getAllEmployee();

    void addUser(UserDto userDto, HttpServletRequest request) throws AppNotFoundException, ValueNotUniqueException, AppTransactionFailureException;

    void createVerificationToken(UserDto user, String token,  String type);

    void activateAccount(String token) throws AppNotFoundException, TokenExpiredException, AppTransactionFailureException;

    User findByEmail(String email) throws AppNotFoundException;

    void setNewPassword(User changeUserPasswordDto, String token) throws AppNotFoundException, AppTransactionFailureException, TokenExpiredException;

    User getCurrentUser() throws AppNotFoundException;

    void editUser(User user) throws AppTransactionFailureException;

    User getUser(UUID userBusinessKey) throws AppNotFoundException;

    Employee getEmployee(UUID employeeBusinessKey) throws AppBaseException;

    boolean isEmployee(UUID userBusinessKey);

    void adminEditEmployee(Employee employeeState) throws AppTransactionFailureException;

    int countAllActiveUsers();

    int countAllEmployees();

    void resetPassword(ResetMailDto userEmail, HttpServletRequest request) throws AppNotFoundException;

    void verifyToken(String token, String type) throws AppNotFoundException, TokenExpiredException, AppTransactionFailureException;

    void removeInactivatedAccounts() throws AppTransactionFailureException;

    EditAdminUserDto prepareEditUser(Employee employeeState, User userState);

    void adminEditUser(User userState, Employee employeeState, EditAdminUserDto userDto) throws EmptyRoleException, AppTransactionFailureException;

    User getUserByToken(String token) throws AppNotFoundException;
}
