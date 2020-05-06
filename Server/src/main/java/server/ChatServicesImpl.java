package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    static final Logger logger = LogManager.getLogger(ChatServicesImpl.class);
    private ClientDataBaseRepository userRepository; //TODO: not so generic...
    private MeciDataBaseRepository meciRepository;
<<<<<<< Updated upstream
    private BiletDataBaseRepository biletRepository;
    private Map<String, IObserver> loggedClients;
=======
    //private BiletDataBaseRepository biletRepository;
    private BiletHBMRepository biletRepository;
>>>>>>> Stashed changes

    //public ChatServicesImpl(ClientDataBaseRepository uRepo, MeciDataBaseRepository mRepo, BiletDataBaseRepository bRepo) {
    public ChatServicesImpl(ClientDataBaseRepository uRepo, MeciDataBaseRepository mRepo, BiletHBMRepository bRepo) {
        userRepository = uRepo;
        meciRepository = mRepo;
        biletRepository = bRepo;
        loggedClients = new ConcurrentHashMap<>();
    }

    private static final int defaultThreadsNo = 5;

    private void notifyTicketsBought(Meci meci) throws ServicesException {
        Iterable<Client> users = userRepository.findAll();
        logger.trace("FULL SERVER: login RECEIVED !OBSERVER! COMMAND @"+ LocalDate.now());
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);

        for(Client us :users) {
            IObserver chatClient = loggedClients.get(us.getId());
            if (chatClient != null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + us.getId() + "] tickets were bought for match [" + meci.getId() + "].");
                        chatClient.notifyTicketsSold(meci);
                    } catch (ServicesException e) {
                        System.out.println("Error notifying friend " + e);
                    }
                });

        }
        executor.shutdown();
    }


    public synchronized void login(Client user, IObserver client) throws ServicesException {
        logger.trace("FULL SERVER: login RECEIVED COMMAND @"+ LocalDate.now());
        Client userR = userRepository.findClientByCredentials(user.getId(), user.getPassword());
        if (userR != null) {
            if (loggedClients.get(user.getId()) != null)
                throw new ServicesException("User already logged in.");
            loggedClients.put(user.getId(), client);
        } else
            throw new ServicesException("Authentication failed.");
        logger.trace("FULL SERVER: login SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
    }



    public synchronized void logout(Client user, IObserver client) throws ServicesException {
        logger.trace("FULL SERVER: logout RECEIVED COMMAND @"+ LocalDate.now());
        IObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new ServicesException("User "+user.getId()+" is not logged in.");
        logger.trace("FULL SERVER: logout SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
    }

    @Override
    public synchronized Meci[] findAllMeciWithTickets() throws ServicesException{
        logger.trace("FULL SERVER: findAllMeciWithTickets RECEIVED COMMAND @"+ LocalDate.now());
        System.out.println("_DEBUG: FIND MATCHES WITH TICKETS");
        List<Meci> meciAll = StreamSupport.stream(meciRepository.findAll().spliterator(), false).collect(Collectors.toList());

        List<Meci> meciList = StreamSupport
                .stream(meciAll.spliterator(), false)
                .filter(x-> x.getNumarBileteDisponibile() > 0)
                .sorted((m1,m2)-> m2.getNumarBileteDisponibile() - m1.getNumarBileteDisponibile())
                .collect(Collectors.toList());

        Meci[] meciuri = new Meci[meciList.size()];
        for (int i = 0; i < meciList.size(); i++) {
            meciuri[i] = meciList.get(i);
        }
        logger.trace("FULL SERVER: findAllMeciWithTickets SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
        return meciuri;
    }

    @Override
    public synchronized Meci[] findAllMeci() throws ServicesException {
        logger.trace("FULL SERVER: findAllMeci RECEIVED COMMAND @"+ LocalDate.now());
        System.out.println("_DEBUG: FIND MATCHES");
        List<Meci> fromDBresult = StreamSupport.stream(meciRepository.findAll().spliterator(), false).collect(Collectors.toList());

        Meci[] meciuri = new Meci[fromDBresult.size()];
        for (int i = 0; i < fromDBresult.size(); i++) {
            meciuri[i] = fromDBresult.get(i);
        }
        logger.trace("FULL SERVER: findAllMeci SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
        return meciuri;
    }


    @Override
    public synchronized void ticketsSold(Meci meci, Client loggedInClient) throws ServicesException {
        logger.trace("FULL SERVER: ticketsSold RECEIVED COMMAND @"+ LocalDate.now());
        Meci ret = meciRepository.update(meci);
        if (ret == null)
            throw new ServicesException("MECI NOT FOUND IN DB TO BE UPDATED!");
        int delta = ret.getNumarBileteDisponibile() - meci.getNumarBileteDisponibile(); // number of tickets bought
<<<<<<< Updated upstream
        List<Bilet> goodTickets = StreamSupport.stream(biletRepository.findAll().spliterator(), false).filter(x ->x.getIdClient() == null).collect(Collectors.toList());
        for (int i = 0; i < delta; i++) {
            Bilet bilet = goodTickets.get(i);
            bilet.setIdClient(loggedInClient.getId());
            bilet.setNumeClient(loggedInClient.getId());
            Bilet res = biletRepository.update(bilet);
            if (res == null)
                throw new ServicesException("BILET NOT FOUNT IN DB TO BE UPDATED!");
=======
        List<Bilet> goodTickets = StreamSupport.stream(biletRepository.findAll().spliterator(), false).filter(x -> x.getIdClient() == null && x.getIdMeci().equals(meci.getId())).collect(Collectors.toList());
        // !!! this function @requires enough not assigned tickets to be in the database when called. else @throws IndexOutOfBoundsException
        for (int i = 0; i < delta; i++) {
            Bilet bilet = goodTickets.get(i);
            bilet.setIdClient(loggedInClient.getId());
            bilet.setNumeClient(loggedInClient.getNume());
            biletRepository.update(bilet);
>>>>>>> Stashed changes
        }
        notifyTicketsBought(meci);
        logger.trace("FULL SERVER: ticketsSold SENT OBSERVER COMMAND TO notifyTicketsBought @"+ LocalDate.now());
    }

}
