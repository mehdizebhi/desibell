package ir.desibell.notificationService.domain.resetPassword;

public class ResetPasswordDomain {
    
    private String email;

    public ResetPasswordDomain() {
    }

    public ResetPasswordDomain(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
