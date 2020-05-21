package services;

import basket.model.notification.Notification;


public interface NotificationSubscriber {
    void notificationReceived(Notification notif);
}
