package ir.desibell.notificationService.domain.mail;

import ir.desibell.notificationService.services.mail.MailServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailTemplateSender {
    
    @Value("${domain}")
    private String domain;

    private MailServiceImplementation mailService;

    @Autowired
    public MailTemplateSender(MailServiceImplementation mailService) {
        this.mailService = mailService;
    }

    public void resetPassword(String to, String token) {
        
        String htmlTemplate = MailHtmlTemplate.resetPasswordHtml(domain + "forgetPassword/changePassword?token=" + token);
        
        Mail mail = new Mail();
        mail.setMailFrom("desibell.ir@gmail.com");
        mail.setMailTo(to);
        mail.setMailSubject("دسی بل - بازنشانی رمز عبور");
        mail.setMailContent(htmlTemplate);
        
        mailService.sendEmail(mail);
    }

}
