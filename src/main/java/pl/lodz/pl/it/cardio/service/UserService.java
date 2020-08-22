package pl.lodz.pl.it.cardio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.repository.EmployeeRepository;
import pl.lodz.pl.it.cardio.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }

    public void addUser(User user){
        userRepository.save(user);
    }
}
