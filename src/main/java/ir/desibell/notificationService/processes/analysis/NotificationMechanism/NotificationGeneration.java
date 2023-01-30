package ir.desibell.notificationService.processes.analysis.NotificationMechanism;

import ir.desibell.notificationService.enums.analysis.Condition;
import ir.desibell.notificationService.processes.sms.SMSApi;
import java.lang.reflect.InvocationTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class NotificationGeneration {

    private NotificationTemplate notificationTemplate;
    private SMSApi smsApi;
    private String message;
    private Condition range;
    private Condition rate;
    private String smsNumber;

    public NotificationGeneration(NotificationTemplate notificationTemplate, SMSApi smsApi, String smsNumber) {
        this.notificationTemplate = notificationTemplate;
        this.message = notificationTemplate.getNotification();
        this.range = notificationTemplate.getRange();
        this.rate = notificationTemplate.getRate();
        this.smsApi = smsApi;
        this.smsNumber = smsNumber;
    }

    public void send(String numberPhone) throws IllegalAccessException, InvocationTargetException, Exception {
        if (isConfirm()) {
            sendSms(numberPhone);
        }
    }

    private String sendSms(String numberPhone) throws Exception {
        return smsApi.sendSMS(smsNumber, numberPhone, message, false);
    }

    private boolean isConfirm() throws IllegalAccessException, InvocationTargetException {
        return !message.isBlank() && (range != Condition.STABLE || rate != Condition.STABLE);
    }

    //--------------------------------------------------------------------------
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationTemplate getNotificationTemplate() {
        return notificationTemplate;
    }

    public void setNotificationTemplate(NotificationTemplate notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
    }

    public SMSApi getSmsApi() {
        return smsApi;
    }

    public void setSmsApi(SMSApi smsApi) {
        this.smsApi = smsApi;
    }

    public Condition getRange() {
        return range;
    }

    public void setRange(Condition range) {
        this.range = range;
    }

    public Condition getRate() {
        return rate;
    }

    public void setRate(Condition rate) {
        this.rate = rate;
    }
}
