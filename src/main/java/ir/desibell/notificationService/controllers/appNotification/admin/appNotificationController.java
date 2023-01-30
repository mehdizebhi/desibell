package ir.desibell.notificationService.controllers.appNotification.admin;

import ir.desibell.notificationService.entities.appNotification.AppNotification;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/notification")
public class appNotificationController {
    
    @Autowired
    private AppNotificationService notificationService;

    public appNotificationController(AppNotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @GetMapping("")
    public String appNotificationAdminPage(Model model, Principal principal, @PageableDefault(size = 20, sort = {"updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("principal", principal);
        model.addAttribute("ans", this.notificationService.findAllNotificationsPage(pageable));
        model.addAttribute("newAn", new AppNotification());
        model.addAttribute("editAn", new AppNotification());
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        return "admin/appNotification/appNotification-table";
    }
    
    @PostMapping("/register")
    public String registerNotification(@ModelAttribute AppNotification notification){
        this.notificationService.registerNotification(notification);
        return "redirect:/admin/notification";
    }
    
    @PostMapping("/edit")
    public String editRole(@ModelAttribute AppNotification notification) throws IllegalAccessException, InvocationTargetException{
        this.notificationService.updateNotification(notification);
        return "redirect:/admin/notification";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable(name = "id") Long id){
        this.notificationService.deleteById(id);
        return "redirect:/admin/notification";
    }
    
}
