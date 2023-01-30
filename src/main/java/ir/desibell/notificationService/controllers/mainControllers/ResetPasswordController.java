package ir.desibell.notificationService.controllers.mainControllers;

import ir.desibell.notificationService.domain.mail.MailTemplateSender;
import ir.desibell.notificationService.domain.resetPassword.ResetPasswordDomain;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.services.resetPasswordToken.ResetPasswordTokenService;
import ir.desibell.notificationService.services.security.SecurityService;
import ir.desibell.notificationService.services.user.UserService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/forgetPassword")
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private MailTemplateSender mailTemplate;

    @GetMapping("")
    public String resetPasswordPage(Model model) {
        model.addAttribute("resetPasswordDomain", new ResetPasswordDomain());
        return "main/auth-resetpassword";
    }

    @PostMapping("/reset")
    public String generateResetPasswordToken(HttpServletRequest request, Model model,
            @ModelAttribute(name = "resetPasswordDomain") ResetPasswordDomain resetPasswordDomain) {
        User user = this.userService.findByEmail(resetPasswordDomain.getEmail());
        if (user == null) {
            model.addAttribute("error", "invalidUser");
            return "main/auth-resetpassword";
        } else {
            final String token = UUID.randomUUID().toString();
            this.resetPasswordTokenService.registerTokenByUser(user, token);
            mailTemplate.resetPassword(user.getEmail(), token);
        }
        return "main/auth-confirm";
    }

    @GetMapping("/changePassword")
    public String changePasswordPage(@RequestParam(name = "token") String token, Model model) {
        if (this.securityService.isValidResetPasswordToken(token)) {
            User user = this.userService.findById(
                    this.resetPasswordTokenService.findByToken(token).getUser().getId());
            model.addAttribute("token", token);
            model.addAttribute("user", user);
            return "main/auth-changePassword";
        }
        return "main/auth-unsuccessful";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam(name = "token") String token, @ModelAttribute User user, Model model) {
        if (this.userService.doesCanResetPassword(user, token)) {
            this.resetPasswordTokenService.updateUsedToken(token);
            this.userService.updatePasswordOfUser(user.getId(), user.getPassword());
            return "main/auth-successful";
        } else {
            model.addAttribute("error", this.userService.whyCantResetPassword(user, token));
        }
        model.addAttribute("token", token);
        return "main/auth-changePassword";
    }

}
