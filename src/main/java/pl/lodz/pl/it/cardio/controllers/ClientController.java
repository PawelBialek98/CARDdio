package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.service.WorkOrderService;

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
}
