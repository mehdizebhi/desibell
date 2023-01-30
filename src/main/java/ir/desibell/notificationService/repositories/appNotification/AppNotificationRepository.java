package ir.desibell.notificationService.repositories.appNotification;

import ir.desibell.notificationService.entities.appNotification.AppNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppNotificationRepository extends JpaRepository<AppNotification, Long>{
    
    @Query(value = "select * from app_notification a order by a.updated_at desc limit :size", nativeQuery = true)
    public List<AppNotification> findNumOfAppNotifications(@Param("size") int size);
}
