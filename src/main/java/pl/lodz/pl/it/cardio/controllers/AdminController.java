package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.dto.*;
import pl.lodz.pl.it.cardio.entities.Role;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.service.RoleService;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private User userState;

    @RequestMapping
    private ModelAndView getMainAdminPage(@ModelAttribute("errorMessage") String errorMessage,
                                          @ModelAttribute("message") String message){
        ModelAndView modelAndView = new ModelAndView("admin/admin", "users", ObjectMapper.mapAll(userService.getAllUsers(), ListUsersDto.class));
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @GetMapping("/editAccount")
    public String getEditAccountForm(@RequestParam("userBusinessKey") String userBusinessKey, final @NotNull Model model, RedirectAttributes redirectAttributes){
        try{
            userState = userService.getUser(UUID.fromString(userBusinessKey));
            Collection<Role> roles = roleService.getAllRoles();
            HashMap<String, Boolean> rolesMap = new HashMap<>();
            //HashSet<Role> rolesSet = new HashSet<>();

            EditAdminUserDto editAdminUserDto = ObjectMapper.map(userState, EditAdminUserDto.class);

            for(Role role : roles){
                if(userState.getRoles().stream().map(Role::getCode).collect(Collectors.toList()).contains(role.getCode())) {
                    rolesMap.put(role.getName(),true);
                }else {
                    rolesMap.put(role.getName(), false);
                }
            }
            //editAdminUserDto.setRolesSet(ObjectMapper.mapAll(roles, RolesDto.class));

            Logger.getGlobal().log(Level.INFO, editAdminUserDto.toString());
            //user = ObjectMapper.map(userState,EditUserDto.class);
            model.addAttribute("user", editAdminUserDto);
            model.addAttribute("all_roles", ObjectMapper.mapAll(roles, RolesDto.class));
            //model.addAttribute("roles", ObjectMapper.mapAll(roleService.getAllRoles(), RolesDto.class));
        } catch (AppBaseException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin";
        }
        return "admin/editUserData";
    }

    @PostMapping("/editAccount")
    public String editAccount(@Valid @ModelAttribute("user") EditAdminUserDto userDto,
                              BindingResult result,
                              Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "admin/editUserData";
        }
        try{
            //User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhoneNumber());
            userState.setFirstName(userDto.getFirstName());
            userState.setLastName(userDto.getLastName());
            userState.setPhoneNumber(userDto.getPhoneNumber());

            Logger.getGlobal().log(Level.INFO, userDto.toString());
            Collection<String> newRoles = new ArrayList<>();
            /*for(Map.Entry<String,Boolean> roleMap : userDto.getRolesMap().entrySet()){
                if(roleMap.getValue().equals(true)){
                    newRoles.add(roleMap.getKey());
                }
            }*/
            userState.setRoles(roleService.getAllRolesByNames(newRoles));

            //userState.setRoles(roleService.getRoleByCode());

            userService.editUser(userState);
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/admin";
        }
        redirectAttributes.addFlashAttribute("message","Success!!");
        return "redirect:/admin";
    }

}
