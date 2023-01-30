package ir.desibell.notificationService.controllers.user;

import com.google.gson.Gson;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/show")
    @ResponseBody
    public String showUsers(){
        return new Gson().toJson(this.userService.findAllUsers());
    }

}
