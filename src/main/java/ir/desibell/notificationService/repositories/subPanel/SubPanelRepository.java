package ir.desibell.notificationService.repositories.subPanel;

import ir.desibell.notificationService.entities.subPanel.SubPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubPanelRepository extends JpaRepository<SubPanel, Long> {
    
    @Query("select s from SubPanel s join fetch s.user where s.id = :id")
    public SubPanel findSubPanelWithUserById(@Param("id") Long id);
    
    public SubPanel findByPhoneNumber(String phoneNumber);
    
    @Query("select s from SubPanel s where s.id = :id")
    public SubPanel loadById(@Param("id") Long id);
}
