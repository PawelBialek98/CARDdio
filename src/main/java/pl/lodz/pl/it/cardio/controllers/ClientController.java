package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.dto.AssignWorkOrderDto;
import pl.lodz.pl.it.cardio.dto.WorkOrderDto;
import pl.lodz.pl.it.cardio.entities.WorkOrder;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.service.WorkOrderService;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import java.util.UUID;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final WorkOrderService workOrderService;

    @GetMapping
    public ModelAndView getClientPage(){
        return new ModelAndView("client/client", "repairs", ObjectMapper.mapAll(workOrderService.getAllWorkOrdersForClient(), WorkOrderDto.class));
    }

    @GetMapping("/newOrder")
    public ModelAndView getNewOrderForm(){
        return new ModelAndView("client/newOrder", "orders", ObjectMapper.mapAll(workOrderService.getAllUnAssignedWorkOrders(), AssignWorkOrderDto.class));
    }

    @PostMapping("/newOrder")
    public String assignToOrder(@RequestParam("orderBusinessKey") String orderBusinessKey, RedirectAttributes redirectAttributes){
        try{
            workOrderService.assignUserToWorkOrder(UUID.fromString(orderBusinessKey));
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/client";
        }
        redirectAttributes.addFlashAttribute("sucessMessage", "Sucess!!!");
        return "redirect:/client";

    }

    @PostMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("orderBusinessKey") String orderBusinessKey, RedirectAttributes redirectAttributes){
        try{
            workOrderService.unassignUserFromWorkOrder(UUID.fromString(orderBusinessKey));
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/client";
        }
        redirectAttributes.addFlashAttribute("sucessMessage", "Sucess!!!");
        return "redirect:/client";
    }
}
