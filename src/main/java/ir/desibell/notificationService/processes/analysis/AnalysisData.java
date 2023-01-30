package ir.desibell.notificationService.processes.analysis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.enums.analysis.Condition;

public class AnalysisData {

    private Data data;
    private JsonObject stats24hr;
    private JsonElement code;
    private JsonObject dataStats;
    private final Double orangeRange = 0.20;

    public AnalysisData(Data data, JsonObject stats24hr) {
        this.data = data;
        this.stats24hr = stats24hr;
        this.code = stats24hr.get("code");
        this.dataStats = stats24hr.getAsJsonObject("data");
    }

    //--------------------------------------------------------------------------
    public Condition checkForChangesInSafeRange() {
        final Double rightRange = Double.valueOf(this.data.getValueOfNameValue("Highest"));
        final Double leftRange = Double.valueOf(this.data.getValueOfNameValue("Lowest"));
        final Double differenceRange = (rightRange - leftRange) * this.orangeRange;
        final Double lastPrice = Double.valueOf(this.last());

        if (lastPrice >= rightRange - differenceRange && lastPrice <= rightRange) {
            return Condition.POSITIVE;
        } else if (lastPrice > rightRange && lastPrice <= rightRange + differenceRange) {
            return Condition.POSITIVE_PLUS;
        } else if (lastPrice > rightRange + differenceRange) {
            return Condition.POSITIVE_PLUS_PLUS;
        } else if (lastPrice <= leftRange + differenceRange && lastPrice >= leftRange) {
            return Condition.NEGATIVE;
        } else if (lastPrice < leftRange && lastPrice >= leftRange - differenceRange) {
            return Condition.NEGATIVE_PLUS;
        } else if (lastPrice < leftRange - differenceRange) {
            return Condition.NEGATIVE_PLUS_PLUS;
        }
        return Condition.STABLE;
    }

    public Condition checkForChangesInSafeRate() {
        final Double maxChange = Double.valueOf(this.data.getValueOfNameValue("MaxChange"));
        final Double minChange = Double.valueOf(this.data.getValueOfNameValue("MinChange"));
        final Double changeRate = Double.valueOf(this.changeRate()) * 100.0;
        Double positiveDifferenceRate = maxChange * this.orangeRange;
        Double negativeDifferenceRate = -1.0 * minChange * this.orangeRange;

        if (changeRate >= maxChange - positiveDifferenceRate && changeRate <= maxChange) {
            return Condition.POSITIVE;
        } else if (changeRate > maxChange && changeRate <= maxChange + positiveDifferenceRate) {
            return Condition.POSITIVE_PLUS;
        } else if (changeRate > maxChange + positiveDifferenceRate) {
            return Condition.POSITIVE_PLUS_PLUS;
        } else if (changeRate <= minChange + negativeDifferenceRate && changeRate >= minChange) {
            return Condition.NEGATIVE;
        } else if (changeRate < minChange && changeRate >= minChange - negativeDifferenceRate) {
            return Condition.NEGATIVE_PLUS;
        } else if (changeRate < minChange - negativeDifferenceRate){
            return Condition.NEGATIVE_PLUS_PLUS;
        }
        return Condition.STABLE;
    }

    //--------------------------------------------------------------------------
    public boolean checkOfCode() {
        if (code.getAsString().equals("200000")) {
            return true;
        }
        return false;
    }

    public String last() {
        return dataStats.get("last").getAsString();
    }

    public String buy() {
        return dataStats.get("buy").getAsString();
    }

    public String sell() {
        return dataStats.get("sell").getAsString();
    }

    public String high() {
        return dataStats.get("high").getAsString();
    }

    public String low() {
        return dataStats.get("low").getAsString();
    }

    public String changeRate() {
        return dataStats.get("changeRate").getAsString();
    }

    public String changePrice() {
        return dataStats.get("changePrice").getAsString();
    }

    public String vol() {
        return dataStats.get("vol").getAsString();
    }

    public String volValue() {
        return dataStats.get("volValue").getAsString();
    }

    public String averagePrice() {
        return dataStats.get("averagePrice").getAsString();
    }
    
    //--------------------------------------------------------------------------

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public JsonObject getStats24hr() {
        return stats24hr;
    }

    public void setStats24hr(JsonObject stats24hr) {
        this.stats24hr = stats24hr;
    }

    public JsonElement getCode() {
        return code;
    }

    public void setCode(JsonElement code) {
        this.code = code;
    }

    public JsonObject getDataStats() {
        return dataStats;
    }

    public void setDataStats(JsonObject dataStats) {
        this.dataStats = dataStats;
    }
    
}
