package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.dto.EditUserDto;
import pl.lodz.pl.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.WorkOrderDto;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.service.WorkOrderService;
import pl.lodz.pl.it.cardio.service.WorkOrderTypeService;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/mechanic")
@RequiredArgsConstructor
public class MechanicController {

    private final WorkOrderService workOrderService;
    private final WorkOrderTypeService workOrderTypeService;
    private final UserService userService;

    @GetMapping
    public ModelAndView getMechanicPage(@ModelAttribute("errorMessage") String errorMessage){
        ModelAndView modelAndView = new ModelAndView("mechanic/mechanic", "repairs", workOrderService.getAllWorkOrdersForEmployee());
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

    @GetMapping("/newOrder")
    public ModelAndView getNewOrderForm(){
        ModelAndView modelAndView = new ModelAndView("mechanic/newOrder", "order", new NewWorkOrderDto());
        //TODO przekazanie do modelu wszystkich typów zadań które może wykonać mechanik bazując na jego umiejestnosciach
        modelAndView.addObject("wot", workOrderTypeService.getAllMyWorkOrderType());
        //modelAndView.addObject("WOTypes", workOrderService.getAllWorkOrderTypeNames());
        return modelAndView;
    }

    @RequestMapping("/newOrder")
    public String createNewOrder(@Valid @ModelAttribute("order") NewWorkOrderDto newWorkOrderDto, BindingResult result){
        if(result.hasErrors()){
            return "mechanic/newOrder";
        }
        workOrderService.addWorkOrder(newWorkOrderDto);
        return "redirect:/mechanic";
    }

    @PostMapping("/orderDetails")
    public String orderDetails(@RequestParam("orderBusinessKey") String orderBusinessKey,
                               final Model model,
                               RedirectAttributes redirectAttributes){
        try{
            WorkOrder workOrder = workOrderService.getWorkOrderByBusinessKey(UUID.fromString(orderBusinessKey));
            model.addAttribute("workOrder", ObjectMapper.map(workOrder, WorkOrderDto.class));
        } catch (AppBaseException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/mechanic";
        }
        return "mechanic/orderDetails";
    }

    @PostMapping("/changeStatus")
    public String changeStatus(@RequestParam("orderBusinessKey") String orderBusinessKey,
                              @RequestParam("statusCode") String statusCode,
                              RedirectAttributes redirectAttributes){
        try{
            workOrderService.changeStatus(UUID.fromString(orderBusinessKey), statusCode);
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mechanic";
        }
        redirectAttributes.addFlashAttribute("sucessMessage", "Sucess!!!");
        return "redirect:/mechanic";
    }
}
