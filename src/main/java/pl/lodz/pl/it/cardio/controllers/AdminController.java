package pl.lodz.pl.it.cardio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.lodz.pl.it.cardio.service.IUserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IUserService userService;

    @Autowired
    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping
    private ModelAndView getMainAdminPage(){
        return new ModelAndView("admin/admin", "users", userService.getAllUsers());
    }

}
