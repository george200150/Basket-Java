package services;

import model.domain.Meci;

public interface IObserver {

    void ticketsSold(Meci meci) throws ServicesException;

}
