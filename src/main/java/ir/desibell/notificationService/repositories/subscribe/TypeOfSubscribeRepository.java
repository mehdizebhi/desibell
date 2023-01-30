package ir.desibell.notificationService.repositories.subscribe;

import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfSubscribeRepository extends JpaRepository<TypeOfSubscribe, Long> {
    
    public TypeOfSubscribe findByName(String name);
}
