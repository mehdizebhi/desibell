package ir.desibell.notificationService.controllers.mainControllers;

import ir.desibell.notificationService.domain.confirmPhone.ConfirmPhoneDomain;
import ir.desibell.notificationService.entities.confirmCode.ConfirmCode;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.services.confirmCode.ConfirmCodeService;
import ir.desibell.notificationService.services.security.SecurityService;
import ir.desibell.notificationService.services.subPanel.SubPanelService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/confirm")
public class ConfirmCodeController {

    @Autowired
    private ConfirmCodeService confirmCodeService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SubPanelService subPanelService;

    @Autowired
    private UserService userService;

    public ConfirmCodeController(ConfirmCodeService confirmCodeService, SecurityService securityService) {
        this.confirmCodeService = confirmCodeService;
        this.securityService = securityService;
    }

    @GetMapping("/subPanelPhone")
    public String subPanelconfirmPhonePage(@RequestParam("phone") String phoneNumber, Model model) {
        model.addAttribute("code", new ConfirmPhoneDomain(phoneNumber));
        model.addAttribute("error", false);
        return "main/confirm-phone";
    }

    @PostMapping("/subPanelPhone")
    public String subPanelConfirmPhone(@ModelAttribute(name = "code") ConfirmPhoneDomain code, @RequestParam("phone") String phoneNumber, Authentication authentication, Model model) throws InvocationTargetException, IllegalAccessException {
        User user = (User) authentication.getPrincipal();
        ConfirmCode cc = this.confirmCodeService.findByCodeAndPhoneNumber(code.getCode(), code.getPhoneNumber());
        if (cc != null && user.getId().equals(cc.getUser().getId())
                && this.securityService.isValidateConfirmCode(code.getCode(), code.getPhoneNumber())) {

            user = this.userService.findById(user.getId());
            SubPanel subPanel = this.subPanelService.findById(user.getSubPanel().getId());
            subPanel.setPhoneNumber(cc.getPhoneNumber());
            this.subPanelService.registerSubPanel(subPanel);
            this.confirmCodeService.updateUsedConfirmCode(cc.getId());
            return "redirect:/spanel";
        }
        
        model.addAttribute("error", true);
        return "main/confirm-phone";
    }
}
