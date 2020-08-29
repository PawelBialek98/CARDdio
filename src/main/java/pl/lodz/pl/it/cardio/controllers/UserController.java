package pl.lodz.pl.it.cardio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.configuration.OnRegistrationCompleteEvent;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.exception.ValueNotUniqueException;
import pl.lodz.pl.it.cardio.service.IUserService;
import pl.lodz.pl.it.cardio.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UserController {

    private IUserService userService;

    private ApplicationEventPublisher eventPublisher;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messages;

    @Autowired
    public UserController(UserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
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

    @GetMapping("/client")
    public String getClientPage(){
        return "client/client";
    }

    // Login form
    @RequestMapping("/login")
    public String login() {
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
                          Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "login/register";
        }
        //return new ModelAndView("register", "user", new UserDto());
        //User user = ObjectMapper.map(userDto, User.class);
        try{
            //User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhoneNumber());
            userService.addUser(userDto);
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDto,
                    request.getLocale(), appUrl));
        } catch (AppNotFoundException | ValueNotUniqueException e) {
            //TODO
            redirectAttributes.addFlashAttribute("message","Nie siadło! " + e.getMessage());
            return "login/register";
        }
        //Dodać wysyłanie emaila
        model.addAttribute("users", userService.getAllUsers());
        return "login/login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        Logger.getGlobal().log(Level.INFO, "Wywolanie!");
        VerificationToken verificationToken = userService.getVerificationToken(token);
        Logger.getGlobal().log(Level.INFO, verificationToken.toString());
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "login/badUser";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "login/badUser";
        }

        user.setActivated(true);
        Logger.getGlobal().log(Level.INFO, "Siadło! Zaloguj się!");
        userService.saveRegisteredUser(user);
        return "redirect:/login" + request.getLocale().getLanguage();
    }


}
