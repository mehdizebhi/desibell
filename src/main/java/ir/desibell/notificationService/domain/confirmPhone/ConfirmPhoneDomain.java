package ir.desibell.notificationService.domain.confirmPhone;

public class ConfirmPhoneDomain {
    
    private String code;
    private String phoneNumber;

    public ConfirmPhoneDomain() {
    } 

    public ConfirmPhoneDomain(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public ConfirmPhoneDomain(String Code, String phoneNumber) {
        this.code = Code;
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
}
