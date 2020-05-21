package server;

import basket.model.domain.Meci;
import basket.model.notification.Notification;
import basket.model.notification.NotificationType;
import org.springframework.jms.core.JmsOperations;
import services.IChatNotificationService;


public class NotificationServiceImpl implements IChatNotificationService {
    private JmsOperations jmsOperations;

    // to do - remove the counter
    private static int counter = 0;

    public NotificationServiceImpl(JmsOperations operations) {
        jmsOperations=operations;
    }

    @Override
    public void newMeciUpdate(Meci meci) {
        System.out.println("New message notification");
        Notification notif = new Notification(NotificationType.NEW_MECI_UPDATE, meci);
        jmsOperations.convertAndSend(notif);
        System.out.println("Sent message to ActiveMQ... " + notif + ' ' + counter);
        counter++;
    }
}
