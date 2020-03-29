package services;

import model.domain.Bilet;
import model.domain.Client;
import model.domain.Echipa;
import model.domain.Meci;

public interface IServices {
    void login(Client user, IObserver client) throws ServicesException;
    void logout(Client user, IObserver client) throws ServicesException;


    Meci[] findAllMeci() throws ServicesException;
    Bilet[] findAllBilet() throws ServicesException;
    void ticketsSold(Meci meci) throws ServicesException;
    void updateBilet(Bilet bilet) throws ServicesException;
    Echipa findOneEchipa(String id) throws ServicesException;
}
