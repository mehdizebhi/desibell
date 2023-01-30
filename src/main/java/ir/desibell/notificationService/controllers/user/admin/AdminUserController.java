package ir.desibell.notificationService.controllers.user.admin;

import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.user.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppNotificationService notificationService;

    @GetMapping("")
    public String userAdminPage(Model model, Principal principal, @PageableDefault(size = 20) Pageable pageable) {
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        model.addAttribute("users", this.userService.findAllUsersPage(pageable));
        model.addAttribute("editUser", new User());
        
        return "admin/user/user-table";
    }

}
