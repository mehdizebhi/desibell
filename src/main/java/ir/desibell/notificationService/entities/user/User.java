package ir.desibell.notificationService.entities.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.desibell.notificationService.entities.invoice.Invoice;
import ir.desibell.notificationService.entities.role.Role;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.entities.subscribe.Subscribe;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(unique = true)
    //@Size(min = 11, max = 11, message = "Mobile number lengthٌ must be 11 digits!")
    private String number;

    //@JsonIgnore
    @NotBlank
    //@Size(min = 8, message = "Password length must be greater than 6!")
    private String password;

    @Transient
    private String confirmPassword;

    @Transient
    private String oldPassword;

    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    private boolean enabled = true;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(unique = true)
    private SubPanel SubPanel;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "authorities", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"number", "role"})},
            joinColumns = {
                @JoinColumn(name = "number", referencedColumnName = "number")},
            inverseJoinColumns = {
                @JoinColumn(name = "role", referencedColumnName = "name")}
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<Subscribe> subscribes;  //no subscribes in same time

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Subscribe currentSubscribe;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<Invoice> invoices;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(Long id, String number, String password, String confirmPassword, String email, String name, SubPanel SubPanel, List<Role> roles, List<Subscribe> subscribes, Subscribe currentSubscribe, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.name = name;
        this.SubPanel = SubPanel;
        this.roles = roles;
        this.subscribes = subscribes;
        this.currentSubscribe = currentSubscribe;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSubPanel(SubPanel SubPanel) {
        this.SubPanel = SubPanel;
    }

    public SubPanel getSubPanel() {
        return SubPanel;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Subscribe> getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(List<Subscribe> subscribes) {
        this.subscribes = subscribes;
    }

    public Subscribe getCurrentSubscribe() {
        return currentSubscribe;
    }

    public void setCurrentSubscribe(Subscribe currentSubscribe) {
        this.currentSubscribe = currentSubscribe;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public boolean isActiveCurrentSubscribe() {
        if (currentSubCalculation() != null) {
            return true;
        } else {
            return false;
        }
    }

    public Subscribe currentSubCalculation() {
        if (currentSubscribe == null || currentSubscribe.expirationCalculation()
                || currentSubscribe.remainingSubTime() <= 5 * 60) {
            currentSubscribe = null;
        }
        return currentSubscribe;
    }

    //this method is useless but maybe we need this shit.
    public Subscribe findCurrentSubFromSubs() {
        if (currentSubCalculation() == null) {
            for (Subscribe sub : subscribes) {
                if (!sub.expirationCalculation() && sub.remainingSubTime() > 5 * 60) {
                    currentSubscribe = sub;
                }
            }
        }
        return currentSubscribe;
    }

    public void removeSubscribeRoles() {
        removeRole("SUB");
    }

    public void removeRole(Role role) {
        for (Role r : roles) {
            if (r.equals(role)) {
                this.roles.remove(role);
            }
        }
    }

    public void removeRole(String beginningOfRollName) {
        for (Role role : roles) {
            if (role.getName().startsWith(beginningOfRollName)) {
                this.roles.remove(role);
            }
        }
    }

    public void addRole(Role role) {
        boolean isExistRole = false;
        if (this.roles != null) {
            for (Role r : roles) {
                if (r.equals(role)) {
                    isExistRole = true;
                }
            }
        } else {
            this.roles = new ArrayList<Role>();
        }
        if (!isExistRole) {
            this.roles.add(role);
        }
    }

    public boolean addSubscribe(Subscribe subscribe) {
        if (!isActiveCurrentSubscribe()) {
            this.currentSubscribe = subscribe;
            this.subscribes.add(subscribe);
            addRole(subscribe.getTypeOfSubscribe().getRole());
            return true;
        } else {
            return false;
        }
    }
}
