package ir.desibell.notificationService.controllers.subscribe.admin;

import com.google.gson.Gson;
import ir.desibell.notificationService.entities.operation.Operation;
import ir.desibell.notificationService.entities.role.Role;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.operation.OperationService;
import ir.desibell.notificationService.services.role.RoleService;
import ir.desibell.notificationService.services.subscribe.TypeOfSubscribeService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping(value = "/admin/tos")
public class TypeOfSubscribeController {

    @Autowired
    private TypeOfSubscribeService typeOfSubscribeService;

    @Autowired
    private AppNotificationService notificationService;
    
    @Autowired
    private RoleService roleService;

    public TypeOfSubscribeController(TypeOfSubscribeService typeOfSubscribeService) {
        this.typeOfSubscribeService = typeOfSubscribeService;
    }

    @GetMapping("")
    public String typeOfSubscribesAdminPage(Model model, Principal principal, @PageableDefault(size = 20) Pageable pageable) {
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        model.addAttribute("toses", this.typeOfSubscribeService.findAllTypeOfSubscribesPage(pageable));
        model.addAttribute("newTos", new TypeOfSubscribe());
        model.addAttribute("editTos", new TypeOfSubscribe());
        model.addAttribute("roles", this.roleService.findAllRoles());
        return "admin/subscribe/typeOfSubscribe-table";
    }

    @PostMapping("/register")
    public String registerTypeOfSubscribe(@ModelAttribute TypeOfSubscribe typeOfSubscribe) {
        this.typeOfSubscribeService.registerTypeOfSubscribe(typeOfSubscribe);
        return "redirect:/admin/tos";
    }

    @PostMapping("/edit")
    public String editTypeOfSubscribe(@ModelAttribute TypeOfSubscribe typeOfSubscribe) {
        this.typeOfSubscribeService.registerTypeOfSubscribe(typeOfSubscribe);
        return "redirect:/admin/tos";
    }

    @GetMapping("/delete/{id}")
    public String deleteTypeOfSubscribe(@PathVariable(name = "id") Long id) {
        this.typeOfSubscribeService.deleteById(id);
        return "redirect:/admin/tos";
    }
}
