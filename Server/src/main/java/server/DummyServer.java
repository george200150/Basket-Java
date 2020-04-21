package server;

import model.domain.Client;
import model.domain.Meci;
import services.IObserver;
import services.IServices;
import services.ServicesException;

public class DummyServer implements IServices {
    @Override
    public void login(Client user, IObserver client) throws ServicesException {

    }

    @Override
    public void logout(Client user, IObserver client) throws ServicesException {

    }

    @Override
    public Meci[] findAllMeciWithTickets() throws ServicesException {
        return new Meci[0];
    }

    @Override
    public Meci[] findAllMeci() throws ServicesException {
        return new Meci[0];
    }

    @Override
    public void ticketsSold(Meci meci, Client loggedInClient) throws ServicesException {

    }
}
