package pl.lodz.pl.it.cardio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.service.UserService;

import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        return "login/login";
    }

    // Login form
    @RequestMapping("/login")
    public String login() {
        Logger.getGlobal().log(Level.INFO, new BCryptPasswordEncoder().encode("adminnimda"));
        return "login/login";
    }

    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login/login-error";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(){
        return new ModelAndView("login/register", "user", new UserDto());
    }

    @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result,
                          Model model, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "login/register";
        }
        //return new ModelAndView("register", "user", new UserDto());
        //User user = ObjectMapper.map(userDto, User.class);
        try{
            User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhoneNumber());
            userService.addUser(user);
        } catch (AppNotFoundException e) {
            //TODO
            redirectAttributes.addFlashAttribute("message","Nie siadło!");
        }
        //Dodać wysyłanie emaila
        model.addAttribute("users", userService.getAllUsers());
        return "login/login";
    }
}
