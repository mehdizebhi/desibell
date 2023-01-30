package ir.desibell.notificationService.enums.invoice;

public enum Status {
    SUCCESSFUL("پرداخت با موفقیت انحام شد"),
    UNSUCCESSFUL("پرداخت ناموفق بود"),
    WAITING("در انتظار پرداخت"),
    EXPIRED("پرداخت منقضی شد");

    private String message;

    private Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
