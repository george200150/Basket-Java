package services;

import domain.Bilet;
import domain.Client;
import domain.Echipa;
import domain.Meci;
import utils.events.*;
import utils.observers.*;
import validators.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class MasterService implements ObservableBilet, ObservableClient, ObservableEchipa, ObservableMeci {
    private BiletService biletService = null;
    private ClientService clientService = null;
    private EchipaService echipaService = null;
    private MeciService meciService = null;

    private List<BiletObserver> biletObservers = new ArrayList<>();
    private List<ClientObserver> clientObservers = new ArrayList<>();
    private List<EchipaObserver> echipaObservers = new ArrayList<>();
    private List<MeciObserver> meciObservers = new ArrayList<>();
    //TODO: IMPLEMENT OBSERVERS FOR ALL ENTITIES.

    public MasterService(BiletService biletService, ClientService clientService, EchipaService echipaService, MeciService meciService) {
        this.biletService = biletService;
        this.clientService = clientService;
        this.echipaService = echipaService;
        this.meciService = meciService;
    }

    public Client findClientByCredentials(String usr, String psswd){
        Client client = findOneClient(usr);
        if (client != null && client.getPassword().equals(psswd))
                return client;
        return null;
    }


    public Bilet findOneBilet(String id){
        return biletService.findOne(id);
    }
    public Iterable<Bilet> findAllBilet() {
        return biletService.findAll();
    }
    public Bilet saveBilet(Bilet entity) throws ValidationException {
        Bilet r = biletService.save(entity);
        if(r == null) {
            notifyObserversBilet(new BiletChangeEvent(ChangeEventType.ADD, entity));
        }
        return r;
    }
    public Bilet deleteBilet(String id){
        Bilet r = biletService.delete(id);
        if(r != null) {
            notifyObserversBilet(new BiletChangeEvent(ChangeEventType.DELETE, r));
        }
        return r;
    }
    public Bilet updateBilet(Bilet newEntity){
        Bilet oldEntity = biletService.findOne(newEntity.getId());
        Bilet res = biletService.update(newEntity);
        if(res == null) {
            notifyObserversBilet(new BiletChangeEvent(ChangeEventType.UPDATE, newEntity, oldEntity));
        }
        return res;
    }


    public Client findOneClient(String id){
        return clientService.findOne(id);
    }
    public Iterable<Client> findAllClient() {
        return clientService.findAll();
    }
    public Client saveClient(Client entity) throws ValidationException {
        Client r = clientService.save(entity);
        if(r == null) {
            notifyObserversClient(new ClientChangeEvent(ChangeEventType.ADD, entity));
        }
        return r;
    }
    public Client deleteClient(String id){
        Client r = clientService.delete(id);
        if(r != null) {
            notifyObserversClient(new ClientChangeEvent(ChangeEventType.DELETE, r));
        }
        return r;
    }
    public Client updateClient(Client newEntity){
        Client oldEntity = clientService.findOne(newEntity.getId());
        Client res = clientService.update(newEntity);
        if(res == null) {
            notifyObserversClient(new ClientChangeEvent(ChangeEventType.UPDATE, newEntity, oldEntity));
        }
        return res;
    }



    public Echipa findOneEchipa(String id){
        return echipaService.findOne(id);
    }
    public Iterable<Echipa> findAllEchipa() {
        return echipaService.findAll();
    }
    public Echipa saveEchipa(Echipa entity) throws ValidationException {
        Echipa r = echipaService.save(entity);
        if(r == null) {
            notifyObserversEchipa(new EchipaChangeEvent(ChangeEventType.ADD, entity));
        }
        return r;
    }
    public Echipa deleteEchipa(String id){
        Echipa r = echipaService.delete(id);
        if(r != null) {
            notifyObserversEchipa(new EchipaChangeEvent(ChangeEventType.DELETE, r));
        }
        return r;
    }
    public Echipa updateEchipa(Echipa newEntity){
        Echipa oldEntity = echipaService.findOne(newEntity.getId());
        Echipa res = echipaService.update(newEntity);
        if(res == null) {
            notifyObserversEchipa(new EchipaChangeEvent(ChangeEventType.UPDATE, newEntity, oldEntity));
        }
        return res;
    }



    public Meci findOneMeci(String id){
        return meciService.findOne(id);
    }
    public Iterable<Meci> findAllMeci() {
        return meciService.findAll();
    }
    public Meci saveMeci(Meci entity) throws ValidationException {
        Meci r = meciService.save(entity);
        if(r == null) {
            notifyObserversMeci(new MeciChangeEvent(ChangeEventType.ADD, entity));
        }
        return r;
    }
    public Meci deleteMeci(String id){
        Meci r = meciService.delete(id);
        if(r != null) {
            notifyObserversMeci(new MeciChangeEvent(ChangeEventType.DELETE, r));
        }
        return r;
    }
    public Meci updateMeci(Meci newEntity){
        Meci oldEntity = meciService.findOne(newEntity.getId());
        Meci res = meciService.update(newEntity);
        if(res == null) {
            notifyObserversMeci(new MeciChangeEvent(ChangeEventType.UPDATE, newEntity, oldEntity));
        }
        return res;
    }




    @Override
    public void addObserverBilet(BiletObserver e) {
        biletObservers.add(e);
    }
    @Override
    public void removeObserverBilet(BiletObserver e) {
        biletObservers.remove(e);
    }
    @Override
    public void notifyObserversBilet(BiletChangeEvent t) {
        biletObservers.forEach(x->x.updateBilet(t));
    }

    @Override
    public void addObserverClient(ClientObserver e) {
        clientObservers.add(e);
    }
    @Override
    public void removeObserverClient(ClientObserver e) {
        clientObservers.remove(e);
    }
    @Override
    public void notifyObserversClient(ClientChangeEvent t) {
        clientObservers.forEach(x->x.updateClient(t));
    }

    @Override
    public void addObserverEchipa(EchipaObserver e) {
        echipaObservers.add(e);
    }
    @Override
    public void removeObserverEchipa(EchipaObserver e) {
        echipaObservers.remove(e);
    }
    @Override
    public void notifyObserversEchipa(EchipaChangeEvent t) {
        echipaObservers.forEach(x->x.updateEchipa(t));
    }

    @Override
    public void addObserverMeci(MeciObserver e) {
        meciObservers.add(e);
    }
    @Override
    public void removeObserverMeci(MeciObserver e) {
        meciObservers.remove(e);
    }
    @Override
    public void notifyObserversMeci(MeciChangeEvent t) {
        meciObservers.forEach(x->x.updateMeci(t));
    }
}
