package pl.lodz.p.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.p.it.cardio.dto.EditAdminUserDto;
import pl.lodz.p.it.cardio.dto.WorkOrderTypeDto;
import pl.lodz.p.it.cardio.entities.Employee;
import pl.lodz.p.it.cardio.entities.Role;
import pl.lodz.p.it.cardio.entities.WorkOrderType;
import pl.lodz.p.it.cardio.exception.AppBaseException;
import pl.lodz.p.it.cardio.exception.EmptyRoleException;
import pl.lodz.p.it.cardio.service.RoleService;
import pl.lodz.p.it.cardio.service.UserService;
import pl.lodz.p.it.cardio.service.WorkOrderTypeService;
import pl.lodz.p.it.cardio.utils.ObjectMapper;
import pl.lodz.p.it.cardio.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final WorkOrderTypeService workOrderTypeService;

    private Employee employeeState;
    private User userState;

    @GetMapping
    public ModelAndView getMainAdminPage(){
        return new ModelAndView("admin/admin", "users", userService.getAllUsers());
    }

    @GetMapping("/editAccount")
    public String getEditAccountForm(@RequestParam("userBusinessKey") String userBusinessKey, final @NotNull Model model, RedirectAttributes redirectAttributes){
        try{
            if(userService.isEmployee(UUID.fromString(userBusinessKey))){
                employeeState = userService.getEmployee(UUID.fromString(userBusinessKey));
                userState = employeeState.getUser();
            } else {
                userState = userService.getUser(UUID.fromString(userBusinessKey));
            }
            EditAdminUserDto editAdminUserDto = userService.prepareEditUser(employeeState, userState);

            model.addAttribute("user", editAdminUserDto);
            model.addAttribute("wot", ObjectMapper.mapAll(workOrderTypeService.findAll(), WorkOrderTypeDto.class));
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
            userService.adminEditUser(userState, employeeState, userDto);
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/admin";
        }
        redirectAttributes.addFlashAttribute("message","Success!!");
        return "redirect:/admin";
    }


}
