package pl.lodz.pl.it.cardio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.Employee;
import pl.lodz.pl.it.cardio.entities.Role;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.repositories.EmployeeRepository;
import pl.lodz.pl.it.cardio.repositories.RoleRepository;
import pl.lodz.pl.it.cardio.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }

    public void addUser(User user) throws AppNotFoundException{
        //TODO - parametr systemowy
        //ArrayList<Role> roles = new ArrayList<>();
        //roles.add(roleRepository.findByCode("CLIENT"));
        user.setRoles(roleRepository.findByCode("CLIENT").orElseThrow(AppNotFoundException::createRoleNotFoundException));
        userRepository.save(user);
    }
}
