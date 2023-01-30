package ir.desibell.notificationService.processes.sms;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;

@Component
public class SMSApi {

    @Value("${smsUsername}")
    private String username;
    
    @Value("${smsPassword}")
    private String password;
    
    @Value("${smsURL}")
    private String baseUrl;

    private final String SEND_SMS_URL = "SendSMS/SendSMS";
    private final String GET_DELIVERIES_URL = "SendSMS/GetDeliveries2";
    private final String GET_CREDIT_URL = "SendSMS/GetCredit";

    private enum HTTP_METHOD {
        GET, POST;
    }

    public SMSApi() {
    }

    private String doRequest(String url, Map<String, Object> bodyMap, HTTP_METHOD method) throws Exception {
        try {
            if (bodyMap == null) {
                bodyMap = new HashMap<>();
            }
            SMSHttpUtilManager httpUtilManager = new SMSHttpUtilManager();
            switch (method) {
                case GET:
                    return httpUtilManager.requestHttpGet(baseUrl, url, bodyMap).block();
                case POST:
                    return httpUtilManager.requestHttpPost(baseUrl, url, bodyMap).block();
                default:
                    return httpUtilManager.requestHttpGet(baseUrl, url, bodyMap).block();
            }
        } catch (WebClientException exception) {
            throw new Exception("melli payamak exeption request");
        }
    }

    public String sendSMS(String from, String to, String text, boolean isFlash) throws Exception {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("username", this.username);
        bodyMap.put("password", this.password);
        bodyMap.put("from", from);
        bodyMap.put("to", to);
        bodyMap.put("text", text);
        bodyMap.put("isFlash", isFlash);
        return doRequest(SEND_SMS_URL, bodyMap, HTTP_METHOD.POST);
    }

    public String getDeliveries(long recId) throws Exception {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserName", this.username);
        bodyMap.put("PassWord", this.password);
        bodyMap.put("recID", recId);
         return doRequest(GET_DELIVERIES_URL, bodyMap, HTTP_METHOD.POST);
    }
    
    public String getCredit() throws Exception{
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserName", this.username);
        bodyMap.put("PassWord", this.password);
        return doRequest(GET_CREDIT_URL, bodyMap, HTTP_METHOD.POST);
    }

}
