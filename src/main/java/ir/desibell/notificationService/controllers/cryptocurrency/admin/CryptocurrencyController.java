package ir.desibell.notificationService.controllers.cryptocurrency.admin;

import com.google.gson.Gson;
import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.cryptocurrency.CryptocurrencyService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/admin/cryptocurrency")
public class CryptocurrencyController {
    
    @Autowired
    private CryptocurrencyService cryptocurrencyService;
    
    @Autowired
    private AppNotificationService notificationService;

    public CryptocurrencyController(CryptocurrencyService cryptocurrencyService) {
        this.cryptocurrencyService = cryptocurrencyService;
    }
    
    @GetMapping("")
    public String cryptocurrencyAdminPage(Model model, Principal principal, @PageableDefault(size = 20) Pageable pageable){
        model.addAttribute("principal", principal);
        model.addAttribute("CCs", this.cryptocurrencyService.findAllCryptocurrenciesPage(pageable));
        model.addAttribute("newCC", new Cryptocurrency());
        model.addAttribute("editCC", new Cryptocurrency());
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        return "admin/cryptocurrency/cryptocurrency-table";
    }
    
    @PostMapping("/register")
    public String registerCryptocurrency(@ModelAttribute Cryptocurrency cryptocurrency){
        this.cryptocurrencyService.registerCryptocurrency(cryptocurrency);
        return "redirect:/admin/cryptocurrency";
    }
    
    @PostMapping("/edit")
    public String editOperation(@ModelAttribute Cryptocurrency cryptocurrency){
        this.cryptocurrencyService.registerCryptocurrency(cryptocurrency);
        return "redirect:/admin/cryptocurrency";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteOperation(@PathVariable(name = "id") Long id){
        this.cryptocurrencyService.deleteById(id);
        return "redirect:/admin/cryptocurrency";
    }
}
