package services;

import model.domain.Client;
import model.domain.Meci;

public interface IServices {
    void login(Client user, IObserver client) throws ServicesException;
    void logout(Client user, IObserver client) throws ServicesException;


    Meci[] findAllMeciWithTickets() throws ServicesException; //order DESC
    Meci[] findAllMeci() throws ServicesException;
    //Bilet[] findAllBilet() throws ServicesException;
    void ticketsSold(Meci meci, Client loggedInClient) throws ServicesException;
    //void updateBilet(Bilet bilet) throws ServicesException;
    //Echipa findOneEchipa(String id) throws ServicesException; //TODO: match has everything it needs to know now. no more teams!
}
