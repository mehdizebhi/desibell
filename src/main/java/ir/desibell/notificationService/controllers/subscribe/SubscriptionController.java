package ir.desibell.notificationService.controllers.subscribe;

import ir.desibell.notificationService.entities.invoice.Invoice;
import ir.desibell.notificationService.entities.subscribe.Subscribe;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.enums.invoice.Status;
import ir.desibell.notificationService.processes.sms.SMSTemplate;
import ir.desibell.notificationService.services.invoice.InvoiceService;
import ir.desibell.notificationService.services.subscribe.SubscribeService;
import ir.desibell.notificationService.services.subscribe.TypeOfSubscribeService;
import ir.desibell.notificationService.services.user.UserService;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/sub")
public class SubscriptionController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private SMSTemplate smst;

    @Autowired
    private TypeOfSubscribeService typeOfSubscribeService;

    public SubscriptionController(InvoiceService invoiceService, SubscribeService subscribeService, UserService userService, SMSTemplate smst, TypeOfSubscribeService typeOfSubscribeService) {
        this.invoiceService = invoiceService;
        this.subscribeService = subscribeService;
        this.userService = userService;
        this.smst = smst;
        this.typeOfSubscribeService = typeOfSubscribeService;
    }

    @GetMapping(value = "")
    public String subPage() {
        return "subscribe/page-subscribe";
    }

    @GetMapping("/create")
    public String createNewSubscribe(Long invoiceNumber, RedirectAttributes redirectAttributes) throws Exception {
        Invoice invoice = this.invoiceService.findByInvoiceNumber(invoiceNumber);
        User user = invoice.getUser();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);

        if (invoice != null && !user.isActiveCurrentSubscribe() && invoice.getStatus() == Status.SUCCESSFUL) {
            final TypeOfSubscribe tos = invoice.getTypeOfSubscribe();
            final LocalDateTime beginningtAt = LocalDateTime.now();
            final LocalDateTime expirationAt = beginningtAt.plusDays(tos.getPeriod());

            Subscribe subscribe = new Subscribe(tos, user, false, beginningtAt, expirationAt, invoice);
            invoice.setSubscribe(subscribe);

            this.subscribeService.registerSubscribe(subscribe);
            this.userService.addSubscribeOfUser(user.getId(), subscribe);

            smst.successfulPurchase(invoice.getUser().getNumber(), invoice.getUser().getName(), String.valueOf(invoice.getInvoiceNumber()));

        }
        redirectAttributes.addAttribute("invoiceNumber", invoice.getInvoiceNumber());
        return "redirect:/invoice/status";
    }

    @PostMapping("/createSubTrial")
    public String createNewTrialSubscribe(Authentication authentication, RedirectAttributes redirectAttributes) throws Exception {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);

        TypeOfSubscribe typeOfSubscribe = this.typeOfSubscribeService.findByName("SUBTRIAL");
        List<Subscribe> subscribes = this.subscribeService.findSubscribeByUserAndTypeOfSubscribes(user, typeOfSubscribe);
        if (!user.isActiveCurrentSubscribe() && subscribes.size() == 0) {
            final TypeOfSubscribe tos = typeOfSubscribe;
            final LocalDateTime beginningtAt = LocalDateTime.now();
            final LocalDateTime expirationAt = beginningtAt.plusDays(tos.getPeriod());

            Subscribe subscribe = new Subscribe(tos, user, false, beginningtAt, expirationAt, null);

            this.subscribeService.registerSubscribe(subscribe);
            this.userService.addSubscribeOfUser(user.getId(), subscribe);

            smst.successfulActiveSubscribe(user.getNumber(), user.getName(), " رایگان ");

            redirectAttributes.addAttribute("notif", "subTrialAccepted");
        } else {
            redirectAttributes.addAttribute("notif", "subTrialNotAccepted");
        }
        return "redirect:/profile";
    }

}
