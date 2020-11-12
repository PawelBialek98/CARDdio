package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.configuration.AccountOperationEvent;
import pl.lodz.pl.it.cardio.configuration.OnResetPasswordCompleteEvent;
import pl.lodz.pl.it.cardio.dto.ChangeUserPasswordDto;
import pl.lodz.pl.it.cardio.dto.EditUserDto;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.service.WorkOrderService;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserController {

    private final UserService userService;
    private final WorkOrderService workOrderService;
    private final ApplicationEventPublisher eventPublisher;

    private User userState;

    @Qualifier("messageSource")
    private final MessageSource messages;

    @GetMapping
    public ModelAndView getAll(){
        ModelAndView modelAndView = new ModelAndView("index", "numOfUsers", userService.countAllActiveUsers());
        modelAndView.addObject("numOfEmployees", userService.countAllEmployees());
        modelAndView.addObject("numOfAllRepairs", workOrderService.countAllFinishedWorkOrders());
        return modelAndView;
    }

    @RequestMapping("/login")
    public ModelAndView login(@ModelAttribute("errorMessage") String errorMessage) {
        return new ModelAndView("login/login", "errorMessage", errorMessage);
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(@ModelAttribute("message") String message){
        ModelAndView modelAndView =  new ModelAndView("login/register", "user", new UserDto());
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }

    @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result,
                          Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "login/register";
        }
        try{
            //User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhoneNumber());
            userService.addUser(userDto);
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new AccountOperationEvent(userDto,
                    request.getLocale(), appUrl, "register"));
        } catch (AppBaseException e) {
            //TODO albo zmienić na modelandview atrybut
            redirectAttributes.addFlashAttribute("errorMessage","Nie siadło! " + e.getMessage());
            return "redirect:/register";
        }
        model.addAttribute("users", userService.getAllUsers());
        return "redirect:/login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
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
        userService.saveRegisteredUser(user);
        return "redirect:/login" + request.getLocale().getLanguage();
    }

    @GetMapping("/resetPassword")
    public String getResetPasswordPage(){
        return "login/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail,
                                final Model model, RedirectAttributes redirectAttributes) {
        UserDto userDto;
        try{
            userDto = userService.findByEmail(userEmail);
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new AccountOperationEvent(userDto,
                    request.getLocale(), appUrl, "resetPassword"));
        } catch (AppBaseException e) {
            //redirectAttributes.addFlashAttribute("message", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "login/resetPassword";
        }
        return "redirect:/login";
    }

    @GetMapping("/setNewPassword")
    public String getNewPasswordForm(WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "login/badUser";
        }

        userState = verificationToken.getUser();
        ChangeUserPasswordDto user = ObjectMapper.map(userState, ChangeUserPasswordDto.class);
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "login/badUser";
        }

        model.addAttribute("user", user);
        return "login/changePassword";
    }

    @PostMapping("/setNewPassword")
    public String setNewPassword(HttpServletRequest request, @Valid @ModelAttribute("user") ChangeUserPasswordDto userDto,
                                final Model model, RedirectAttributes redirectAttributes) {
        try{
            userState.setPassword(userDto.getPassword());
            userService.setNewPassword(userState);
        } catch (AppBaseException e) {
            //redirectAttributes.addFlashAttribute("message", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "login/login";
        }
        return "redirect:/login";
    }

    @GetMapping("/editAccount")
    public String getEditAccountForm(final Model model, RedirectAttributes redirectAttributes){
        //EditUserDto user;
        try{
            userState = userService.getCurrentUser();
            //user = ObjectMapper.map(userState,EditUserDto.class);
            model.addAttribute("user", ObjectMapper.map(userState,EditUserDto.class));
        } catch (AppBaseException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/login";
        }
        return "login/editOwnData";
    }

    @PostMapping("/editAccount")
    public String editAccount(@Valid @ModelAttribute("user") EditUserDto userDto, BindingResult result,
                              Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "login/editOwnData";
        }
        try{
            //User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhoneNumber());
            userState.setFirstName(userDto.getFirstName());
            userState.setLastName(userDto.getLastName());
            userState.setPhoneNumber(userDto.getPhoneNumber());

            userService.editUser(userState);
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return redirectRules();
        }
        redirectAttributes.addFlashAttribute("message","Success!!");
        return redirectRules();
    }

    @GetMapping("/goback")
    private String redirectRules(){
        Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        if(roles.contains("ROLE_ADMINISTRATOR")){
            return "redirect:/admin";
        } else if(roles.contains("ROLE_DISPATCHER")){
            return "redirect:/dispatcher";
        } else if(roles.contains("ROLE_MECHANIC")){
            return "redirect:/mechanic";
        } else if(roles.contains("ROLE_CLIENT")){
            return "redirect:/client";
        } else {
            return "redirect:/";
        }
    }

    @Scheduled(cron = "${cron.deleteInactiveUsers}", zone = "Europe/Warsaw")
    public void removeInactivatedAccounts() {

        long now = System.currentTimeMillis() / 1000;
        Logger.getGlobal().log(Level.INFO,
                "schedule tasks using cron jobs - " + now);
    }


}
