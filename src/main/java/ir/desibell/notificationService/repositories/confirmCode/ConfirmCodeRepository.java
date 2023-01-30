package ir.desibell.notificationService.repositories.confirmCode;

import ir.desibell.notificationService.entities.confirmCode.ConfirmCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, Long> {
    
    @Query("select c from ConfirmCode c where c.code = :code")
    public ConfirmCode findByCode(@Param("code") String code);
    
    @Query("select c from ConfirmCode c where c.code = :code and c.phoneNumber = :phoneNumber")
    public ConfirmCode findByCodeAndPhoneNumber(@Param("code") String code, @Param("phoneNumber") String phoneNumber);
}
