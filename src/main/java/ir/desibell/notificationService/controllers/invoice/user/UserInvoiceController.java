package ir.desibell.notificationService.controllers.invoice.user;

import ir.desibell.notificationService.entities.invoice.Invoice;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.enums.invoice.Status;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.invoice.InvoiceService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/invoice")
public class UserInvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private AppNotificationService appNotificationService;

    public UserInvoiceController(InvoiceService invoiceService, UserService userService) {
        this.invoiceService = invoiceService;
        this.userService = userService;
    }

    @GetMapping("/status")
    public String paymentStatus(@RequestParam Long invoiceNumber, Model model, Authentication authentication, Principal principal) throws IllegalAccessException, InvocationTargetException {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);

        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        Invoice invoice = this.invoiceService.findByInvoiceNumber(invoiceNumber);
        if (invoice != null && invoice.getUser().getId().equals(user.getId())) {
            invoice.updateStatus();
            this.invoiceService.registerInvoice(invoice);
            model.addAttribute("invoice", invoice);
        }
        
        return "invoice/subscribe-payment-invoice";
    }

    @GetMapping("/all")
    public String allUserInvoices(Model model, @PageableDefault(size = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication, Principal principal) throws IllegalAccessException, InvocationTargetException {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);
        
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        model.addAttribute("invoices", this.invoiceService.findAllUserInvoices(user, pageable));
        return "invoice/invoice-table";
    }

    @GetMapping("/cancel")
    public String invoiceCancellation(@RequestParam Long invoiceNumber,Authentication authentication) throws IllegalAccessException, InvocationTargetException {
        User user = (User) authentication.getPrincipal();
        Invoice invoice = this.invoiceService.findByInvoiceNumber(invoiceNumber);
        if(invoice != null && (invoice.getStatus() == Status.UNSUCCESSFUL || invoice.getStatus() == Status.WAITING)
                && user.getId().equals(invoice.getUser().getId())){
            invoice.setStatus(Status.EXPIRED);
            this.invoiceService.registerInvoice(invoice);
        }
        
        return "redirect:/invoice/all";
    }
    
}
