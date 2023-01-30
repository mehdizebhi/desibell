package ir.desibell.notificationService.services.confirmCode;

import ir.desibell.notificationService.entities.confirmCode.ConfirmCode;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.repositories.confirmCode.ConfirmCodeRepository;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ConfirmCodeService {

    @Autowired
    ConfirmCodeRepository confirmCodeRepository;

    public ConfirmCodeService(ConfirmCodeRepository confirmCodeRepository) {
        this.confirmCodeRepository = confirmCodeRepository;
    }
    
    @Transactional
    public ConfirmCode registerConfirmCode(ConfirmCode confirmCode) {
        return this.confirmCodeRepository.save(confirmCode);
    }

    @Transactional
    public ConfirmCode registerConfirmCodeByUser(User user, String code, String phoneNumber) {
        ConfirmCode confirmCode = new ConfirmCode(code, user, LocalDateTime.now().plusSeconds(ConfirmCode.getEXPIRATION()), false, phoneNumber);
        return this.confirmCodeRepository.save(confirmCode);
    }

    @Transactional
    public void deleteById(Long id) {
        this.confirmCodeRepository.deleteById(id);
    }

    @Transactional
    public ConfirmCode updateUsedConfirmCode(Long id) {
        ConfirmCode cc = this.confirmCodeRepository.getById(id);
        cc.setUsed(true);
        return this.confirmCodeRepository.save(cc);
    }
    
    public List<ConfirmCode> findAllConfirmCodes(){
        return this.confirmCodeRepository.findAll();
    }
    
    public Page<ConfirmCode> findAllConfirmCodesPage(Pageable pageable){
        return this.confirmCodeRepository.findAll(pageable);
    }
    
    public ConfirmCode findById(Long id) {
        return this.confirmCodeRepository.getById(id);
    }
    
    public ConfirmCode findByCode(String code){
        return this.confirmCodeRepository.findByCode(code);
    }
    
    public ConfirmCode findByCodeAndPhoneNumber(String code, String phoneNumber){
        return this.confirmCodeRepository.findByCodeAndPhoneNumber(code, phoneNumber);
    }

}
