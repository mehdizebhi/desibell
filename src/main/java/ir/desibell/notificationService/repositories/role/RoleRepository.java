package ir.desibell.notificationService.repositories.role;

import ir.desibell.notificationService.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    public Role findByName(String name);
}
