package pl.lodz.p.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.cardio.dto.UserDto.EditAdminUserDto;
import pl.lodz.p.it.cardio.dto.UserDto.EmployeeDto;
import pl.lodz.p.it.cardio.dto.ResetMailDto;
import pl.lodz.p.it.cardio.entities.*;
import pl.lodz.p.it.cardio.exception.*;
import pl.lodz.p.it.cardio.repositories.*;
import pl.lodz.p.it.cardio.utils.ObjectMapper;
import pl.lodz.p.it.cardio.events.accountOperation.AccountOperationEvent;
import pl.lodz.p.it.cardio.dto.UserDto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final WorkOrderTypeRepository workOrderTypeRepository;
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
        //TODO przenieść do parmetrów systemowych
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
        try {
            verifyToken(token,"register");
            User user = getUserByToken(token);
            user.setActivated(true);
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
    public void setNewPassword(User changeUserPasswordDto, String token) throws AppNotFoundException, AppTransactionFailureException, TokenExpiredException {
        try{
            verifyToken(token, "resetPassword");

            User user = userRepository.findByEmail(changeUserPasswordDto.getEmail()).orElseThrow(AppNotFoundException::createUserNotFoundException);
            user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getPassword()));
            userRepository.saveAndFlush(user);
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
    }

    @Override
    @PostAuthorize("returnObject.email == authentication.principal.username")
    public User getCurrentUser() throws AppNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(AppNotFoundException::createUserNotFoundException);
    }

    @Override
    @PreAuthorize("#editUser.email == authentication.principal.username || hasRole('ROLE_ADMINISTRATOR')")
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
        } catch (ObjectOptimisticLockingFailureException e){
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
    public void verifyToken(String token, String type) throws AppNotFoundException, TokenExpiredException, AppTransactionFailureException {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElseThrow(AppNotFoundException::createTokenNotFoundException);
        if (!verificationToken.getType().equals(type)) {
            throw AppNotFoundException.createTokenNotFoundException();
        }
        if ((verificationToken.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime()) <= 0 || verificationToken.isUsed()) {
            throw TokenExpiredException.createTokenExpiredException(verificationToken);
        }
        verificationToken.setUsed(true);
        try{
            tokenRepository.saveAndFlush(verificationToken);
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }
    }

    @Override
    public void removeInactivatedAccounts() throws AppTransactionFailureException {
        try{
            userRepository.deleteAll(userRepository.findAllByActivatedIsFalse());
        } catch (ObjectOptimisticLockingFailureException e){
            throw AppTransactionFailureException.createOptimisticLockingException(e.getCause());
        }

    }

    @Override
    public EditAdminUserDto prepareEditUser(Employee employeeState, User userState) {
        Collection<Role> roles = roleRepository.findAll();
        HashMap<String, Boolean> rolesMap = new HashMap<>();
        HashMap<String, Boolean> wotMap = new HashMap<>();
        List<String> wotList = new ArrayList<>();

        EditAdminUserDto editAdminUserDto = ObjectMapper.map(userState, EditAdminUserDto.class);

        for(Role role : roles){
            if(userState.getRoles().stream().map(Role::getCode).collect(Collectors.toList()).contains(role.getCode())) {
                rolesMap.put(role.getCode(),true);
            }else {
                rolesMap.put(role.getCode(), false);
            }
        }
        if(employeeState != null){
            editAdminUserDto.setDateBirth(employeeState.getBirth().toString());

            for(WorkOrderType wot : workOrderTypeRepository.findAllByActiveIsTrue()){
                if(employeeState.getWorkOrderTypes().stream().map(WorkOrderType::getCode).collect(Collectors.toList()).contains(wot.getCode())) {
                    wotMap.put(wot.getCode(),true);
                    wotList.add(wot.getCode());
                }else {
                    wotMap.put(wot.getCode(), false);
                }
            }
        } else {
            for(WorkOrderType wot : workOrderTypeRepository.findAllByActiveIsTrue()){
                wotMap.put(wot.getCode(), false);
            }
        }
        editAdminUserDto.setWorkOrderTypeMap(wotMap);
        editAdminUserDto.setRolesMap(rolesMap);
        editAdminUserDto.setWorkOrderType(wotList);

        return editAdminUserDto;
    }

    @Override
    public void adminEditUser(User userState, Employee employeeState, EditAdminUserDto userDto) throws EmptyRoleException, AppTransactionFailureException {
        userState.setFirstName(userDto.getFirstName());
        userState.setLastName(userDto.getLastName());
        userState.setPhoneNumber(userDto.getPhoneNumber());
        if(!userState.getLocked() && userDto.getLocked()){
            userState.setInvalidLoginAttempts(0);
        }
        userState.setLocked(userDto.getLocked());
        userState.setActivated(userDto.getActivated());

        Collection<String> newRoles = new ArrayList<>();
        if(userDto.getRolesMap().values().stream().allMatch(Objects::isNull)){
            throw EmptyRoleException.createEmptyRoleException();
        }

        for(Map.Entry<String,Boolean> roleMap : userDto.getRolesMap().entrySet()){
            if(null != roleMap.getValue()){
                newRoles.add(roleMap.getKey());
            }
        }

        userState.setRoles(roleRepository.findAllByCodeIn(newRoles));
        this.editUser(userState);

        if(newRoles.contains("MECHANIC")){
            LocalDate tmp = LocalDate.now();
            try{
                tmp = LocalDate.parse(userDto.getDateBirth());
            } catch (DateTimeParseException re){
                Logger.getGlobal().log(Level.INFO, "Wrong date format! " + userDto.getDateBirth());
            }
            Collection<WorkOrderType> wot = workOrderTypeRepository.findAllByActiveIsTrueAndCodeIn(userDto.getWorkOrderType());
            if(employeeState != null){
                employeeState.setBirth(tmp);
                employeeState.setWorkOrderTypes(wot);
            } else {
                employeeState = new Employee(tmp,userState,wot);
            }
            adminEditEmployee(employeeState);
        }
    }

    @Override
    public User getUserByToken(String token) throws AppNotFoundException {
        return tokenRepository.findByToken(token).orElseThrow(AppNotFoundException::createTokenNotFoundException).getUser();
    }
}
