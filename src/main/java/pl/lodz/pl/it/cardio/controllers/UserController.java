package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.dto.ChangeUserPasswordDto;
import pl.lodz.pl.it.cardio.dto.EditUserDto;
import pl.lodz.pl.it.cardio.dto.ResetMailDto;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
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
    public ModelAndView login(@ModelAttribute("errorMessage") String errorMessage,
                              @ModelAttribute("message") String message) {
        ModelAndView modelAndView = new ModelAndView("login/login", "errorMessage", errorMessage);
        modelAndView.addObject("message", message);
        return modelAndView;
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
        try{
            userService.addUser(userDto, request);
        } catch (AppBaseException e) {
            model.addAttribute("errorMessage",e.getMessage());
            return "login/register";
        }
        redirectAttributes.addFlashAttribute("message","SUPER!");
        return "redirect:/login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, @RequestParam("token") String token,
                                      RedirectAttributes redirectAttributes) {
        try{
            userService.activateAccount(token);
        } catch (AppBaseException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("message",messages.getMessage("auth.message.success", null, request.getLocale()));
        return "redirect:/login";
    }

    @GetMapping("/resetPassword")
    public ModelAndView getResetPasswordPage(){
        return new ModelAndView("login/resetPassword", "mail", new ResetMailDto());
    }

    @PostMapping("/resetPassword")
    public String resetPassword(HttpServletRequest request, @Valid  @ModelAttribute("mail") ResetMailDto userEmail, BindingResult bindingResult,
                                final Model model) {
        if(bindingResult.hasErrors()){
            return "login/resetPassword";
        }
        try{
            userService.resetPassword(userEmail, request) ;
        } catch (AppBaseException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login/resetPassword";
        }
        model.addAttribute("errorMessage",  messages.getMessage("resetPassword.checkMailBox", null, request.getLocale()));
        return "login/login";
    }

    @GetMapping("/setNewPassword")
    public String getNewPasswordForm(Model model, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

        try{
            userState = userService.verifyToken(token);
            model.addAttribute("user", ObjectMapper.map(userState, ChangeUserPasswordDto.class));
        } catch (AppBaseException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
        return "login/changePassword";
    }

    @PostMapping("/setNewPassword")
    public String setNewPassword(HttpServletRequest request, @Valid @ModelAttribute("user") ChangeUserPasswordDto userDto, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return "login/changePassword";
        }

        try{
            userState.setPassword(userDto.getPassword());
            userService.setNewPassword(userState);
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("message", messages.getMessage("resetPassword.success", null, request.getLocale()));
        return "redirect:/login";
    }

    @GetMapping("/editAccount")
    public String getEditAccountForm(final Model model, RedirectAttributes redirectAttributes){
        try{
            userState = userService.getCurrentUser();
            model.addAttribute("user", ObjectMapper.map(userState,EditUserDto.class));
        } catch (AppBaseException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/login";
        }
        return "login/editOwnData";
    }

    @PostMapping("/editAccount")
    public String editAccount(@Valid @ModelAttribute("user") EditUserDto userDto, BindingResult result,
                               HttpServletRequest request, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "login/editOwnData";
        }
        try{
            userState.setFirstName(userDto.getFirstName());
            userState.setLastName(userDto.getLastName());
            userState.setPhoneNumber(userDto.getPhoneNumber());

            userService.editUser(userState);
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return redirectRules();
        }
        redirectAttributes.addFlashAttribute("message",messages.getMessage("editAccount.success", null, request.getLocale()));
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
}
