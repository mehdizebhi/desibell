package ir.desibell.notificationService.jwt.domain;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {

    private String number;
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String number, String password) {
        this.number = number;
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
