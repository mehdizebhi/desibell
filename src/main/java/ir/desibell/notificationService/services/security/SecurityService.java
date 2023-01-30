package ir.desibell.notificationService.services.security;

import ir.desibell.notificationService.entities.confirmCode.ConfirmCode;
import ir.desibell.notificationService.entities.resetPasswordToken.ResetPasswordToken;
import ir.desibell.notificationService.services.confirmCode.ConfirmCodeService;
import ir.desibell.notificationService.services.resetPasswordToken.ResetPasswordTokenService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;
    
    @Autowired
    private ConfirmCodeService confirmCodeService;

    public boolean isValidResetPasswordToken(String token) {
        ResetPasswordToken resetPasswordToken = this.resetPasswordTokenService.findByToken(token);
        return isResetPasswordTokenFound(resetPasswordToken) && !isResetPasswordTokenExpired(resetPasswordToken)
                && !resetPasswordToken.isUsed();
    }

    public boolean isResetPasswordTokenFound(ResetPasswordToken resetPasswordToken) {
        return resetPasswordToken != null;
    }

    public boolean isResetPasswordTokenExpired(ResetPasswordToken resetPasswordToken) {
        return LocalDateTime.now().isAfter(resetPasswordToken.getExpirationAt());
    }
    
    //--------------------------------------------------------------------------------------------------------------
    
    public boolean isValidateConfirmCode(String code, String phoneNumber){
        ConfirmCode cc = this.confirmCodeService.findByCodeAndPhoneNumber(code, phoneNumber);
        return isConfirmCodeFound(cc) && !isConfirmCodeExpired(cc) && !cc.isUsed();
    }
    
    public boolean isConfirmCodeFound(ConfirmCode cc){
        return cc != null;
    }
    
    public boolean isConfirmCodeExpired(ConfirmCode cc){
        return LocalDateTime.now().isAfter(cc.getExpirationAt());
    }
}
