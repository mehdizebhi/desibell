package ir.desibell.notificationService.entities.role;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.desibell.notificationService.entities.operation.Operation;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Role implements GrantedAuthority {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true)
    private String name;
    
    @ManyToMany
    private List<Operation> allowedOperations;

    public Role() {
    }

    public Role(String name, List<Operation> allowedOperations) {
        this.name = name;
        this.allowedOperations = allowedOperations;
    }
    
    @Override
    public String getAuthority() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Operation> getAllowedOperations() {
        return allowedOperations;
    }

    public void setAllowedOperations(List<Operation> allowedOperations) {
        this.allowedOperations = allowedOperations;
    }
    
    public boolean isExistOperation(Operation op){
        return this.allowedOperations.contains(op);
    }
    
}
