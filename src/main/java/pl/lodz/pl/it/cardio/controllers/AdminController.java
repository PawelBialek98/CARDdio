package pl.lodz.pl.it.cardio.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.pl.it.cardio.service.UserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @RequestMapping
    private ModelAndView getMainAdminPage(@ModelAttribute("errorMessage") String errorMessage){
        ModelAndView modelAndView = new ModelAndView("admin/admin", "users", userService.getAllUsers());
        modelAndView.addObject("errorMessage", errorMessage);
        return modelAndView;
    }

}
