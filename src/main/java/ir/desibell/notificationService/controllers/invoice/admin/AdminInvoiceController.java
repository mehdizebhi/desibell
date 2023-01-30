package ir.desibell.notificationService.controllers.invoice.admin;

import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.invoice.InvoiceService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/invoice")
public class AdminInvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AppNotificationService notificationService;

    @GetMapping("")
    public String invoiceAdminPage(Model model, Principal principal, @PageableDefault(size = 20) Pageable pageable) {
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.notificationService.findNumberOfNotifications(4));
        model.addAttribute("invoices", this.invoiceService.findAllInvoices(pageable));
        return "admin/invoice/invoice-table";
    }
    
}
