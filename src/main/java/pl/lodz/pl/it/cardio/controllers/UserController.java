package pl.lodz.pl.it.cardio.controllers;

import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.validation.Valid;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getAll(){
        return new ModelAndView("index", "users", userService.getAllUsers());
    }

    @GetMapping("/employee")
    public ModelAndView getAllEmployee(){
        return new ModelAndView("index", "users", userService.getAllEmployee());
    }

    /*@GetMapping("/main")
    public String getMainLayout(){
        return "layouts/main_layout";
    }*/

    @GetMapping("/signIn")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(){
        return new ModelAndView("register", "user", new UserDto());
    }

    @PostMapping("/register")
    public ModelAndView addUser(@Valid UserDto userDto){
        //return new ModelAndView("register", "user", new UserDto());
        User user = ObjectMapper.map(userDto, User.class);
        userService.addUser(user);
        return new ModelAndView("index", "users", userService.getAllUsers());
    }
}
