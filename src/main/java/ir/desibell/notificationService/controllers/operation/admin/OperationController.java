package ir.desibell.notificationService.controllers.operation.admin;

import com.google.gson.Gson;
import ir.desibell.notificationService.entities.operation.Operation;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.operation.OperationService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/operation")
public class OperationController {
    
    @Autowired
    private OperationService operationService;
    
    @Autowired
    private AppNotificationService notificationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }
    
    @GetMapping("")
    public String operationsAdminPage(Model model, Principal principal, @PageableDefault(size = 20) Pageable pageable){
        model.addAttribute("principal", principal);
        model.addAttribute("operations", this.operationService.findAllOperationsPage(pageable));
        model.addAttribute("newOperation", new Operation());
        model.addAttribute("editOperation", new Operation());
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        return "admin/operation/operation-table";
    } 
    
    @PostMapping("/register")
    public String registerOperation(@ModelAttribute Operation operation){
        this.operationService.registerOperation(operation);
        return "redirect:/admin/operation";
    }
    
    @PostMapping("/edit")
    public String editOperation(@ModelAttribute Operation operation){
        this.operationService.registerOperation(operation);
        return "redirect:/admin/operation";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteOperation(@PathVariable(name = "id") Long id){
        this.operationService.deleteById(id);
        return "redirect:/admin/operation";
    }
}
