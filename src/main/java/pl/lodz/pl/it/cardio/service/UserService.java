package pl.lodz.pl.it.cardio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;
    private RoleRepository roleRepository;
    private VerificationTokenRepository tokenRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

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
        user.setRoles(roleRepository.findByCode("CLIENT").orElseThrow(AppNotFoundException::createRoleNotFoundException));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(UserDto user, String token) {
        VerificationToken myToken = new VerificationToken(token, userRepository.findByEmail(user.getEmail()));
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
}
