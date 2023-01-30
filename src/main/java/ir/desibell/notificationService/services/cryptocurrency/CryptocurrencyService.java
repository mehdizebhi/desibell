package ir.desibell.notificationService.services.cryptocurrency;

import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import ir.desibell.notificationService.repositories.cryptocurrency.CryptocurrencyRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CryptocurrencyService {

    @Autowired
    CryptocurrencyRepository cryptocurrencyRepository;

    public CryptocurrencyService(CryptocurrencyRepository cryptocurrencyRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    @Transactional
    public Cryptocurrency registerCryptocurrency(Cryptocurrency cryptocurrency) {
        return this.cryptocurrencyRepository.save(cryptocurrency);
    }

    public List<Cryptocurrency> findAllCryptocurrencies() {
        return this.cryptocurrencyRepository.findAll();
    }
    
    public Page<Cryptocurrency> findAllCryptocurrenciesPage(Pageable pageable) {
        return this.cryptocurrencyRepository.findAll(pageable);
    }

    public Cryptocurrency findById(Long id) {
        return this.cryptocurrencyRepository.getById(id);
    }
    
    public long numberOfCryptocurrency(){
        return this.cryptocurrencyRepository.numberOfCryptocurrency();
    }
    
    public long numberOfAvailableCryptocurrency(){
        return this.cryptocurrencyRepository.numberOfAvailableCryptocurrency();
    }

    @Transactional
    public void deleteById(Long id) {
        this.cryptocurrencyRepository.deleteById(id);
    }

}
