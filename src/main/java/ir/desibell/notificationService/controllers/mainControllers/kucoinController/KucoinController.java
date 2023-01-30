package ir.desibell.notificationService.controllers.mainControllers.kucoinController;

import ir.desibell.notificationService.processes.analysis.NotificationMechanism.MassNotification;
import ir.desibell.notificationService.services.subPanel.SubPanelService;
import ir.desibell.notificationService.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/kucoin")
public class KucoinController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubPanelService subPanelService;
    
    @Autowired
    private MassNotification m;

    @GetMapping("")
    @ResponseBody
    public String get24hrStats(Authentication authentication) throws Exception {
        m.sendAll();
        return "hello";
    }

}
