package ir.desibell.notificationService.repositories.message;

import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.message.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query("select m from Message m where m.data = :data")
    public List<Message> findByData(@Param("data") Data data);
}
