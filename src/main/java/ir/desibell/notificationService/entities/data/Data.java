package ir.desibell.notificationService.entities.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import ir.desibell.notificationService.entities.message.Message;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Cryptocurrency cryptocurrency;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cryptocurrency_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"data_id", "name"})},
            joinColumns = @JoinColumn(name = "data_id", referencedColumnName = "id"))
    @Column(name = "value")
    @MapKeyColumn(name = "name")
    private Map<String, String> nameValue;

    @ManyToOne(cascade = {javax.persistence.CascadeType.PERSIST})
    private SubPanel subPanel;
    
    @OneToOne(cascade = {javax.persistence.CascadeType.ALL}, mappedBy = "data")
    private Message lastMessage;

    public Data() {
    }

    public Data(SubPanel subPanel) {
        this.subPanel = subPanel;
    }

    public Data(Cryptocurrency cryptocurrency, Map<String, String> nameValue, SubPanel subPanel) {
        this.cryptocurrency = cryptocurrency;
        this.nameValue = nameValue;
        this.subPanel = subPanel;
    }

    public Data(Cryptocurrency cryptocurrency, Map<String, String> nameValue, SubPanel subPanel, Message lastMessage) {
        this.cryptocurrency = cryptocurrency;
        this.nameValue = nameValue;
        this.subPanel = subPanel;
        this.lastMessage = lastMessage;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cryptocurrency getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public Map<String, String> getNameValue() {
        return nameValue;
    }

    public void setNameValue(Map<String, String> nameValue) {
        this.nameValue = nameValue;
    }

    public SubPanel getSubPanel() {
        return subPanel;
    }

    public void setSubPanel(SubPanel subPanel) {
        this.subPanel = subPanel;
    }

    public void addNameValue(String name, String value) {
        this.nameValue.put(name, value);
    }

    public String getValueOfNameValue(String key) {
        return this.nameValue.get(key);
    }

    public void removeNameValue(String name) {
        this.nameValue.remove(name);
    }

    public void removeAllNameValue() {
        this.nameValue.clear();
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
