package pl.lodz.pl.it.cardio.controllers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.pl.it.cardio.dto.WorkOrderDto;
import pl.lodz.pl.it.cardio.service.WorkOrderService;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final WorkOrderService workOrderService;

    @Autowired
    public ClientController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public ModelAndView getClientPage(){
        return new ModelAndView("client/client", "repairs", workOrderService.getAllWorkOrdersForClient());
    }

    @GetMapping("/newOrder")
    public ModelAndView getNewOrderForm(){
        ModelAndView modelAndView = new ModelAndView("client/newOrder", "order", new WorkOrderDto());
        modelAndView.addObject("WOTypes", workOrderService.getAllWorkOrderTypeNames());
        return modelAndView;
    }
}
