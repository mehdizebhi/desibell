package ir.desibell.notificationService.processes.analysis;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ir.desibell.notificationService.processes.kucoinApi.KucoinMarketDataApi;

public class DataExtraction {

    private KucoinMarketDataApi api;

    public DataExtraction(KucoinMarketDataApi api) {
        this.api = api;
    }

    //--------------------------------24hrStats---------------------------------
    public JsonObject get24hrStats(String symbol) throws Exception {
        return new Gson().fromJson(api.get24hrStats(symbol), JsonObject.class);
    }

    public JsonElement getCodeOf24hrStats(String symbol) throws Exception {
        final JsonObject stats = get24hrStats(symbol);
        return stats.get("code");
    }

    public JsonObject getDataOf24hrStats(String symbol) throws Exception {
        final JsonObject stats = get24hrStats(symbol);
        return stats.getAsJsonObject("data");
    }

    public JsonElement getElementOfDataOf24hrStats(String symbol, String nameOfElement) throws Exception {
        return getDataOf24hrStats(symbol).get(nameOfElement);
    }

    //----------------------------------Ticker----------------------------------
    public JsonObject getTicker(String symbol) throws Exception {
        return new Gson().fromJson(api.getTicker(symbol), JsonObject.class);
    }

    public JsonElement getCodeOfTicker(String symbol) throws Exception {
        final JsonObject ticker = getTicker(symbol);
        return ticker.get("code");
    }

    public JsonObject getDataOfTicker(String symbol) throws Exception {
        final JsonObject ticker = getTicker(symbol);
        return  ticker.getAsJsonObject("data");
    }

    public JsonElement getElementOfDataOfTicker(String symbol, String nameOfElement) throws Exception {
        return getDataOfTicker(symbol).get(nameOfElement);
    }

}
