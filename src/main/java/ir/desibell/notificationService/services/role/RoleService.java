package ir.desibell.notificationService.services.role;

import ir.desibell.notificationService.entities.role.Role;
import ir.desibell.notificationService.repositories.role.RoleRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role registerRole(Role role) {
        return this.roleRepository.save(role);
    }

    public List<Role> findAllRoles() {
        return this.roleRepository.findAll();
    }
    public Page<Role> findAllRolesPage(Pageable pageable) {
        return this.roleRepository.findAll(pageable);
    }

    @Transactional
    public void deleteById(Long id) {
        this.roleRepository.deleteById(id);
    }
    
    public Role findById(Long id) {
        return this.roleRepository.getById(id);
    }

    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }
}
