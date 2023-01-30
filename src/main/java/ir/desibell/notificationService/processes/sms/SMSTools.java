package ir.desibell.notificationService.processes.sms;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSTools {
    
    @Autowired
    private SMSApi smsApi;
    
    public SMSTools() {
    }
    
    public String inventory() throws Exception{
         final String response = smsApi.getCredit();
         final JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
         final JsonElement value = jsonObject.get("Value");
         return value.getAsString();
    }
    
}
