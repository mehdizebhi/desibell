package ir.desibell.notificationService.controllers.resetPasswordToken.admin;

import ir.desibell.notificationService.entities.resetPasswordToken.ResetPasswordToken;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.resetPasswordToken.ResetPasswordTokenService;
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
@RequestMapping(value = "/admin/resetPassword")
public class AdminResetPasswordTokenController {

    @Autowired
    private ResetPasswordTokenService tokenService;
    
    @Autowired
    private AppNotificationService notificationService;

    @GetMapping("")
    public String resetPasswordTokenAdminPage(Model model, Principal principal, @PageableDefault(size = 20, sort = {"expirationAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("principal", principal);
        model.addAttribute("RSTs", this.tokenService.findAllTokensPage(pageable));
        model.addAttribute("newRST", new ResetPasswordToken());
        model.addAttribute("editRST", new ResetPasswordToken());
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        return "admin/resetPasswordToken/resetPasswordToken-table";
    }
}
