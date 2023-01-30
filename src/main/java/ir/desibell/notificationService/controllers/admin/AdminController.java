package ir.desibell.notificationService.controllers.admin;

import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.cryptocurrency.CryptocurrencyService;
import ir.desibell.notificationService.services.invoice.InvoiceService;
import ir.desibell.notificationService.services.subscribe.SubscribeService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AppNotificationService notificationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SubscribeService subscribeService;
    
    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    @GetMapping("/panel")
    public String contentsAdminPage(Model model, Principal principal) throws IllegalAccessException, InvocationTargetException {
        
        this.userService.updateCurrentSubscribeAllUser();
        
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        model.addAttribute("usersNumber", this.userService.numberOfUsers());
        model.addAttribute("subsNumber", this.subscribeService.numberOfSubs());
        model.addAttribute("TrialSubsNumber", this.userService.numberOfRoles("SUBTRIAL"));
        model.addAttribute("Tier1SubsNumber", this.userService.numberOfRoles("SUBTIER1"));
        model.addAttribute("Tier2SubsNumber", this.userService.numberOfRoles("SUBTIER2"));
        model.addAttribute("Tier3SubsNumber", this.userService.numberOfRoles("SUBTIER3"));
        model.addAttribute("currentSubsNumber", this.subscribeService.numberOfCurrentSubs());
        model.addAttribute("successfulInvoiceNum", this.invoiceService.successfulInvoicesNum());
        model.addAttribute("sumInvoicesAmount", this.invoiceService.sumOfAllAmount());
        model.addAttribute("cryptoNum", this.cryptocurrencyService.numberOfCryptocurrency());
        model.addAttribute("availableCryptoNum", this.cryptocurrencyService.numberOfAvailableCryptocurrency());
        
        return "admin/admin-panel";
    }

}
