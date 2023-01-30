package ir.desibell.notificationService.controllers.confirmCode.admin;

import ir.desibell.notificationService.entities.confirmCode.ConfirmCode;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.confirmCode.ConfirmCodeService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/confirmCode")
public class AdminConfirmCodeController {
    
    @Autowired
    private ConfirmCodeService confirmCodeService;
    
    @Autowired
    private AppNotificationService notificationService;
    
    @GetMapping("")
    public String confirmCodeAdminPage(Model model, Principal principal, @PageableDefault(size = 20, sort = {"expirationAt"}, direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("principal", principal);
        model.addAttribute("codes", this.confirmCodeService.findAllConfirmCodesPage(pageable));
        model.addAttribute("newCode", new ConfirmCode());
        model.addAttribute("editCode", new ConfirmCode());
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        return "admin/confirmCode/confirmCode-table";
    }
    
}
