package ir.desibell.notificationService.repositories.resetPasswordToken;

import ir.desibell.notificationService.entities.resetPasswordToken.ResetPasswordToken;
import ir.desibell.notificationService.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long>{
    
    @Query("select r from ResetPasswordToken r where r.token = :token")
    public ResetPasswordToken findByToken(@Param("token") String token);
}
