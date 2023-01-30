package ir.desibell.notificationService.services.subPanel;

import ir.desibell.notificationService.config.beanUpdate.BeanCopyConfig;
import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.repositories.subPanel.SubPanelRepository;
import ir.desibell.notificationService.services.data.DataService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubPanelService {

    @Autowired
    SubPanelRepository subPanelRepository;

    @Autowired
    private DataService dataService;

    public SubPanelService(SubPanelRepository subPanelRepository) {
        this.subPanelRepository = subPanelRepository;
    }

    @Transactional
    public SubPanel registerSubPanel(SubPanel subPanel) throws IllegalAccessException, InvocationTargetException {
        if (subPanel.getId() != null && this.subPanelRepository.getById(subPanel.getId()) != null) {
            //updata exist subPanel
            SubPanel existSubPanel = this.subPanelRepository.getById(subPanel.getId());
            BeanCopyConfig beanCopyConfig = new BeanCopyConfig();
            beanCopyConfig.copyProperties(existSubPanel, subPanel);
            return this.subPanelRepository.save(existSubPanel);
        }
        return this.subPanelRepository.save(subPanel);
    }

    @Transactional
    public SubPanel updateEnabled(SubPanel subPanel, boolean enabled) {
        subPanel = this.subPanelRepository.getById(subPanel.getId());
        if (subPanel != null) {
            subPanel.setEnabled(enabled);
            return this.subPanelRepository.save(subPanel);
        }
        return null;
    }

    public boolean subPanelContainCryptocurrency(SubPanel subPanel, Cryptocurrency c) {
        return this.dataService.findAllCryptocurrencysOfSubPanelData(subPanel).contains(c);
    }

    public boolean verifyData(SubPanel subPanel, User user, Data data) {
        return subPanel.getUser().getId().equals(user.getId()) && user.isActiveCurrentSubscribe()
                && !subPanelContainCryptocurrency(subPanel, data.getCryptocurrency())
                && subPanel.getData().size() < user.getCurrentSubscribe().getTypeOfSubscribe().getNumberOfCoinsAccepted();
    }

    public List<SubPanel> findAllSubPanel() {
        return this.subPanelRepository.findAll();
    }

    public SubPanel findById(Long id) {
        return this.subPanelRepository.getById(id);
    }

    public SubPanel LoadById(Long id) {
        return this.subPanelRepository.loadById(id);
    }

    public SubPanel findWithUserById(Long id) {
        return this.subPanelRepository.findSubPanelWithUserById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        this.subPanelRepository.deleteById(id);
    }

    @Transactional
    public void updateData(SubPanel subPanel, User user) throws IllegalAccessException, InvocationTargetException {
        if (!user.isActiveCurrentSubscribe()) {
            this.dataService.deleteAllDataOfSubPanel(subPanel);
            subPanel.setAvailableCryptocurrencies(null);
            subPanel.setData(null);
            this.registerSubPanel(subPanel);

        } else {
            int dist = user.getCurrentSubscribe().getTypeOfSubscribe().getNumberOfCoinsAccepted() - subPanel.getData().size();
            if (dist < 0) {
                this.dataService.deleteAllDataOfSubPanel(subPanel);
                subPanel.setAvailableCryptocurrencies(null);
                subPanel.setData(null);
                this.registerSubPanel(subPanel);
            }
        }
    }

    public SubPanel findByPhoneNumber(String phoneNumber) {
        return this.subPanelRepository.findByPhoneNumber(phoneNumber);
    }

}
