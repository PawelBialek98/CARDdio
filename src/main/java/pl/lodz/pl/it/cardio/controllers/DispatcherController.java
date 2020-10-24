package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lodz.pl.it.cardio.dto.WorkOrderDto;
import pl.lodz.pl.it.cardio.exception.AppBaseException;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.service.WorkOrderService;
import pl.lodz.pl.it.cardio.utils.ObjectMapper;

import java.util.UUID;

@Controller
@RequestMapping("/dispatcher")
@RequiredArgsConstructor
public class DispatcherController {

    private final WorkOrderService workOrderService;
    private final UserService userService;

    @GetMapping
    public ModelAndView getDispatcherPage() throws AppNotFoundException {
        return new ModelAndView("dispatcher/dispatcher", "repairs", ObjectMapper.mapAll(workOrderService.getAllWorkOrders(), WorkOrderDto.class));
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
