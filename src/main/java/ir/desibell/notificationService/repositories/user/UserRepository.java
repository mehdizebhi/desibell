package ir.desibell.notificationService.repositories.user;

import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.entities.user.User;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    @Query("select u from User u join fetch u.subscribes where u.id = :id")
    public User findUserWithSubscribesById(@Param("id") Long id);
    
    @Modifying
    @Query(nativeQuery = true, value = "update desibell.authorities set number = :number where number = :existNumber")
    public void updateAuthorities(@Param("existNumber") String existNumber,@Param("number") String number);
    
    @Modifying
    @Query(nativeQuery = true, value = "delete from desibell.authorities where number = :number and role like 'SUB%'")
    public void deleteSubAuthorities(@Param("number") String number);
    
    @Modifying
    @Query(nativeQuery = true, value = "delete from desibell.authorities where number = :number and role = :role")
    public void deleteAuthority(@Param("number") String number, @Param("role") String role);
    
    @Modifying
    @Query(nativeQuery = true, value = "insert into desibell.authorities(number,role) values(:number,:role)")
    public void addAuthority(@Param("number") String number, @Param("role") String role);
    
    @Query(nativeQuery = true, value = "select role from desibell.authorities a where a.number = :number")
    public List<String> findAllNamesOfRolesByNumber(@Param("number") String number);
    
    @Query(nativeQuery = true, value = "select count(*) from desibell.user")
    public long numberOfUsers();
    
    @Query(nativeQuery = true, value = "select count(*) from desibell.authorities a where a.role = :role")
    public long numberOfRoles(@Param("role") String role);

    public User findByNumber(String number);
    public User findByEmail(String email);
    
    @Query(nativeQuery = true, value = "select * from desibell.user where sub_panel_id = :subPanel")
    public User findBySubPanel(@Param("subPanel")Long subPanelId);
    
}
