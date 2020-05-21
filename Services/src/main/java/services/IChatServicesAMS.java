package services;

import basket.model.domain.Client;
import basket.model.domain.Meci;


public interface IChatServicesAMS {
    void login(Client user) throws ServicesException;
    void logout(Client user) throws ServicesException;
    Meci[] findAllMeciWithTickets() throws ServicesException; //order DESC
    Meci[] findAllMeci() throws ServicesException;
    void ticketsSold(Meci meci, Client loggedInClient) throws ServicesException;
}
