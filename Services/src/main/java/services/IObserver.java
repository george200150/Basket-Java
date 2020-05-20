package services;

import basket.model.domain.Meci;

public interface IObserver {

    void notifyTicketsSold(Meci meci) throws ServicesException;

}
