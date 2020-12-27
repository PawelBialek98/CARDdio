package pl.lodz.p.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.p.it.cardio.dto.UserDto.EditAdminUserDto;
import pl.lodz.p.it.cardio.dto.WorkOrderTypeDto;
import pl.lodz.p.it.cardio.entities.Employee;
import pl.lodz.p.it.cardio.exception.AppBaseException;
import pl.lodz.p.it.cardio.service.UserService;
import pl.lodz.p.it.cardio.service.WorkOrderTypeService;
import pl.lodz.p.it.cardio.entities.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
public class AdminController {

    private final UserService userService;
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
            model.addAttribute("wot", workOrderTypeService.findAll());
        } catch (AppBaseException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin";
        }
        return "admin/editUserData";
    }

    @PostMapping("/editAccount")
    public String editAccount(@Valid @ModelAttribute("user") EditAdminUserDto userDto,
                              BindingResult result, RedirectAttributes redirectAttributes){
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

    @GetMapping("/allOrderType")
    public ModelAndView getAllOrderTypePage(){
        return new ModelAndView("admin/allOrderTypes", "orderTypes", workOrderTypeService.findAll());
    }

    @PostMapping("/changeActivity")
    public String changeActivity( @RequestParam("orderTypeBusinessKey") String orderTypeBusinessKey,RedirectAttributes redirectAttributes){
        try {
            workOrderTypeService.changeActivity(UUID.fromString(orderTypeBusinessKey));
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/allOrderType";
    }

    @GetMapping("/newOrderType")
    public ModelAndView getNewOrderTypePage(){
        return new ModelAndView("admin/newOrderType", "orderType", new WorkOrderTypeDto());
    }

    @PostMapping("/newOrderType")
    public String createNewOrderType(@Valid @ModelAttribute("orderType") WorkOrderTypeDto workOrderTypeDto, BindingResult result,
                                     Model model, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "admin/newOrderType";
        }
        try{
            workOrderTypeService.addWorkOrderType(workOrderTypeDto);
        } catch (AppBaseException e) {
            model.addAttribute("errorMessage",e.getMessage());
            return "admin/newOrderType";
        }
        redirectAttributes.addFlashAttribute("message","SUCESS");
        return "redirect:/admin/allOrderType";
    }
}
