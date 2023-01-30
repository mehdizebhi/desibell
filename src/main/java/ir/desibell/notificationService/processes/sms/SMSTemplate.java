package ir.desibell.notificationService.processes.sms;

import ir.desibell.notificationService.config.commonMethod.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSTemplate {
    
    @Value("${smsNumber}")
    private String from;
    
    @Autowired
    private SMSApi smsApi;

    public SMSTemplate() {
    }
    
    public void welcome (String to) throws Exception{
        final String text = "به «دسی بل» خوش آمدید" + "\n" + "جهت خرید اشتراک می توانید همین الان اقدام کنید";
        smsApi.sendSMS(this.from, to, text, false);
    }
    
    public void successfulPurchase(String to, String name, String invoiceCode) throws Exception{
        final String invoiceCodePersianNumber = CommonMethod.changeToPersianNumber(invoiceCode);
        final String text = "سلام " + name + " عزیز " + "\n" + "پرداخت شما با کد پیگیری " + invoiceCodePersianNumber + " دریافت شد " + "\n" +
                "از همین لحظه اشتراک برای شما فعال شد" + "\n" + "«دسی بل»";
        smsApi.sendSMS(this.from, to, text, false);
    }
    
    public void successfulActiveSubscribe(String to, String name, String subscribeName) throws Exception{
        final String text = "سلام " + name + " عزیز " + "\n" + "اشتراک " + subscribeName + "برای شما فعال شد " + "\n" +
                 "«دسی بل»";
        smsApi.sendSMS(this.from, to, text, false);
    }
    
    public void sendConfirmCode(String to, String code) throws Exception{
        final String text = "دسی بل" + "\n" + "کد تایید شما: " + code;
        smsApi.sendSMS(this.from, to, text, false);
    }
    
}
