package services;

import basket.model.domain.Meci;


public interface IChatNotificationService {
    void newMeciUpdate(Meci meci);
}
