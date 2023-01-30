package ir.desibell.notificationService.entities.invoice;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.desibell.notificationService.entities.subscribe.Subscribe;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.enums.invoice.Status;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Invoice_number", unique = true)
    private Long invoiceNumber;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "expiration_at", updatable = false)
    private LocalDateTime expirationAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private User user;
    
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(unique = true)
    private String code;
    private Long amount;
    private String description;
    
    @Column(name = "client_ref_id")
    private String clientRefId;
    
    @Column(name = "card_number")
    private String cardNumber;
    
    @Column(name = "ref_id")
    private String refId;
    
    @Column(name = "card_hash_pan")
    private String cardHashPan;
    
    @ManyToOne
    private TypeOfSubscribe typeOfSubscribe;
    
    @OneToOne
    private Subscribe subscribe;

    public Invoice() {
    }

    public Invoice(Long invoiceNumber, User user, LocalDateTime expirationAt, Status status, String code, Long amount, String description, String clientRefId, TypeOfSubscribe typeOfSubscribe) {
        this.invoiceNumber = invoiceNumber;
        this.user = user;
        this.expirationAt = expirationAt;
        this.status = status;
        this.code = code;
        this.amount = amount;
        this.description = description;
        this.clientRefId = clientRefId;
        this.typeOfSubscribe = typeOfSubscribe;
    }

    public Invoice(Long invoiceNumber, User user, LocalDateTime expirationAt, Status status, String code, Long amount, String description, String clientRefId, String cardNumber, String refid, String cardHashPan,  TypeOfSubscribe typeOfSubscribe, Subscribe subscribe) {
        this.invoiceNumber = invoiceNumber;
        this.user = user;
        this.expirationAt = expirationAt;
        this.status = status;
        this.code = code;
        this.amount = amount;
        this.description = description;
        this.clientRefId = clientRefId;
        this.cardNumber = cardNumber;
        this.refId = refid;
        this.cardHashPan = cardHashPan;
        this.typeOfSubscribe = typeOfSubscribe;
        this.subscribe = subscribe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpirationAt() {
        return expirationAt;
    }

    public void setExpirationAt(LocalDateTime expirationAt) {
        this.expirationAt = expirationAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientRefId() {
        return clientRefId;
    }

    public void setClientRefId(String clientRefId) {
        this.clientRefId = clientRefId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getCardHashPan() {
        return cardHashPan;
    }

    public void setCardHashPan(String cardHashPan) {
        this.cardHashPan = cardHashPan;
    }

    public TypeOfSubscribe getTypeOfSubscribe() {
        return typeOfSubscribe;
    }

    public void setTypeOfSubscribe(TypeOfSubscribe typeOfSubscribe) {
        this.typeOfSubscribe = typeOfSubscribe;
    }

    public Subscribe getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Subscribe subscribe) {
        this.subscribe = subscribe;
    }
    
    public boolean isExpired(){
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expirationAt)) {
            return true;
        } 
        return false;
    }
    
    public Status updateStatus(){
        if(this.status != Status.SUCCESSFUL && this.status != Status.EXPIRED && isExpired()){
            this.status = Status.EXPIRED;
            return this.status;
        }
        return this.status;
    }
    
    public Long remainingInvoiceHours() {
        Duration duration = Duration.between(LocalDateTime.now(), expirationAt);
        return duration.toHours() > 0 ? duration.toHours() : Long.valueOf(0);
    }
    
    public Long remainingInvoiceMinutes() {
        Duration duration = Duration.between(LocalDateTime.now(), expirationAt);
        return duration.toMinutes() > 0 ? duration.toMinutes() : Long.valueOf(0);
    }

}
