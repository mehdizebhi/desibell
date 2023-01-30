package ir.desibell.notificationService.processes.analysis.NotificationMechanism;

import com.google.gson.JsonObject;
import ir.desibell.notificationService.entities.cryptocurrency.Cryptocurrency;
import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.message.Message;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.entities.user.User;
import ir.desibell.notificationService.enums.analysis.Condition;
import ir.desibell.notificationService.processes.analysis.AnalysisData;
import ir.desibell.notificationService.processes.analysis.DataExtraction;
import ir.desibell.notificationService.processes.kucoinApi.KucoinMarketDataApi;
import ir.desibell.notificationService.processes.sms.SMSApi;
import ir.desibell.notificationService.services.cryptocurrency.CryptocurrencyService;
import ir.desibell.notificationService.services.data.DataService;
import ir.desibell.notificationService.services.message.MessageService;
import ir.desibell.notificationService.services.subPanel.SubPanelService;
import ir.desibell.notificationService.services.user.UserService;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MassNotification {

    private SubPanelService subPanelService;
    private DataService dataService;
    private CryptocurrencyService cryptocurrencyService;
    private UserService userService;
    private MessageService messageService;
    private SMSApi smsApi;

    @Value("${kucoinURL}")
    private String kucoinBaseUrl;

    @Value("${smsActivity}")
    private String smsIsAvtive;

    @Value("${smsNumber}")
    private String smsNumber;

    @Autowired
    public MassNotification(SubPanelService subPanelService, DataService dataService, CryptocurrencyService cryptocurrencyService,
            UserService userService, MessageService messageService, SMSApi smsApi) {
        this.subPanelService = subPanelService;
        this.dataService = dataService;
        this.cryptocurrencyService = cryptocurrencyService;
        this.userService = userService;
        this.messageService = messageService;
        this.smsApi = smsApi;
    }

    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @Scheduled(fixedDelay = 300000, initialDelay = 60000)
    public void sendAll() throws Exception {

        this.updateAllUsers();

        List<Cryptocurrency> cryptocurrencies = this.cryptocurrencyService.findAllCryptocurrencies();
        for (Cryptocurrency currency : cryptocurrencies) {
            String symbol = currency.getSymbol() + "-USDT";
            JsonObject stats24hr = this.getStats24hr(symbol);
            JsonObject statsData = stats24hr.getAsJsonObject("data");
            Long time = Long.valueOf(statsData.get("time").getAsString());
            List<Data> data = this.getData(currency);
            for (Data d : data) {
                /**
                 * If the time stamp is more than 2 seconds away from the time
                 * stamp data, we will update the data from Kucoin API again.
                 */
                Long now = Long.valueOf(timestamp.getTime());
                if (now - time > 10000) {
                    stats24hr = this.getStats24hr(symbol);
                    statsData = stats24hr.getAsJsonObject("data");
                    time = Long.valueOf(statsData.get("time").getAsString());
                }

                SubPanel subPanel = this.subPanelService.LoadById(d.getSubPanel().getId());
                if (this.isConfirm(subPanel)) {
                    AnalysisData analysisData = new AnalysisData(d, stats24hr);
                    NotificationTemplate nt = new NotificationTemplate(analysisData);
                    NotificationGeneration ng = new NotificationGeneration(nt, smsApi,smsNumber);
                    try {
                        if (smsIsAvtive.equals("enable")) {
                            sendNotification(d, subPanel, ng);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }

            }
        }
    }

    private JsonObject getStats24hr(String symbol) throws Exception {
        KucoinMarketDataApi api = new KucoinMarketDataApi(kucoinBaseUrl);
        return new DataExtraction(api).get24hrStats(symbol);
    }

    private List<Data> getData(Cryptocurrency cryptocurrency) {
        return this.dataService.findAllCryptocurrencyData(cryptocurrency);
    }

    private boolean isConfirm(SubPanel subPanel) throws IllegalAccessException, InvocationTargetException {
        User user = this.userService.findBySubPanel(subPanel);
        return subPanel.isEnabled() && user.isEnabled() && user.isActiveCurrentSubscribe();
    }

    private void updateAllUsers() throws IllegalAccessException, InvocationTargetException {
        this.userService.updateAllCurrentSubscribesUsers();
    }

    private boolean isChangedMessageCondition(Message lastMessage, Message newMessage) {
        return !lastMessage.getCondition().equals(newMessage.getCondition());
    }

    private void sendNotification(Data data, SubPanel subPanel, NotificationGeneration ng) throws IllegalAccessException, InvocationTargetException, Exception {
        if (data.getLastMessage() == null) {
            Message message = new Message(ng.getMessage(), ng.getRange(), data);
            this.messageService.registerMessage(message);
            System.out.println(ng.getMessage());
            System.out.println("----------------------------------");
            ng.send(subPanel.getPhoneNumber());
        } else {

            if (ng.getRange() != Condition.NEGATIVE_PLUS_PLUS && ng.getRange() != Condition.POSITIVE_PLUS_PLUS) {
                Message message = data.getLastMessage();
                message.setCondition(ng.getRange());
                message.setText(ng.getMessage());
                if (isChangedMessageCondition(data.getLastMessage(), message)
                        || LocalDateTime.now().isAfter(data.getLastMessage().getUpdatedAt().plusHours(1))) {
                    this.messageService.registerMessage(message);
                    System.out.println(ng.getMessage());
                    System.out.println("----------------------------------");
                    ng.send(subPanel.getPhoneNumber());
                }
            } else if ((data.getLastMessage().getCondition() != Condition.NEGATIVE_PLUS_PLUS
                    && data.getLastMessage().getCondition() != Condition.POSITIVE_PLUS_PLUS)
                    || LocalDateTime.now().isAfter(data.getLastMessage().getUpdatedAt().plusHours(12))) {

                Message message = data.getLastMessage();
                message.setCondition(ng.getRange());
                message.setText(ng.getMessage());
                this.messageService.registerMessage(message);
                System.out.println(ng.getMessage());
                System.out.println("----------------------------------");
                ng.send(subPanel.getPhoneNumber());
            } else {
                System.out.println("Nothing to send");
                System.out.println("----------------------------------");
            }
        }
    }
}
