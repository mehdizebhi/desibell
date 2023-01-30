package ir.desibell.notificationService.repositories.invoice;

import ir.desibell.notificationService.entities.invoice.Invoice;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.enums.invoice.Status;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    public Invoice findByInvoiceNumber(Long invoiceNumber);

    public Invoice findByCode(String code);

    @Query("select i from Invoice i where i.user = :user")
    public Page<Invoice> findAllByUser(@Param("user") User user, Pageable pageable);

    @Query("select i from Invoice i where i.user = :user and i.typeOfSubscribe = :typeOfSubscribe and"
            + " (i.status = 'UNSUCCESSFUL' or i.status = 'WAITING')")
    public List<Invoice> findInvoiceByUserAndTypeOfSubscribe(@Param("user") User user,
            @Param("typeOfSubscribe") TypeOfSubscribe typeOfSubscribe);
    
    @Query(nativeQuery = true, value = "select count(*) from desibell.invoice i where i.status = 'SUCCESSFUL'")
    public long successfulInvoicesNumber();
    
    @Query(nativeQuery = true, value = "select sum(amount) from desibell.invoice i where i.status = 'SUCCESSFUL'")
    public long sumOfAllAmountInvoices();
}
