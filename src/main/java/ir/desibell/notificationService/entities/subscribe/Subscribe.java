package ir.desibell.notificationService.entities.subscribe;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.desibell.notificationService.entities.invoice.Invoice;
import ir.desibell.notificationService.entities.user.User;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Subscribe {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private TypeOfSubscribe typeOfSubscribe;

    @ManyToOne
    private User user;

    private boolean expired;

    
    @Column(name = "beginning", updatable = false)
    private LocalDateTime beginningtAt;

    @Column(name = "expiration", updatable = false)
    private LocalDateTime expirationAt;
    
    @OneToOne
    private Invoice invoice;

    public Subscribe() {
    }

    public Subscribe(TypeOfSubscribe typeOfSebscribe, User user) {
        this.typeOfSubscribe = typeOfSebscribe;
        this.user = user;
    }
    
    public Subscribe(TypeOfSubscribe typeOfSebscribe, User user, boolean expired, LocalDateTime beginningtAt, LocalDateTime expirationAt) {
        this.typeOfSubscribe = typeOfSebscribe;
        this.user = user;
        this.expired = expired;
        this.beginningtAt = beginningtAt;
        this.expirationAt = expirationAt;
    }

    public Subscribe(TypeOfSubscribe typeOfSebscribe, User user, boolean expired, LocalDateTime beginningtAt, LocalDateTime expirationAt, Invoice invoice) {
        this.typeOfSubscribe = typeOfSebscribe;
        this.user = user;
        this.expired = expired;
        this.beginningtAt = beginningtAt;
        this.expirationAt = expirationAt;
        this.invoice = invoice;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOfSubscribe getTypeOfSubscribe() {
        return typeOfSubscribe;
    }

    public void setTypeOfSubscribe(TypeOfSubscribe typeOfSubscribe) {
        this.typeOfSubscribe = typeOfSubscribe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getBeginningtAt() {
        return beginningtAt;
    }

    public void setBeginningtAt(LocalDateTime beginningtAt) {
        this.beginningtAt = beginningtAt;
    }

    public LocalDateTime getExpirationAt() {
        return expirationAt;
    }
    
    public void setExpirationAt(LocalDateTime expirationAt) {
        this.expirationAt = expirationAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    
    public boolean expirationCalculation() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expirationAt)) {
            expired = true;
        } else {
            expired = false;
        }
        return expired;
    }
    
    public Long durationSubTime() {
        Duration duration = Duration.between(beginningtAt, expirationAt);
        return duration.getSeconds() > 0 ? duration.getSeconds() : Long.valueOf(0);
    }
    
    public Long remainingSubTime() {
        Duration duration = Duration.between(LocalDateTime.now(), expirationAt);
        return duration.getSeconds() > 0 ? duration.getSeconds() : Long.valueOf(0);
    }
     
    public Long remainingHours(LocalDateTime from, LocalDateTime to) {
        Duration duration = Duration.between(from, to);
        return duration.toHours()> 0 ? duration.toHours(): Long.valueOf(0);
    }
    
    public Long remainingSubDay() {
        Duration duration = Duration.between(LocalDateTime.now(), expirationAt);
        return duration.toDays() > 0 ? duration.toDays(): Long.valueOf(0);
    }
    
    public Long remainingSubHours(){
        // less 24H, on last day
        return remainingHours(LocalDateTime.now().plusDays(remainingSubDay()), expirationAt);
    }
    
    
    public double remainingPercentageOfSub(){
        return Math.round((remainingSubTime() * 1.0 / durationSubTime() * 100) * 10) / 10.0;
    }
}
