package ir.desibell.notificationService.controllers.mainControllers.smsController;

import ir.desibell.notificationService.config.commonMethod.CommonMethod;
import ir.desibell.notificationService.processes.sms.SMSApi;
import ir.desibell.notificationService.processes.sms.SMSTemplate;
import ir.desibell.notificationService.processes.sms.SMSTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/sms")
public class SMSController {
    
    @Value("${smsNumber}")
    private String smsNumber;
    
    @Value("${smsURL}")
    private String smsUrl;
    
    @Autowired
    SMSApi smsApi;
    
    @GetMapping("/sendSMS")
    @ResponseBody
    public String sendSMS() throws Exception{
        return smsApi.sendSMS(smsNumber, "09021519966", "hello", false);
    }
    
}
