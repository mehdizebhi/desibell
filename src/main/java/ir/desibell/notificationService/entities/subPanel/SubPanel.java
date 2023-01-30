package ir.desibell.notificationService.entities.subPanel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.message.Message;
import ir.desibell.notificationService.entities.subscribe.Subscribe;
import ir.desibell.notificationService.entities.user.User;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SubPanel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "phone_number", unique = true, length = 11)
    @Size(min = 11, max = 11)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;
    
    private boolean enabled;
    
    @ManyToMany
    private List<Cryptocurrency> availableCryptocurrencies;
    
    @OneToMany(mappedBy = "subPanel", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Data> data;
    
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public SubPanel() {
    }

    public SubPanel(User user, boolean enabled) {
        this.user = user;
        this.enabled = enabled;
    }
    
    public SubPanel(String phoneNumber, User user, boolean enabled, List<Cryptocurrency> availableCryptocurrencies, List<Data> data) {
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.enabled = enabled;
        this.availableCryptocurrencies = availableCryptocurrencies;
        this.data = data;
    }

    public SubPanel(String phoneNumber, User user, boolean enabled, List<Cryptocurrency> availableCryptocurrencies, List<Data> data, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.enabled = enabled;
        this.availableCryptocurrencies = availableCryptocurrencies;
        this.data = data;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Cryptocurrency> getAvailableCryptocurrencies() {
        return availableCryptocurrencies;
    }

    public void setAvailableCryptocurrencies(List<Cryptocurrency> availableCryptocurrencies) {
        this.availableCryptocurrencies = availableCryptocurrencies;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    
    public boolean isActiveSubPanel(){
        return this.user.isActiveCurrentSubscribe();
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
    
    public void addData(Data d){
        this.data.add(d);
    }
    
    public boolean deleteData(Data d){
        return this.data.remove(d);
    }
    
    public Data getData(int index){
        return this.data.get(index);
    }
    
    public void addAvailableCryptocurrencies(Cryptocurrency cryptocurrency){
        this.availableCryptocurrencies.add(cryptocurrency);
    }
    
    public boolean containAvailableCryptocurrency(Cryptocurrency cryptocurrency){
        return this.availableCryptocurrencies.contains(cryptocurrency);
    }
    
    public boolean deleteCryptocurrencyOnAvailableCryptocurrencies(Cryptocurrency cryptocurrency){
        return this.availableCryptocurrencies.remove(cryptocurrency);
    }
}
