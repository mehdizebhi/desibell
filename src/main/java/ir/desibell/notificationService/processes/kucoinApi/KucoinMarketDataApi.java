package ir.desibell.notificationService.processes.kucoinApi;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.reactive.function.client.WebClientException;

public class KucoinMarketDataApi {

    private String baseUrl;

    private final String GET_SYMBOLS_URL = "symbols";
    private final String GET_TICKER_URL = "market/orderbook/level1";            //with parameters : symbol
    private final String GET_ALL_TICKER_URL = "market/allTickers";
    private final String GET_24HR_STATS_URL = "market/stats";   //with parameters : symbol

    private enum HTTP_METHOD {
        GET, POST;
    }

    public KucoinMarketDataApi(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private String doRequest(String url, Map<String, String> parameters, HTTP_METHOD method) throws Exception {
        try {
            if (parameters == null) {
                parameters = new HashMap<>();
            }
            KucoinHttpUtilManager httpUtilManager = new KucoinHttpUtilManager();
            switch (method) {
                case GET:
                    return httpUtilManager.requestHttpGet(baseUrl, url, parameters).block();
                case POST:
                    return httpUtilManager.requestHttpPost(baseUrl, url, parameters).block();
                default:
                    return httpUtilManager.requestHttpGet(baseUrl, url, parameters).block();
            }
        } catch (WebClientException exception) {
            throw new Exception("Kucoin exeption request");
        }
    }
    
    /*
    public String getSymbolsList() throws Exception {
        return doRequest(GET_SYMBOLS_URL, null, HTTP_METHOD.GET);
    }*/
    
    /*public String getAllTickers() throws Exception{
        return doRequest(GET_ALL_TICKER_URL, null, HTTP_METHOD.GET);
    }*/

    public String getTicker(String symbol) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        return doRequest(GET_TICKER_URL, parameters, HTTP_METHOD.GET);
    }
    
    public String get24hrStats(String symbol) throws Exception{
        Map<String, String> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        return doRequest(GET_24HR_STATS_URL, parameters, HTTP_METHOD.GET);
    }

}
