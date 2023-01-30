package ir.desibell.notificationService.controllers.mainControllers;

import com.google.common.hash.Hashing;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.jwt.domain.AuthenticationRequest;
import ir.desibell.notificationService.jwt.jwtUtil.JwtUtil;
import ir.desibell.notificationService.services.appNotification.AppNotificationService;
import ir.desibell.notificationService.services.role.RoleService;
import ir.desibell.notificationService.services.subPanel.SubPanelService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubPanelService subPanelService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtil jwtUtilToken;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppNotificationService appNotificationService;

    @GetMapping(value = {"", "/", "index"})
    public String index(Model model, Principal principal) throws IllegalAccessException, InvocationTargetException {
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        return "main/index";
    }

    @GetMapping("/login")
    public String loginPage(Model model, Principal principal, @RequestParam(name = "error", required = false) String error) {
        if (principal != null) {
            return "redirect:/profile";
        }
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        model.addAttribute("error", error);
        return "main/auth-login";
    }

    @PostMapping("/auth")
    public String createAuthenticationToken(@ModelAttribute AuthenticationRequest authenticationRequest, RedirectAttributes redirectAttributes,
            HttpServletResponse response, @RequestParam(name = "callback", required = false) String callback) throws Exception {
        try {

            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getNumber(),
                    authenticationRequest.getPassword()));

        } catch (BadCredentialsException e) {
            //throw new Exception("invalid number or password", e);
            redirectAttributes.addAttribute("error", "invalid");
            return "redirect:/login";
        }

        final User user = this.userService.findByNumber(authenticationRequest.getNumber());
        if (this.userService.isMatchPassword(authenticationRequest.getPassword(), user.getPassword())) {
            this.userService.updateCurrentSubscribe(user);
            final String jwt = this.jwtUtilToken.generatedToken(user);

            response.addHeader("Authorization", "Bearer " + jwt);

            Cookie jwtCookie = new Cookie("access_token", jwt);
            jwtCookie.setMaxAge(43200);
            response.addCookie(jwtCookie);

            final String credential = jwt + "TRUE";
            final String credentialToken = Hashing.sha256()
                    .hashString(credential, StandardCharsets.UTF_8)
                    .toString();
            Cookie credentialCookie = new Cookie("crt", credentialToken);
            credentialCookie.setMaxAge(43200);
            response.addCookie(credentialCookie);

            if (callback.equals("null") || callback.contains("forgetPassword")) {
                callback = "";
            }
            return "redirect:" + callback;
        }
        redirectAttributes.addAttribute("error", "invalid");
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/profile";
        }
        model.addAttribute("user", new User());
        return "main/auth-register";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute(name = "user") User user, Model model) throws IllegalAccessException, InvocationTargetException {
        if (this.userService.doesCanRegister(user)) {

            user.addRole(this.roleService.findByName("USER"));
            user = this.userService.registerUser(user);

            SubPanel subPanel = new SubPanel(user, true);
            subPanel = this.subPanelService.registerSubPanel(subPanel);

            user.setSubPanel(subPanel);
            this.userService.updateExistUser(user);

            return "redirect:/login";

        } else {
            model.addAttribute("error", this.userService.whyUserCantRegistered(user));
        }
        return "main/auth-register";
    }

    @RequestMapping(value = "access-denied")
    public String accessDeniedPage() {
        return "error/accessDenied";
    }

    @GetMapping("/get-start")
    public String getStartingPage(Model model, Principal principal) {
        model.addAttribute("principal", principal);
        model.addAttribute("notifications", this.appNotificationService.findNumberOfNotifications(4));
        return "main/get-starting";
    }

}
