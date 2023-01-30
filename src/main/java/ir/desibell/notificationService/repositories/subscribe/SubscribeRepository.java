package ir.desibell.notificationService.repositories.subscribe;

import ir.desibell.notificationService.entities.subscribe.Subscribe;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.entities.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long>{
    
    @Query("select s from Subscribe s where s.user = :user and s.typeOfSubscribe = :typeOfSubscribe")
    public List<Subscribe> findByUserAndTypeOfSubscribe(@Param("user") User user, @Param("typeOfSubscribe") TypeOfSubscribe typeOfSubscribe);
    
    @Query(nativeQuery = true, value = "select count(*) from desibell.subscribe")
    public long numberOfSubscribe();
    
    @Query(nativeQuery = true, value = "select count(*) from desibell.subscribe i where i.expired = false")
    public long numberOfCurrentSubscribe();
    
}
