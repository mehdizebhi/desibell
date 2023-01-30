package ir.desibell.notificationService.entities.subscribe;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.desibell.notificationService.entities.role.Role;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "type_of_subscribe")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TypeOfSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "number_of_coins_accepted")
    private int numberOfCoinsAccepted;  //Number of coins supported

    @OneToOne
    private Role role;

    private int period;   //days

    private Long price;   //rilas
    
    @ElementCollection
    @CollectionTable(name = "type_of_data",
            joinColumns = @JoinColumn(name = "type_of_subscribe_id", referencedColumnName = "id"))
    @Column(name = "name_of_value")
    private List<String> typeOfData;
    
    @OneToMany(mappedBy = "typeOfSubscribe")
    private List<Subscribe> subscribes;

    public TypeOfSubscribe() {
    }

    public TypeOfSubscribe(String name, int numOfCoinsSup, Role role, int period, Long price, List<String> typeOfData, List<Subscribe> subscribes) {
        this.name = name;
        this.numberOfCoinsAccepted = numOfCoinsSup;
        this.role = role;
        this.period = period;
        this.price = price;
        this.typeOfData = typeOfData;
        this.subscribes = subscribes;
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

    public int getNumberOfCoinsAccepted() {
        return numberOfCoinsAccepted;
    }

    public void setNumberOfCoinsAccepted(int numberOfCoinsAccepted) {
        this.numberOfCoinsAccepted = numberOfCoinsAccepted;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<Subscribe> getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(List<Subscribe> subscribes) {
        this.subscribes = subscribes;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<String> getTypeOfData() {
        return typeOfData;
    }

    public void setTypeOfData(List<String> typeOfData) {
        this.typeOfData = typeOfData;
    }
    
}
