package ir.desibell.notificationService.controllers.user;

import ir.desibell.notificationService.entities.subscribe.Subscribe;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.subscribe.TypeOfSubscribeService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/profile")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private TypeOfSubscribeService typeOfSubscribeService;

    @Autowired
    private AppNotificationService appNotificationService;

    public AccountController(UserService userService, TypeOfSubscribeService typeOfSubscribeService) {
        this.userService = userService;
        this.typeOfSubscribeService = typeOfSubscribeService;
    }

    @GetMapping(value = {"", "/"})
    public String profilePage(Model model, Authentication authentication, Principal principal,
            @RequestParam(name = "notif", required = false) String notif) {
        model.addAttribute("user", (User) authentication.getPrincipal());
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        model.addAttribute("notif", notif);
        return "user/profile";
    }

    @GetMapping(value = {"/edit", "/edit/"})
    public String editProfilePage(Model model, Authentication authentication, Principal principal) {
        model.addAttribute("user", (User) authentication.getPrincipal());
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        return "user/profile-edit";
    }

    @PostMapping("/edit")
    public String editProfile(@ModelAttribute(name = "user") User user, Model model, Principal principal,
            RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException {

        if (this.userService.doesCanEdit(user)) {
            
            if (this.userService.isEditPassword(user)) {
                this.userService.registerUser(user);
                redirectAttributes.addAttribute("notif", "editProfile");
                return "redirect:/profile";
            } else {
                this.userService.updateExistUser(user);
                redirectAttributes.addAttribute("notif", "editProfile");
                return "redirect:/profile";
            }

        } else {
            model.addAttribute("error", this.userService.whyUserCantEdit(user));
        }
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        return "user/profile-edit";

    }

}
