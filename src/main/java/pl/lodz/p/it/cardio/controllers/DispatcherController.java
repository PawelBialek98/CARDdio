package pl.lodz.p.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.p.it.cardio.dto.EmployeeDto;
import pl.lodz.p.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.p.it.cardio.exception.AppBaseException;
import pl.lodz.p.it.cardio.service.UserService;
import pl.lodz.p.it.cardio.service.WorkOrderService;
import pl.lodz.p.it.cardio.utils.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/dispatcher")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_DISPATCHER')")
public class DispatcherController {

    private final WorkOrderService workOrderService;
    private final UserService userService;

    @Qualifier("messageSource")
    private final MessageSource messages;


    @GetMapping
    public ModelAndView getDispatcherPage(@ModelAttribute("errorMessage") String errorMessage,
                                          @ModelAttribute("message") String message) {
        ModelAndView modelAndView = new ModelAndView("dispatcher/dispatcher", "repairs", workOrderService.getAllWorkOrders());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @GetMapping("/allEmployees")
    public ModelAndView allEmployees() {
        return new ModelAndView("dispatcher/allEmployees", "employees", userService.getAllEmployee());
    }


    @GetMapping("/newOrder")
    public String getNewOrderForm(@RequestParam("employeeBusinessKey") String employeeBusinessKey, final Model model, RedirectAttributes redirectAttributes){
        model.addAttribute("order", new NewWorkOrderDto());
        try{
            model.addAttribute("employee", ObjectMapper.map(userService.getEmployee(UUID.fromString(employeeBusinessKey)), EmployeeDto.class));
        } catch (AppBaseException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dispatcher";
        }
        return "dispatcher/newOrder";
    }

    @PostMapping("/newOrder")
    public String createNewOrder(@Valid @ModelAttribute("order") NewWorkOrderDto newWorkOrderDto,
                                 BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "dispatcher/newOrder";
        }
        try {
            workOrderService.addWorkOrder(newWorkOrderDto);
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dispatcher";
        }
        redirectAttributes.addFlashAttribute("message", messages.getMessage("newOrder.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/dispatcher";
    }

    @PostMapping("/cancelOrder")
    public String cancelOrder(HttpServletRequest request, @RequestParam("orderBusinessKey") String orderBusinessKey, RedirectAttributes redirectAttributes){
        try{
            workOrderService.unassignUserFromWorkOrder(UUID.fromString(orderBusinessKey));
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dispatcher";
        }
        redirectAttributes.addFlashAttribute("message", messages.getMessage("cancelOrder.success", null, request.getLocale()));
        return "redirect:/dispatcher";

    }
}
