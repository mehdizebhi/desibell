package ir.desibell.notificationService.services.data;

import ir.desibell.notificationService.config.beanUpdate.BeanCopyConfig;
import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.repositories.data.DataRepository;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DataService {
    
    @Autowired
    DataRepository dataRepository;

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }
    
    @Transactional
    public Data registerData(Data data){
        return this.dataRepository.save(data);
    } 
    
    @Transactional
    public Data updateData(Data data) throws IllegalAccessException, InvocationTargetException{
        if(data.getId() != null && this.dataRepository.getById(data.getId()) != null){
            Data existData = this.dataRepository.getById(data.getId());
            BeanCopyConfig beanCopyConfig = new BeanCopyConfig();
            beanCopyConfig.copyProperties(existData, data);
            return this.dataRepository.save(data);
        }
        return null;
    }
    
    @Transactional
    public void deleteAllDataOfSubPanel(SubPanel subPanel){
        List<Data> data = this.dataRepository.findAllBySubPanelList(subPanel);
        this.dataRepository.deleteAll(data);
    }
    
    public List<Cryptocurrency> findAllCryptocurrencysOfSubPanelData(SubPanel subPanel){
        return this.dataRepository.findAllCryptocurrencysBySubPanel(subPanel);
    }
    
    public List<Data> findAllData(){
        return this.dataRepository.findAll();
    }
    
    public Page<Data> findAllSubPanelData(SubPanel subPanel, Pageable pageable){
        return this.dataRepository.findAllBySubPanel(subPanel, pageable);
    }
    
    public List<Data> findAllCryptocurrencyData(Cryptocurrency cryptocurrency){
        return this.dataRepository.findAllByCurrency(cryptocurrency);
    }

    public Data findById(Long id){
        return this.dataRepository.getById(id);
    }
    
    @Transactional
    public void deleteById(Long id){
        this.dataRepository.deleteById(id);
    }
}
