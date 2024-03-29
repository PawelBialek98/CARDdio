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
import pl.lodz.p.it.cardio.dto.WorkOrderDto.NewWorkOrderDto;
import pl.lodz.p.it.cardio.exception.AppBaseException;
import pl.lodz.p.it.cardio.service.WorkOrderService;
import pl.lodz.p.it.cardio.service.WorkOrderTypeService;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/mechanic")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MECHANIC')")
public class MechanicController {

    private final WorkOrderService workOrderService;
    private final WorkOrderTypeService workOrderTypeService;

    @Qualifier("messageSource")
    private final MessageSource messages;

    @GetMapping
    public ModelAndView getMechanicPage(@ModelAttribute("errorMessage") String errorMessage,
                                        @ModelAttribute("message") String message){
        ModelAndView modelAndView = new ModelAndView("mechanic/mechanic", "repairs", workOrderService.getAllWorkOrdersForEmployee());
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @GetMapping("/newOrder")
    public ModelAndView getNewOrderForm(){
        ModelAndView modelAndView = new ModelAndView("mechanic/newOrder", "order", new NewWorkOrderDto());
        modelAndView.addObject("wot", workOrderTypeService.getAllMyWorkOrderType());
        return modelAndView;
    }

    @RequestMapping("/newOrder")
    public String createNewOrder(@Valid @ModelAttribute("order") NewWorkOrderDto newWorkOrderDto,
                                 BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()){
            return "mechanic/newOrder";
        }
        try {
            workOrderService.addWorkOrder(newWorkOrderDto);
        } catch (AppBaseException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mechanic";
        }
        redirectAttributes.addFlashAttribute("message", messages.getMessage("newOrder.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/mechanic";
    }

    @PostMapping("/orderDetails")
    public String orderDetails(@RequestParam("orderBusinessKey") String orderBusinessKey,
                               final Model model,
                               RedirectAttributes redirectAttributes){
        try{
            model.addAttribute("workOrder", workOrderService.getWorkOrderByBusinessKey(UUID.fromString(orderBusinessKey)));
        } catch (AppBaseException e) {
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
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
        redirectAttributes.addFlashAttribute("message", messages.getMessage("order.changeStatus.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/mechanic";
    }
}
