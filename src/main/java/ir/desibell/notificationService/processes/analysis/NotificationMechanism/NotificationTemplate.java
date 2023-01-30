package ir.desibell.notificationService.processes.analysis.NotificationMechanism;

import ir.desibell.notificationService.enums.analysis.Condition;
import ir.desibell.notificationService.processes.analysis.AnalysisData;

public class NotificationTemplate {

    private AnalysisData analysisData;

    private Condition range;
    private Condition rate;

    public NotificationTemplate(AnalysisData analysisData) {
        this.analysisData = analysisData;
        this.range = this.analysisData.checkForChangesInSafeRange();
        this.rate = this.analysisData.checkForChangesInSafeRate();
    }

    public String getNotification() {
        String message = "";

        message = this.rangeMessage(range, message);
        if (!range.equals(Condition.POSITIVE_PLUS_PLUS) && !range.equals(Condition.NEGATIVE_PLUS_PLUS)) {
            message = this.rateMessage(rate, message);
        }

        if (!message.isBlank()) {
            message
                    += this.lastPrice()
                    + "\n" + this.changeRate()
                    + "\n" + this.changePrice()
                    //                    + "\n" + this.highPrice()
                    //                    + "\n" + this.lowPrice()
                    + "\n";
        }
        return message;
    }

    public String rangeMessage(Condition range, String message) {
        switch (range) {
            case POSITIVE:
                message += this.currencyName() + this.rangePositive()
                        + "\n";
                break;
            case POSITIVE_PLUS:
                message += this.currencyName() + this.rangePositivePlus()
                        + "\n";
                break;
            case POSITIVE_PLUS_PLUS:
                message += this.currencyName() + this.rangePositivePlusPlus()
                        + "\n";
                break;
            case NEGATIVE:
                message += this.currencyName() + this.rangeNegative()
                        + "\n";
                break;
            case NEGATIVE_PLUS:
                message += this.currencyName() + this.rangeNegativePlus()
                        + "\n";
                break;
            case NEGATIVE_PLUS_PLUS:
                message += this.currencyName() + this.rangeNegativePlusPlus()
                        + "\n";
                break;
            case STABLE:
                break;
            default:
                break;
        }

        return message;
    }

    public String rateMessage(Condition rate, String message) {
        if (message.isBlank()) {
            switch (rate) {
                case POSITIVE:
                    message += this.currencyName() + this.ratePositive()
                            + "\n";
                    break;
                case POSITIVE_PLUS:
                    message += this.currencyName() + this.ratePositivePlus()
                            + "\n";
                    break;
                case POSITIVE_PLUS_PLUS:
                    message += this.currencyName() + this.ratePositivePlusPlus()
                            + "\n";
                    break;
                case NEGATIVE:
                    message += this.currencyName() + this.rateNegative()
                            + "\n";
                    break;
                case NEGATIVE_PLUS:
                    message += this.currencyName() + this.rateNegativePlus()
                            + "\n";
                    break;
                case NEGATIVE_PLUS_PLUS:
                    message += this.currencyName() + this.rateNegativePlusPlus()
                            + "\n";
                    break;
                case STABLE:
                    break;
                default:
                    break;
            }
        } else {
            switch (rate) {
                case POSITIVE:
                    message += " همچنین ارز شما" + this.ratePositive()
                            + "\n";
                    break;
                case POSITIVE_PLUS:
                    message += " همچنین ارز شما" + this.ratePositivePlus()
                            + "\n";
                    break;
                case POSITIVE_PLUS_PLUS:
                    message += " همچنین ارز شما" + this.ratePositivePlusPlus()
                            + "\n";
                    break;
                case NEGATIVE:
                    message += " همچنین ارز شما" + this.rateNegative()
                            + "\n";
                    break;
                case NEGATIVE_PLUS:
                    message += " همچنین ارز شما" + this.rateNegativePlus()
                            + "\n";
                    break;
                case NEGATIVE_PLUS_PLUS:
                    message += " همچنین ارز شما" + this.rateNegativePlusPlus()
                            + "\n";
                    break;
                case STABLE:
                    break;
                default:
                    break;
            }
        }

        return message;
    }

    //----------------------------message---------------------------------------
    private String rangePositive() {
        return " به سقف بازه امن مورد نظر شما نزدیک شده است";
    }

    private String rangePositivePlus() {
        return " از سقف بازه امن مورد نظر شما عبور کرده است";
    }

    private String rangePositivePlusPlus() {
        return " با سقف بازه امن مورد نظر شما فاصله زیادی دارد"
                + "\n" + " جهت دریافت مجدد پیامک اطلاع رسانی، "
                + " لطفا بازه قیمتی خود را تغییر دهید";
    }

    private String rangeNegative() {
        return " به کف بازه امن مورد نظر شما نزدیک شده است";
    }

    private String rangeNegativePlus() {
        return " از کف بازه امن مورد نظر شما عبور کرده است";
    }

    private String rangeNegativePlusPlus() {
        return " با کف بازه امن مورد نظر شما فاصله زیادی دارد"
                + "\n" + " جهت دریافت مجدد پیامک اطلاع رسانی، "
                + " لطفا بازه قیمتی خود را تغییر دهید";
    }

    private String ratePositive() {
        return " در حال افرایش است";
    }

    private String ratePositivePlus() {
        return " در حال افرایش بیشتر از مورد انتظار شماست";
    }

    private String ratePositivePlusPlus() {
        return " در حال افرایش بیشتر از مورد انتظار شماست";
    }

    private String rateNegative() {
        return " در حال کاهش است";
    }

    private String rateNegativePlus() {
        return " در حال کاهش بیشتر از مورد انتظار شماست";
    }

    private String rateNegativePlusPlus() {
        return " در حال کاهش بیشتر از مورد انتظار شماست";
    }

    private String currencyName() {
        return "قیمت ارز " + this.analysisData.getData().getCryptocurrency().getName();
    }

    private String lastPrice() {
        return " آخرین قیمت: " + this.analysisData.last();
    }

    private String changeRate() {
        return " آخرین تغییرات: " + "%" + this.customizeChangeRate();
    }

    private String changePrice() {
        return " آخرین تغییرات قیمت: " + this.customizeChangePrice();
    }

    private String highPrice() {
        return "بالاترین قیمت روزانه : " + this.analysisData.high();
    }

    private String lowPrice() {
        return " پایین ترین قیمت روزانه: " + this.analysisData.low();
    }

    private String averagePrice() {
        return " میانگین قیمت معاملات روزانه: " + this.analysisData.averagePrice();
    }

    private String customizeChangeRate() {
        float persent = Float.parseFloat(this.analysisData.changeRate()) * 100;
        if (persent < 0) {
            return String.valueOf(Math.abs(persent)) + "-";
        } 
        return String.valueOf(persent);
    }
    
    private String customizeChangePrice() {
        float price = Float.parseFloat(this.analysisData.changePrice());
        if (price < 0) {
            return String.valueOf(Math.abs(price)) + "-";
        } 
        return String.valueOf(price);
    }
    //--------------------------------------------------------------------------

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
