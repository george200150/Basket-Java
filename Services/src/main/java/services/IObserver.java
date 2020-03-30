package services;

import model.domain.Meci;

public interface IObserver {

    void notifyTicketsSold(Meci meci) throws ServicesException;

}
