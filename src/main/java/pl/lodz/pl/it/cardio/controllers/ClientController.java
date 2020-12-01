package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final WorkOrderService workOrderService;

    @Qualifier("messageSource")
    private final MessageSource messages;

    @GetMapping
    public ModelAndView getClientPage(@ModelAttribute("errorMessage") String errorMessage,
                                      @ModelAttribute("message") String message){
        ModelAndView modelAndView = new ModelAndView("client/client", "repairs", workOrderService.getAllWorkOrdersForClient());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @GetMapping("/newOrder")
    public ModelAndView getNewOrderForm(){
        return new ModelAndView("client/newOrder", "orders", workOrderService.getAllUnAssignedWorkOrders());
    }

    @PostMapping("/newOrder")
    public String assignToOrder(HttpServletRequest request, @RequestParam("orderBusinessKey") String orderBusinessKey, RedirectAttributes redirectAttributes){
        try{
            workOrderService.assignUserToWorkOrder(UUID.fromString(orderBusinessKey));
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/client";
        }
        redirectAttributes.addFlashAttribute("message",  messages.getMessage("assignToOrder.success", null, request.getLocale()));
        return "redirect:/client";

    }

    @PostMapping("/cancelOrder")
    public String cancelOrder(HttpServletRequest request, @RequestParam("orderBusinessKey") String orderBusinessKey, RedirectAttributes redirectAttributes){
        try{
            workOrderService.unassignUserFromWorkOrder(UUID.fromString(orderBusinessKey));
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/client";
        }
        redirectAttributes.addFlashAttribute("message", messages.getMessage("cancelOrder.success", null, request.getLocale()));
        return "redirect:/client";
    }
}
