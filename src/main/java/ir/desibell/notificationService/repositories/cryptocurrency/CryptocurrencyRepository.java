package ir.desibell.notificationService.repositories.cryptocurrency;

import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {
    
    @Query(nativeQuery = true, value = "select count(*) from desibell.cryptocurrency")
    public long numberOfCryptocurrency();
    
    @Query(nativeQuery = true, value = "select count(*) from desibell.sub_panel_available_cryptocurrencies")
    public long numberOfAvailableCryptocurrency();
    
}
