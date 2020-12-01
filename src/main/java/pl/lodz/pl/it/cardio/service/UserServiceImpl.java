package pl.lodz.pl.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.configuration.AccountOperationEvent;
import pl.lodz.pl.it.cardio.dto.ChangeUserPasswordDto;
import pl.lodz.pl.it.cardio.dto.EmployeeDto;
import pl.lodz.pl.it.cardio.dto.ResetMailDto;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.*;
import pl.lodz.pl.it.cardio.repositories.EmployeeRepository;
import pl.lodz.pl.it.cardio.repositories.RoleRepository;
import pl.lodz.pl.it.cardio.repositories.UserRepository;
import pl.lodz.pl.it.cardio.repositories.VerificationTokenRepository;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public List<EmployeeDto> getAllEmployee(){
        return ObjectMapper.mapAll(employeeRepository.findAll(), EmployeeDto.class);
    }

    @Override
    public void addUser(UserDto userDto, HttpServletRequest request) throws AppNotFoundException, ValueNotUniqueException, AppTransactionFailureException {
        User user =  new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhoneNumber());
        if(userRepository.existsByEmail(user.getEmail())){
            throw ValueNotUniqueException.createEmailNotUniqueException(user);
        }
        user.setActivated(false);
        user.setCreateDate(new Date());
        user.setRoles(roleRepository.findByCode("CLIENT").orElseThrow(AppNotFoundException::createStatusNotFoundException));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try{
            userRepository.save(user);
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }

        eventPublisher.publishEvent(new AccountOperationEvent(userDto,
                request.getLocale(), request.getContextPath(), "register"));
    }

    @Override
    public void createVerificationToken(UserDto user, String token, String type) {
        VerificationToken myToken = new VerificationToken(token, userRepository.findByEmail(user.getEmail()).get(), type);
        tokenRepository.save(myToken);
    }

    @Override
    public void activateAccount(String token) throws AppNotFoundException, TokenExpiredException, AppTransactionFailureException {

        VerificationToken verificationToken = tokenRepository.findByToken(token).orElseThrow(AppNotFoundException::createTokenNotFoundException);

        if (!verificationToken.getType().equals("register")) {
            throw AppNotFoundException.createTokenNotFoundException();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw TokenExpiredException.createTokenExpiredException(verificationToken);
        }

        user.setActivated(true);

        try {
            userRepository.save(user);
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
    }

    @Override
    public User findByEmail(String email) throws AppNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(AppNotFoundException::createUserNotFoundException);
    }

    @Override
    public void setNewPassword(User changeUserPasswordDto) throws AppNotFoundException, AppTransactionFailureException {
        User user = userRepository.findByEmail(changeUserPasswordDto.getEmail()).orElseThrow(AppNotFoundException::createUserNotFoundException);
        user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getPassword()));
        try{
            userRepository.saveAndFlush(user);
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
    }

    @Override
    public User getCurrentUser() throws AppNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(AppNotFoundException::createUserNotFoundException);
    }

    @Override
    public void editUser(User editUser) throws AppTransactionFailureException {
        try{
            userRepository.saveAndFlush(editUser);
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
    }

    @Override
    public User getUser(UUID userBusinessKey) throws AppNotFoundException {
        return userRepository.findByBusinessKey(userBusinessKey).orElseThrow(AppNotFoundException::createUserNotFoundException);
    }

    @Override
    public Employee getEmployee(UUID employeeBusinessKey) throws AppBaseException {
        return employeeRepository.findByUser_BusinessKey(employeeBusinessKey).orElseThrow(AppNotFoundException::createUserNotFoundException);
    }

    @Override
    public boolean isEmployee(UUID userBusinessKey) {
        return employeeRepository.existsByUser_BusinessKey(userBusinessKey);
    }

    @Override
    public void adminEditEmployee(Employee employeeState) throws AppTransactionFailureException {
        try{
            employeeRepository.save(employeeState);
        }  catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
    }

    @Override
    public int countAllActiveUsers() {
        return userRepository.countAllByActivatedIsTrueAndLockedIsFalse();
    }

    @Override
    public int countAllEmployees() {
        return employeeRepository.countAllByUser_LockedIsFalse();
    }

    @Override
    public void resetPassword(ResetMailDto userEmail, HttpServletRequest request) throws AppNotFoundException {
        UserDto userDto = ObjectMapper.map(findByEmail(userEmail.getEmail()),UserDto.class);
        eventPublisher.publishEvent(new AccountOperationEvent(userDto,
                request.getLocale(), request.getContextPath(), "resetPassword"));
    }

    @Override
    public User verifyToken(String token) throws AppNotFoundException, TokenExpiredException {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElseThrow(AppNotFoundException::createTokenNotFoundException);
        if (!verificationToken.getType().equals("resetPassword")) {
            throw AppNotFoundException.createTokenNotFoundException();
        }

        if ((verificationToken.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime()) <= 0) {
            throw TokenExpiredException.createTokenExpiredException(verificationToken);
        }
        return verificationToken.getUser();
    }

    @Override
    public void removeInactivatedAccounts() throws AppTransactionFailureException {
        try{
            userRepository.deleteAll(userRepository.findAllByActivatedIsFalse());
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }

    }
}
