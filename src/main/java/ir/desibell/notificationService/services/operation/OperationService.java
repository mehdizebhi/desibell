package ir.desibell.notificationService.services.operation;

import ir.desibell.notificationService.entities.operation.Operation;
import ir.desibell.notificationService.repositories.operation.OperationRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OperationService {
    
    @Autowired
    OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
    
    @Transactional
    public Operation registerOperation(Operation operation){
        return this.operationRepository.save(operation);
    }
    
    public List<Operation> findAllOperations(){
        return this.operationRepository.findAll();
    }
    
    public Page<Operation> findAllOperationsPage(Pageable pageable){
        return this.operationRepository.findAll(pageable);
    }
    
    @Transactional
    public void deleteById(Long id){
        this.operationRepository.deleteById(id);
    }
    
    public Operation findById(Long id){
        return this.operationRepository.getById(id);
    }
}
