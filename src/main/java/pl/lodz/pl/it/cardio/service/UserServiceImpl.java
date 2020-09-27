package pl.lodz.pl.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.dto.ChangeUserPasswordDto;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.exception.ValueNotUniqueException;
import pl.lodz.pl.it.cardio.repositories.EmployeeRepository;
import pl.lodz.pl.it.cardio.repositories.RoleRepository;
import pl.lodz.pl.it.cardio.repositories.UserRepository;
import pl.lodz.pl.it.cardio.repositories.VerificationTokenRepository;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }

    @Override
    public void addUser(UserDto userDto) throws AppNotFoundException, ValueNotUniqueException{
        //TODO - parametr systemowy
        User user =  new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhoneNumber());
        //ArrayList<Role> roles = new ArrayList<>();
        //roles.add(roleRepository.findByCode("CLIENT"));
        if(userRepository.existsByEmail(user.getEmail())){
            throw ValueNotUniqueException.createEmailNotUniqueException(user);
        }
        user.setActivated(false);
        user.setCreateDate(new Date());
        user.setRoles(roleRepository.findByCode("CLIENT").orElseThrow(AppNotFoundException::createRoleNotFoundException));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(UserDto user, String token) {
        VerificationToken myToken = new VerificationToken(token, userRepository.findByEmail(user.getEmail()).get());
        tokenRepository.save(myToken);
        Logger.getGlobal().log(Level.INFO, myToken.toString());
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveRegisteredUser(User user) {
        //User user = userRepository.findByEmail(userDto.getEmail());
        userRepository.save(user);
    }

    @Override
    public UserDto findByEmail(String email) throws AppNotFoundException {
        return ObjectMapper.map(userRepository.findByEmail(email).orElseThrow(AppNotFoundException::createUserNotFoundException), UserDto.class);
    }

    @Override
    public void setNewPassword(User changeUserPasswordDto) throws AppNotFoundException {
        User user = userRepository.findByEmail(changeUserPasswordDto.getEmail()).orElseThrow(AppNotFoundException::createUserNotFoundException);
        user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public User getCurrentUser() throws AppNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(AppNotFoundException::createUserNotFoundException);
    }

    @Override
    public void editUser(User editUser) throws AppNotFoundException {
        User user = userRepository.findByEmail(editUser.getEmail()).orElseThrow(AppNotFoundException::createUserNotFoundException);
        user.setFirstName(editUser.getFirstName());
        user.setLastName(editUser.getLastName());
        user.setPhoneNumber(editUser.getPhoneNumber());
        userRepository.save(user);
    }
}
