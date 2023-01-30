package ir.desibell.notificationService.controllers.role.admin;

import com.google.gson.Gson;
import ir.desibell.notificationService.entities.role.Role;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.operation.OperationService;
import ir.desibell.notificationService.services.role.RoleService;
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
@RequestMapping(value = "/admin/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private AppNotificationService notificationService;
    
    @Autowired
    private OperationService operationService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
    @GetMapping("")
    public String roleAdminPage(Model model, Principal principal, @PageableDefault(size = 20) Pageable pageable){
        model.addAttribute("principal", principal);
        model.addAttribute("roles", this.roleService.findAllRolesPage(pageable));
        model.addAttribute("newRole", new Role());
        model.addAttribute("editRole", new Role());
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        model.addAttribute("operations", this.operationService.findAllOperations());
        return "admin/role/role-table";
    }
    

    @PostMapping("/register")
    public String registerRole(@ModelAttribute Role role){
        this.roleService.registerRole(role);
        return "redirect:/admin/role";
    }

    @PostMapping("/edit")
    public String editRole(@ModelAttribute Role role){
        this.roleService.registerRole(role);
        return "redirect:/admin/role";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable(name = "id") Long id){
        this.roleService.deleteById(id);
        return "redirect:/admin/role";
    }
    
}
