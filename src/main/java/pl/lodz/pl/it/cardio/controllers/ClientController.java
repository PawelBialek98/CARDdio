package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.service.WorkOrderService;

import java.util.UUID;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final WorkOrderService workOrderService;
    private final UserService userService;

    @GetMapping
    public ModelAndView getClientPage(){
        return new ModelAndView("client/client", "repairs", workOrderService.getAllWorkOrdersForClient());
    }

    @GetMapping("/newOrder")
    public ModelAndView getNewOrderForm(){
        return new ModelAndView("client/newOrder", "orders", workOrderService.getAllUnAssignedWorkOrders());
    }

    @PostMapping("/newOrder")
    public String assignToOrder(@RequestParam("orderBusinessKey") String orderBusinessKey, RedirectAttributes redirectAttributes){
        //WorkOrder workOrder;
        try{
            //workOrder = workOrderService.getWorkOrderByBusinessKey(orderBusinessKey);
            workOrderService.assignUserToWorkOrder(UUID.fromString(orderBusinessKey));
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            //model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/client/newOrder";
        }
        //model.addAttribute("sucessMessage", "Sucess!!!");
        redirectAttributes.addFlashAttribute("sucessMessage", "Sucess!!!");
        return "redirect:/client";

    }
}
