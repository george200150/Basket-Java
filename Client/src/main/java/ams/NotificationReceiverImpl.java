package ams;

import basket.model.notification.Notification;
import org.springframework.jms.core.JmsOperations;
import services.NotificationReceiver;
import services.NotificationSubscriber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class NotificationReceiverImpl implements NotificationReceiver {
    private JmsOperations jmsOperations;
    private boolean running;
    public NotificationReceiverImpl(JmsOperations operations) {
        jmsOperations=operations;
    }
    private ExecutorService service;
    private NotificationSubscriber subscriber;

    // to do - remove the counter of received messages
    private static int receviveds = 0;

    @Override
    public void start(NotificationSubscriber subscriber) {
        System.out.println("Starting notification receiver ...");
        running=true;
        this.subscriber=subscriber;
        service = Executors.newSingleThreadExecutor();
        service.submit(this::run);
    }

    private void run(){
        while(running){
            Notification notif=(Notification)jmsOperations.receiveAndConvert();
            System.out.println("Received Notification... "+notif + ' ' + receviveds);
            receviveds ++;
            subscriber.notificationReceived(notif);
            try {
                service.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void stop() {
        running=false;
        try {
            service.awaitTermination(500, TimeUnit.MILLISECONDS);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped notification receiver");
    }
}
