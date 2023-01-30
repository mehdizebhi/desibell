package ir.desibell.notificationService.processes.payment;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

public class PaymentApi {

    private String accessToken;
    private String baseUrl;

    private final String PAY_TOKEN_ULR = "pay";
    private final String PAY_VERIFY_URL = "pay/verify";

    private enum HTTP_METHOD {
        GET, POST, DELETE, PUT;
    }

    public PaymentApi(String baseUrl, String accessToken) {
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
    }

    private String doRequest(String url, Map<String, Object> bodyMap, HTTP_METHOD method) throws Exception {
        try {
            if (bodyMap == null) {
                bodyMap = new HashMap<>();
            }
            String authorization = "Bearer " + this.accessToken;
            HttpUtilManager httpUtilManager = new HttpUtilManager();
            switch (method) {
                case GET:
                    return httpUtilManager.requestHttpGet(baseUrl, url, authorization, bodyMap).block();
                case POST:
                    return httpUtilManager.requestHttpPost(baseUrl, url, authorization, bodyMap).block();
                default:
                    return httpUtilManager.requestHttpGet(baseUrl, url, authorization, bodyMap).block();
            }
        } catch (WebClientException exception) {
            throw new Exception("bad request");
        }
    }

    public String createPayToken(int amount, String payerIdentity, String payerName, String description, String returnUrl, String clientRefId) throws Exception {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("amount", amount);
        bodyMap.put("payerIdentity", payerIdentity);
        bodyMap.put("payerName", payerName);
        bodyMap.put("description", description);
        bodyMap.put("returnUrl", returnUrl);
        bodyMap.put("clientRefId", clientRefId);
        return doRequest(PAY_TOKEN_ULR, bodyMap, HTTP_METHOD.POST);
    }

    public String verifyPay(String refId, int amount) throws Exception {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("refId", refId);
        bodyMap.put("amount", amount);
        return doRequest(PAY_VERIFY_URL, bodyMap, HTTP_METHOD.POST);
    }

}
