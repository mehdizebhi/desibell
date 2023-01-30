package ir.desibell.notificationService.services.message;

import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.message.Message;
import ir.desibell.notificationService.repositories.message.MessageRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    @Autowired
    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    
    @Transactional
    public Message registerMessage(Message message){
        return this.messageRepository.save(message);
    }
    
    @Transactional
    public void deleteById(Long id){
        this.messageRepository.deleteById(id);
    }
    
    public Message findById(Long id){
        return this.messageRepository.getById(id);
    }
    
    public List<Message> findAllMessage(){
        return this.messageRepository.findAll();
    }
    
    public List<Message> findByData(Data data){
        return this.messageRepository.findByData(data);
    }
}
