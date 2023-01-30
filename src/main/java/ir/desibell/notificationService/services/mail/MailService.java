package ir.desibell.notificationService.services.mail;

import ir.desibell.notificationService.domain.mail.Mail;

public interface MailService {
    
    public void sendEmail(Mail mail);
}
