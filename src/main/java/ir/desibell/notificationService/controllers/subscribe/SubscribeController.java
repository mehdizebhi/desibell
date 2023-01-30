package ir.desibell.notificationService.controllers.subscribe;

import com.google.gson.Gson;
import ir.desibell.notificationService.entities.subscribe.Subscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.services.subscribe.SubscribeService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/subscribe")
public class SubscribeController {
    
    @Autowired
    private SubscribeService subscribeService;
    
    @Autowired
    private UserService userService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }
    
    @GetMapping("/show")
    @ResponseBody
    public String showSubscribes(){
        return this.subscribeService.findAllSubscribes().toString();
    }
    
    @GetMapping("/register")
    public String registerSubscribePage(Model model){
        model.addAttribute("subscribe", new Subscribe());
        return "redirect:/subscribe/show";
    }
    
    @PostMapping("/register")
    public String registerSubscribe(@ModelAttribute Subscribe subscribe) throws IllegalAccessException, InvocationTargetException{
        subscribe.setBeginningtAt(LocalDateTime.now());
        subscribe.setExpirationAt(subscribe.getBeginningtAt().plusDays(subscribe.getTypeOfSubscribe().getPeriod()));
        subscribe.setExpired(false);
        this.subscribeService.registerSubscribe(subscribe);
        this.userService.addSubscribeOfUser(subscribe.getUser().getId(), subscribe);
        return "redirect:/";
    }
}
