package ir.desibell.notificationService.services.subscribe;

import ir.desibell.notificationService.config.beanUpdate.BeanCopyConfig;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.repositories.subscribe.TypeOfSubscribeRepository;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TypeOfSubscribeService {
    
    @Autowired
    TypeOfSubscribeRepository typeOfSubscribeRepository;

    public TypeOfSubscribeService(TypeOfSubscribeRepository typeOfSubscribeRepository) {
        this.typeOfSubscribeRepository = typeOfSubscribeRepository;
    }
    
    @Transactional
    public TypeOfSubscribe registerTypeOfSubscribe(TypeOfSubscribe typeOfSubscribe){
        return this.typeOfSubscribeRepository.save(typeOfSubscribe);
    }  
    
    @Transactional
    public TypeOfSubscribe updateTypeOfSubscribe(TypeOfSubscribe tos) throws IllegalAccessException, InvocationTargetException{
        if(tos.getId() != null && this.typeOfSubscribeRepository.getById(tos.getId()) != null){
            TypeOfSubscribe existTos = this.typeOfSubscribeRepository.getById(tos.getId());
            BeanCopyConfig beanCopyConfig = new BeanCopyConfig();
            beanCopyConfig.copyProperties(existTos, tos);
            return this.typeOfSubscribeRepository.save(existTos);
        }
        return null;
    }
    
    public List<TypeOfSubscribe> findAllTypeOfSubscribes(){
        return this.typeOfSubscribeRepository.findAll();
    }
    
    public Page<TypeOfSubscribe> findAllTypeOfSubscribesPage(Pageable pageable){
        return this.typeOfSubscribeRepository.findAll(pageable);
    }
    
    public TypeOfSubscribe findById(Long id){
        return this.typeOfSubscribeRepository.getById(id);
    }
    
    public TypeOfSubscribe findByName(String name){
        return this.typeOfSubscribeRepository.findByName(name);
    }
    
    @Transactional
    public void deleteById(Long id){
        this.typeOfSubscribeRepository.deleteById(id);
    }
    
}
