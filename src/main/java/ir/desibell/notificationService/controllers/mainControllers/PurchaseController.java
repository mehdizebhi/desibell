package ir.desibell.notificationService.controllers.mainControllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ir.desibell.notificationService.config.commonMethod.CommonMethod;
import ir.desibell.notificationService.entities.invoice.Invoice;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.enums.invoice.Status;
import ir.desibell.notificationService.processes.payment.PaymentApi;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.invoice.InvoiceService;
import ir.desibell.notificationService.services.subscribe.SubscribeService;
import ir.desibell.notificationService.services.subscribe.TypeOfSubscribeService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/purchase")
public class PurchaseController {
    
    @Value("${domain}")
    private String domain;
    
    @Value("${paymentKey}")
    private String paymentKey; 
    
    @Value("${peymentURL}")
    private String baseURL;

    @Autowired
    private TypeOfSubscribeService typeOfSubscribeService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private AppNotificationService appNotificationService;

    public PurchaseController(TypeOfSubscribeService typeOfSubscribeService, InvoiceService invoiceService, UserService userService, SubscribeService subscribeService) {
        this.typeOfSubscribeService = typeOfSubscribeService;
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.subscribeService = subscribeService;
    }

    @GetMapping("/offer/{subName}")
    public String buySubscribe(Model model, Authentication authentication, Principal principal,
            @PathVariable(name = "subName") String subscribeName) {
        model.addAttribute("user", (User) authentication.getPrincipal());
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        model.addAttribute("tos", this.typeOfSubscribeService.findByName(subscribeName));
        return "subscribe/subscribe-invoice";
    }

    @PostMapping("/create-pay")
    public String createPay(Authentication authentication, @ModelAttribute(name = "tos") TypeOfSubscribe tos, RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException, Exception {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);
        
        List<Invoice> is = this.invoiceService.findUnsuccessfulInvoiceByUserAndTypeOfSubscribe(user, tos);
        if(is.size() != 0){
            Invoice i = is.get(0);
            redirectAttributes.addAttribute("invoiceNumber", i.getInvoiceNumber());
            return "redirect:/invoice/status";
        }
        
        if (!user.isActiveCurrentSubscribe()) {
            tos = this.typeOfSubscribeService.findById(tos.getId());
            final PaymentApi paymentApi = new PaymentApi(baseURL, paymentKey);

            final int amount = tos.getPrice().intValue();
            final String payerIdentity = user.getNumber();
            final String payerName = user.getName();
            final String description = "خرید اشتراک";
            final String returnUrl = domain + "purchase/return";
            final String clientRefId = user.getNumber();

            final String jsonResponse = paymentApi.createPayToken(amount, payerIdentity, payerName, description, returnUrl, clientRefId);
            final JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
            final JsonElement code = jsonObject.get("code");
            final String redirectUrl = baseURL + "pay/gotoipg/" + code.getAsString();

            final Long invoiceNumber = CommonMethod.generatedRandomLong(100000, 999999);
            if (invoiceNumber != 0 && this.invoiceService.findByInvoiceNumber(invoiceNumber) == null) {
                final Invoice invoice = new Invoice(invoiceNumber, user, LocalDateTime.now().plusDays(2),
                        Status.WAITING, code.getAsString(), tos.getPrice(), description, clientRefId, tos);
                this.invoiceService.registerInvoice(invoice);
            }

            return "redirect:" + redirectUrl;
        }
        return "redirect:/purchase/offer/" + tos.getName();
    }

    @RequestMapping(value = "/return")
    public String extractReturnPaymentData(@RequestBody String requestBodyData, RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException {
        String[] dataSets = requestBodyData.split("&");
        Map<String, Object> paymentDataMap = new HashMap<>();
        for (String data : dataSets) {
            String[] keyValue = data.split("=");

            String key = null;
            String value = null;

            if (keyValue.length == 1) {
                key = keyValue[0];
            } else if (keyValue.length == 2) {
                key = keyValue[0];
                value = keyValue[1];
            }
            paymentDataMap.put(key, value);
        }

        Invoice invoice = this.invoiceService.findByCode((String) paymentDataMap.get("code"));
        if (invoice != null) {
            invoice.setRefId((String) paymentDataMap.get("refid"));
            if (paymentDataMap.size() > 3) {
                invoice.setCardNumber((String) paymentDataMap.get("cardnumber"));
                invoice.setCardHashPan((String) paymentDataMap.get("cardhashpan"));
            }
            this.invoiceService.registerInvoice(invoice);
        }

        final String jsonPaymentData = new Gson().toJson(paymentDataMap);
        redirectAttributes.addAttribute("jsonPaymentData", jsonPaymentData);
        return "redirect:/purchase/verify";
    }

    @GetMapping("/verify")
    public String verifyReturnPayment(String jsonPaymentData, Authentication authentication, RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException, Exception {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);

        final JsonObject jsonObject = new Gson().fromJson(jsonPaymentData, JsonObject.class);
        final String refId = jsonObject.get("refid").getAsString();
        Invoice invoice = this.invoiceService.findByCode(jsonObject.get("code").getAsString());
        invoice.updateStatus();

        if (invoice != null && !invoice.getRefId().equals("1") && invoice.getRefId().equals(refId)
                && user.getId().equals(invoice.getUser().getId()) && !user.isActiveCurrentSubscribe()
                && (invoice.getStatus() == Status.WAITING || invoice.getStatus() == Status.UNSUCCESSFUL)) {

            try {
                final PaymentApi paymentApi = new PaymentApi(baseURL, paymentKey);
                final int amount = invoice.getAmount().intValue();

                final String jsonResponse = paymentApi.verifyPay(refId, amount);
                final JsonObject jsonResponseObject = new Gson().fromJson(jsonResponse, JsonObject.class);
                final JsonElement cardNumber = jsonResponseObject.get("cardNumber");
                final JsonElement cardHashPan = jsonResponseObject.get("cardHashPan");

                invoice.setCardNumber(cardNumber.isJsonNull() ? null : cardNumber.getAsString());
                invoice.setCardHashPan(cardHashPan.isJsonNull() ? null : cardHashPan.getAsString());
                invoice.setStatus(Status.SUCCESSFUL);
                this.invoiceService.registerInvoice(invoice);

                redirectAttributes.addAttribute("invoiceNumber", invoice.getInvoiceNumber());
                return "redirect:/sub/create";

            } catch (Exception e) {
                invoice.setStatus(Status.UNSUCCESSFUL);
            }

        } else if (invoice.getStatus() == Status.WAITING) {
            invoice.setStatus(Status.UNSUCCESSFUL);
        }

        this.invoiceService.registerInvoice(invoice);
        redirectAttributes.addAttribute("invoiceNumber", invoice.getInvoiceNumber());
        return "redirect:/invoice/status";
    }

    @PostMapping("/repay")
    public String repayInvoice(Authentication authentication, @ModelAttribute(name = "invoice") Invoice invoice,
            RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);
        invoice = this.invoiceService.findById(invoice.getId());
        invoice.updateStatus();
        if (invoice != null && user.getId().equals(invoice.getUser().getId()) && !user.isActiveCurrentSubscribe()
                && (invoice.getStatus() == Status.UNSUCCESSFUL || invoice.getStatus() == Status.WAITING)) {

            final String redirectUrl = baseURL + "pay/gotoipg/" + invoice.getCode();
            return "redirect:" + redirectUrl;
        }
        this.invoiceService.registerInvoice(invoice);
        redirectAttributes.addAttribute("invoiceNumber", invoice.getInvoiceNumber());
        return "redirect:/invoice/status";
    }

}
