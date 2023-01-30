package ir.desibell.notificationService.controllers.authority.admin;

import ir.desibell.notificationService.domain.authority.AuthorityDomain;
import ir.desibell.notificationService.entities.role.Role;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.role.RoleService;
import ir.desibell.notificationService.services.user.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/admin/authority")
public class AuthorityAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AppNotificationService notificationService;

    @GetMapping("")
    public String authorityAdminPage(Model model, Principal principal, @PageableDefault(size = 20) Pageable pageable) {
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        model.addAttribute("roles", this.roleService.findAllRoles());
        model.addAttribute("users", this.userService.findAllUsers());
        model.addAttribute("usersPage", this.userService.allUserWithRoles(pageable));
        model.addAttribute("newAuthority", new AuthorityDomain());
        model.addAttribute("deleteAuthority", new AuthorityDomain());
        
        return "admin/authority/authority-table";
    }
    
    @PostMapping("/register")
    public String registerRole(@ModelAttribute AuthorityDomain authorityDomain){
        this.userService.addAuthority(authorityDomain.getUserId(), this.roleService.findById(authorityDomain.getRoleId()).getName());
        return "redirect:/admin/authority";
    }

    @PostMapping("/delete")
    public String deleteRole(@ModelAttribute AuthorityDomain authorityDomain){
        this.userService.deleteAuthority(authorityDomain.getUserId(), this.roleService.findById(authorityDomain.getRoleId()).getName());
        return "redirect:/admin/authority";
    }

}
