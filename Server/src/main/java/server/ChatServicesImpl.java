package server;


import model.loggers.Log;
import repos.*;
import services.IObserver;
import services.IServices;
import model.domain.*;
import services.ServicesException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ChatServicesImpl implements IServices {

    private ClientDataBaseRepository userRepository; //TODO: not so generic...
    private MeciDataBaseRepository meciRepository;
    private BiletDataBaseRepository biletRepository;
    private EchipaDataBaseRepository echipaRepository;
    private Map<String, IObserver> loggedClients;

    public ChatServicesImpl(ClientDataBaseRepository uRepo, MeciDataBaseRepository mRepo, BiletDataBaseRepository bRepo, EchipaDataBaseRepository eRepo) {
        userRepository = uRepo;
        meciRepository = mRepo;
        echipaRepository = eRepo;
        biletRepository = bRepo;
        loggedClients = new ConcurrentHashMap<>();
    }


    private static final int defaultThreadsNo = 1;

    private void notifyTicketsBought(Meci meci) throws ServicesException {
        Iterable<Client> users = userRepository.findAll();
        Log.logger.trace("FULL SERVER: login RECEIVED !OBSERVER! COMMAND @"+ LocalDate.now());
        //ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);


        for(Client us :users) {
            IObserver chatClient = loggedClients.get(us.getId());
            if (chatClient != null)
                try {
                    System.out.println("Notifying [" + us.getId() + "] tickets were bought for match [" + meci.getId() + "].");
                    chatClient.ticketsSold(meci);
                    Log.logger.trace("FULL SERVER: login SENT COMMAND TO SERVER PROXY @"+ LocalDate.now());
                } catch (ServicesException e) {
                    System.out.println("Error notifying friend " + e);
                }
        }


        /*for(Client us :users){
            IObserver chatClient=loggedClients.get(us.getId());
            if (chatClient!=null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying ["+us.getId()+"] tickets were bought for match ["+meci.getId()+"].");
                        chatClient.ticketsSold(meci);
                    } catch (ServicesException e) {
                        System.out.println("Error notifying friend " + e);
                    }
                });

        }
        executor.shutdown();*/
    }


    public synchronized void login(Client user, IObserver client) throws ServicesException {
        Log.logger.trace("FULL SERVER: login RECEIVED COMMAND @"+ LocalDate.now());
        Client userR = userRepository.findClientByCredentials(user.getId(), user.getPassword());
        if (userR != null) {
            if (loggedClients.get(user.getId()) != null)
                throw new ServicesException("User already logged in.");
            loggedClients.put(user.getId(), client);
        } else
            throw new ServicesException("Authentication failed.");
        Log.logger.trace("FULL SERVER: login SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
    }



    public synchronized void logout(Client user, IObserver client) throws ServicesException {
        Log.logger.trace("FULL SERVER: logout RECEIVED COMMAND @"+ LocalDate.now());
        IObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new ServicesException("User "+user.getId()+" is not logged in.");
        Log.logger.trace("FULL SERVER: logout SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
    }


    @Override
    public synchronized Meci[] findAllMeci() throws ServicesException {
        Log.logger.trace("FULL SERVER: findAllMeci RECEIVED COMMAND @"+ LocalDate.now());
        System.out.println("_DEBUG: FIND MATCHES");
        List<Meci> fromDBresult = StreamSupport.stream(meciRepository.findAll().spliterator(), false).collect(Collectors.toList());

        Meci[] meciuri = new Meci[fromDBresult.size()];
        for (int i = 0; i < fromDBresult.size(); i++) {
            meciuri[i] = fromDBresult.get(i);
        }
        Log.logger.trace("FULL SERVER: findAllMeci SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
        return meciuri;
    }


    @Override
    public synchronized Bilet[] findAllBilet() throws ServicesException {
        Log.logger.trace("FULL SERVER: findAllBilet RECEIVED COMMAND @"+ LocalDate.now());
        System.out.println("_DEBUG: GET ALL BILETE");
        List<Bilet> fromDBresult = StreamSupport.stream(biletRepository.findAll().spliterator(), false).collect(Collectors.toList());

        Bilet[] bilete = new Bilet[fromDBresult.size()];
        for (int i = 0; i < fromDBresult.size(); i++) {
            bilete[i] = fromDBresult.get(i);
        }
        Log.logger.trace("FULL SERVER: findAllBilet SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
        return bilete;
    }


    @Override
    public synchronized void ticketsSold(Meci meci) throws ServicesException {
        Log.logger.trace("FULL SERVER: ticketsSold RECEIVED COMMAND @"+ LocalDate.now());
        Meci ret = meciRepository.update(meci);
        if (ret == null)
            throw new ServicesException("MECI NOT FOUND IN DB TO BE UPDATED!");
        notifyTicketsBought(meci);
        Log.logger.trace("FULL SERVER: ticketsSold SENT OBSERVER COMMAND TO notifyTicketsBought @"+ LocalDate.now());
    }


    @Override
    public synchronized void updateBilet(Bilet bilet) throws ServicesException {
        Log.logger.trace("FULL SERVER: updateBilet RECEIVED COMMAND @"+ LocalDate.now());
        Bilet ret = biletRepository.update(bilet);
        if (ret == null)
            throw new ServicesException("BILET NOT FOUND IN DB TO BE UPDATED!");
        Log.logger.trace("FULL SERVER: updateBilet SUCCESSFUL @"+ LocalDate.now());
    }


    @Override
    public synchronized Echipa findOneEchipa(String id) throws ServicesException {
        Log.logger.trace("FULL SERVER: findOneEchipa RECEIVED COMMAND @"+ LocalDate.now());
        Log.logger.trace("FULL SERVER: findOneEchipa SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
        return echipaRepository.findOne(id);
    }
}
