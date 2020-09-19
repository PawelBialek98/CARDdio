package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.pl.it.cardio.dto.NewWorkOrderDto;
import pl.lodz.pl.it.cardio.service.UserService;
import pl.lodz.pl.it.cardio.service.WorkOrderService;
import pl.lodz.pl.it.cardio.service.WorkOrderTypeService;

import javax.validation.Valid;

@Controller
@RequestMapping("/mechanic")
@RequiredArgsConstructor
public class MechanicController {

    private final WorkOrderService workOrderService;
    private final WorkOrderTypeService workOrderTypeService;
    private final UserService userService;

    @GetMapping
    public ModelAndView getMechanicPage(){
        return new ModelAndView("mechanic/mechanic", "repairs", workOrderService.getAllWorkOrdersForEmployee());
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
}
