package services;

import model.domain.Meci;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {

    void notifyTicketsSold(Meci meci) throws ServicesException, RemoteException;

}
