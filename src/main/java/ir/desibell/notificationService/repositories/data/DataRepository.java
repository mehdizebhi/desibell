package ir.desibell.notificationService.repositories.data;

import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
    
    @Query("select d from Data d where d.subPanel = :subPanel")
    public List<Data> findAllBySubPanelList(@Param("subPanel") SubPanel subPanel);

    @Query("select d from Data d where d.subPanel = :subPanel")
    public Page<Data> findAllBySubPanel(@Param("subPanel") SubPanel subPanel, Pageable pageable);

    @Query("select d from Data d where d.cryptocurrency = :cryptocurrency")
    public List<Data> findAllByCurrency(@Param("cryptocurrency") Cryptocurrency cryptocurrency);
    
    @Query("select d.cryptocurrency from Data d where d.subPanel = :subPanel")
    public List<Cryptocurrency> findAllCryptocurrencysBySubPanel(@Param("subPanel") SubPanel subPanel);

}
