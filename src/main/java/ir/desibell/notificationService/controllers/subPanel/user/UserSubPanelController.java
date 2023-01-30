package ir.desibell.notificationService.controllers.subPanel.user;

import ir.desibell.notificationService.config.commonMethod.CommonMethod;
import ir.desibell.notificationService.entities.confirmCode.ConfirmCode;
import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.processes.sms.SMSTemplate;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.confirmCode.ConfirmCodeService;
import ir.desibell.notificationService.services.cryptocurrency.CryptocurrencyService;
import ir.desibell.notificationService.services.data.DataService;
import ir.desibell.notificationService.services.subPanel.SubPanelService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/spanel")
public class UserSubPanelController {

    @Autowired
    private SubPanelService subPanelService;

    @Autowired
    private UserService userService;

    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    @Autowired
    private DataService dataService;

    @Autowired
    private AppNotificationService appNotificationService;

    @Autowired
    private ConfirmCodeService confirmCodeService;
    
    @Autowired
    private SMSTemplate smst;

    public UserSubPanelController(SubPanelService subPanelService, UserService userService, CryptocurrencyService cryptocurrencyService, DataService dataService, AppNotificationService appNotificationService, ConfirmCodeService confirmCodeService, SMSTemplate smst) {
        this.subPanelService = subPanelService;
        this.userService = userService;
        this.cryptocurrencyService = cryptocurrencyService;
        this.dataService = dataService;
        this.appNotificationService = appNotificationService;
        this.confirmCodeService = confirmCodeService;
        this.smst = smst;
    }

    @GetMapping("")
    public String subPanelPage(Model model, @PageableDefault(size = 10) Pageable pageable,
            Authentication authentication, Principal principal, @RequestParam(required = false) String error) throws IllegalAccessException, InvocationTargetException {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);
        SubPanel subPanel = this.subPanelService.findById(user.getSubPanel().getId());
        if (subPanel.getData() == null) {
            subPanel.setData(new ArrayList<>());
        }
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        model.addAttribute("subPanel", subPanel);
        model.addAttribute("data", this.dataService.findAllSubPanelData(subPanel, pageable));
        model.addAttribute("dataObject", new Data(subPanel));
        model.addAttribute("dataEditObject", new Data(subPanel));
        model.addAttribute("cryptocurrencies", this.cryptocurrencyService.findAllCryptocurrencies());
        model.addAttribute("error", error);
        return "subPanel/sub-panel";
    }

    @PostMapping("/verify-phone")
    public String phoneVerification(@ModelAttribute SubPanel subPanel, Authentication authentication, RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException, Exception {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);
        Long userId = this.subPanelService.findById(subPanel.getId()).getUser().getId();

        SubPanel existSubPanel = this.subPanelService.findByPhoneNumber(subPanel.getPhoneNumber());

        if (userId.equals(user.getId()) && subPanel.getPhoneNumber().length() == 11 && subPanel.getPhoneNumber().startsWith("09")
                && user.isActiveCurrentSubscribe()
                && existSubPanel == null) {

            String code = CommonMethod.generatedRandomLong(10000, 99999).toString();
            ConfirmCode cc = this.confirmCodeService.registerConfirmCodeByUser(user, code, subPanel.getPhoneNumber());

            smst.sendConfirmCode(subPanel.getPhoneNumber(), code);

            redirectAttributes.addAttribute("phone", cc.getPhoneNumber());
            return "redirect:/confirm/subPanelPhone";
        } else if (existSubPanel != null && !existSubPanel.getUser().getId().equals(user.getId())) {
            redirectAttributes.addAttribute("error", "existNumber");
        }

        return "redirect:/spanel";

    }

    @PostMapping("/verify-data")
    public String dataVerification(@ModelAttribute(name = "dataObject") Data dataObject, Authentication authentication,
            RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);

        SubPanel subPanel = this.subPanelService.findById(dataObject.getSubPanel().getId());
        if (this.subPanelService.verifyData(subPanel, user, dataObject)) {
            dataObject = this.dataService.registerData(dataObject);
            subPanel.addData(dataObject);
            subPanel.addAvailableCryptocurrencies(dataObject.getCryptocurrency());
            this.subPanelService.registerSubPanel(subPanel);
            redirectAttributes.addAttribute("error", "verifydata");
        } else {
            redirectAttributes.addAttribute("error", "cantVerifydata");
        }

        return "redirect:/spanel";
    }

    @GetMapping("/delete-data")
    public String deleteData(@RequestParam Long dataId, Authentication authentication, RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException {
        User user = (User) authentication.getPrincipal();
        Data data = this.dataService.findById(dataId);
        SubPanel subPanel = this.subPanelService.findById(data.getSubPanel().getId());
        if (user.getId().equals(subPanel.getUser().getId())) {
            this.dataService.deleteById(dataId);
            subPanel.deleteCryptocurrencyOnAvailableCryptocurrencies(data.getCryptocurrency());
            this.subPanelService.registerSubPanel(subPanel);
            redirectAttributes.addAttribute("error", "verifyDelete");
        } else {
            redirectAttributes.addAttribute("error", "cantVerifyDelete");
        }
        return "redirect:/spanel";
    }

    @PostMapping("/edit-data")
    public String editData(@ModelAttribute(name = "dataEditObject") Data dataEditObject, Authentication authentication,
            RedirectAttributes redirectAttributes) throws IllegalAccessException, InvocationTargetException {
        User user = (User) authentication.getPrincipal();
        user = this.userService.findById(user.getId());
        user = this.userService.updateCurrentSubscribe(user);

        SubPanel subPanel = this.subPanelService.findById(dataEditObject.getSubPanel().getId());
        Data existData = this.dataService.findById(dataEditObject.getId());

        if (existData != null && existData.getCryptocurrency().equals(dataEditObject.getCryptocurrency())
                && subPanel.getUser().getId().equals(user.getId()) && user.isActiveCurrentSubscribe()
                && dataEditObject.getSubPanel().getId().equals(subPanel.getId())) {

            dataEditObject = this.dataService.registerData(dataEditObject);
            subPanel.deleteData(existData);
            subPanel.addData(dataEditObject);
            this.subPanelService.registerSubPanel(subPanel);
            redirectAttributes.addAttribute("error", "verifyEdit");
        } else {
            redirectAttributes.addAttribute("error", "cantVerifyEdit");
        }
        return "redirect:/spanel";
    }

    @PostMapping("/enabled")
    public String enabledSubPanel(@ModelAttribute("subPanel") SubPanel subPanel) throws IllegalAccessException, InvocationTargetException {
        boolean enabled = subPanel.isEnabled();
        this.subPanelService.updateEnabled(subPanel, enabled);
        return "redirect:/spanel";
    }

}
