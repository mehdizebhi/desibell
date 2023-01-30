package ir.desibell.notificationService.services.subscribe;

import ir.desibell.notificationService.entities.subscribe.Subscribe;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.repositories.subscribe.SubscribeRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {
    
    @Autowired
    SubscribeRepository subscribeRepository;

    public SubscribeService(SubscribeRepository subscribeRepository) {
        this.subscribeRepository = subscribeRepository;
    }
    
    @Transactional
    public Subscribe registerSubscribe(Subscribe subscribe){
        return this.subscribeRepository.save(subscribe);
    }
    
    @Transactional
    public Subscribe updateSubscribe(Subscribe subscribe){
        return this.subscribeRepository.save(subscribe);
    }
    
    public List<Subscribe> findSubscribeByUserAndTypeOfSubscribes(User user, TypeOfSubscribe typeOfSubscribe){
        return this.subscribeRepository.findByUserAndTypeOfSubscribe(user, typeOfSubscribe);
    }
    
    public List<Subscribe> findAllSubscribes(){
        return this.subscribeRepository.findAll();
    }
    
    public Subscribe findById(Long id){
        return this.subscribeRepository.getById(id);
    }
    
    public long numberOfSubs(){
        return this.subscribeRepository.numberOfSubscribe();
    }
    
    public long numberOfCurrentSubs(){
        return this.subscribeRepository.numberOfCurrentSubscribe();
    }
    
    @Transactional
    public void deleteById(Long id){
        this.subscribeRepository.deleteById(id);
    }
}
