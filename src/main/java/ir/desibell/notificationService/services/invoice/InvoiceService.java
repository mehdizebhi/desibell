package ir.desibell.notificationService.services.invoice;

import ir.desibell.notificationService.config.beanUpdate.BeanCopyConfig;
import ir.desibell.notificationService.entities.invoice.Invoice;
import ir.desibell.notificationService.entities.subscribe.TypeOfSubscribe;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.enums.invoice.Status;
import ir.desibell.notificationService.repositories.invoice.InvoiceRepository;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public Invoice registerInvoice(Invoice invoice) throws IllegalAccessException, InvocationTargetException {
        if (invoice.getId() != null) {
            Invoice existInvoice = this.invoiceRepository.getById(invoice.getId());
            BeanCopyConfig updateExistInvoiceByInputInvoice = new BeanCopyConfig();
            updateExistInvoiceByInputInvoice.copyProperties(existInvoice, invoice);
            return this.invoiceRepository.save(existInvoice);
        }
        return this.invoiceRepository.save(invoice);
    }

    @Transactional
    public void deleteById(Long id) {
        this.invoiceRepository.deleteById(id);
    }

    public List<Invoice> findAllInvoices() {
        return this.invoiceRepository.findAll();
    }
    
    public Page<Invoice> findAllInvoices(Pageable pageable){
        return this.invoiceRepository.findAll(pageable);
    }
    
    public Page<Invoice> findAllUserInvoices(User user, Pageable pageable){
        return this.invoiceRepository.findAllByUser(user, pageable);
    }
    
    public List<Invoice> findUnsuccessfulInvoiceByUserAndTypeOfSubscribe(User user, TypeOfSubscribe typeOfSubscribe){
        return this.invoiceRepository.findInvoiceByUserAndTypeOfSubscribe(user, typeOfSubscribe);
    }

    public Invoice findById(Long id) {
        return this.invoiceRepository.getById(id);
    }

    public Invoice findByInvoiceNumber(Long invoiceNumber) {
        return this.invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }
    
    public long successfulInvoicesNum(){
        return this.invoiceRepository.successfulInvoicesNumber();
    }
    
    public long sumOfAllAmount(){
        return this.invoiceRepository.sumOfAllAmountInvoices();
    }

    public Invoice findByCode(String code) {
        return this.invoiceRepository.findByCode(code);
    }
}
