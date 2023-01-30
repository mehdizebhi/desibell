package ir.desibell.notificationService.services.appNotification;

import ir.desibell.notificationService.config.beanUpdate.BeanCopyConfig;
import ir.desibell.notificationService.entities.appNotification.AppNotification;
import ir.desibell.notificationService.repositories.appNotification.AppNotificationRepository;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AppNotificationService {
    
    @Autowired
    AppNotificationRepository appNotificationRepository;

    public AppNotificationService(AppNotificationRepository appNotificationRepository) {
        this.appNotificationRepository = appNotificationRepository;
    }
    
    @Transactional
    public AppNotification registerNotification(AppNotification notification){
        return this.appNotificationRepository.save(notification);
    }
    
    @Transactional
    public AppNotification updateNotification(AppNotification notification) throws IllegalAccessException, InvocationTargetException{
        if(notification.getId() != null && this.appNotificationRepository.getById(notification.getId()) != null){
            AppNotification existNotification = this.appNotificationRepository.getById(notification.getId());
            BeanCopyConfig beanCopyConfig = new BeanCopyConfig();
            beanCopyConfig.copyProperties(existNotification, notification);
            return this.appNotificationRepository.save(existNotification);
        }
        return this.appNotificationRepository.save(notification);
    }
    
    @Transactional
    public void deleteById(Long id){
        this.appNotificationRepository.deleteById(id);
    }
    
    public AppNotification findById(Long id){
        return this.appNotificationRepository.getById(id);
    }
    
    public List<AppNotification> findAllNotifications(){
        return this.appNotificationRepository.findAll();
    }
    
    public Page<AppNotification> findAllNotificationsPage(Pageable pageable){
        return this.appNotificationRepository.findAll(pageable);
    }
    
    public List<AppNotification> findNumberOfNotifications(int size){
        return this.appNotificationRepository.findNumOfAppNotifications(size);
    }
}
