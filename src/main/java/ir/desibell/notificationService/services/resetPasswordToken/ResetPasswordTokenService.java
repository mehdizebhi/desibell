package ir.desibell.notificationService.services.resetPasswordToken;

import ir.desibell.notificationService.entities.resetPasswordToken.ResetPasswordToken;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.repositories.resetPasswordToken.ResetPasswordTokenRepository;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenService {

    @Autowired
    ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordTokenService(ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Transactional
    public ResetPasswordToken registerToken(ResetPasswordToken token) {
        return this.resetPasswordTokenRepository.save(token);
    }

    @Transactional
    public ResetPasswordToken registerTokenByUser(User user, String token) {
        ResetPasswordToken rpt = new ResetPasswordToken(token, user, LocalDateTime.now().plusSeconds(ResetPasswordToken.getEXPIRATION()), false);
        return this.resetPasswordTokenRepository.save(rpt);
    }
    
    @Transactional
    public ResetPasswordToken updateUsedToken(String token){
        ResetPasswordToken rpt = this.findByToken(token);
        rpt.setUsed(true);
        return this.resetPasswordTokenRepository.save(rpt);
    }

    @Transactional
    public void deleteById(Long id) {
        this.resetPasswordTokenRepository.deleteById(id);
    }

    public List<ResetPasswordToken> findAllTokens() {
        return this.resetPasswordTokenRepository.findAll();
    }
    
    public Page<ResetPasswordToken> findAllTokensPage(Pageable pageable) {
        return this.resetPasswordTokenRepository.findAll(pageable);
    }

    public ResetPasswordToken findById(Long id) {
        return this.resetPasswordTokenRepository.getById(id);
    }

    public ResetPasswordToken findByToken(String Token) {
        return this.resetPasswordTokenRepository.findByToken(Token);
    }
}
