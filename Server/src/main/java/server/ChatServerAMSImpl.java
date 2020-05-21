package server;

import basket.model.domain.Bilet;
import basket.model.domain.Client;
import basket.model.domain.Meci;
import basket.repos.BiletDataBaseRepository;
import basket.repos.ClientDataBaseRepository;
import basket.repos.MeciDataBaseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IChatNotificationService;
import services.IChatServicesAMS;
import services.ServicesException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ChatServerAMSImpl implements IChatServicesAMS {
    private static final Logger logger = LogManager.getLogger(ChatServerAMSImpl.class);
    private ClientDataBaseRepository userRepository;
    private MeciDataBaseRepository meciRepository;
    private BiletDataBaseRepository biletRepository;

    private Map<String, Client> loggedClients;
    private IChatNotificationService notificationService;


    public ChatServerAMSImpl(ClientDataBaseRepository uRepo, MeciDataBaseRepository mRepo, BiletDataBaseRepository bRepo, IChatNotificationService service) {
        userRepository = uRepo;
        meciRepository = mRepo;
        biletRepository = bRepo;
        loggedClients = new ConcurrentHashMap<>();
        notificationService = service;
    }


    public synchronized void login(Client user) throws ServicesException {
        logger.trace("FULL SERVER: login RECEIVED COMMAND @"+ LocalDate.now());
        Client userR = userRepository.findClientByCredentials(user.getId(), user.getPassword());
        if (userR != null) {
            if (loggedClients.get(user.getId()) != null)
                throw new ServicesException("User already logged in.");
            loggedClients.put(user.getId(), user);
        } else
            throw new ServicesException("Authentication failed.");
        logger.trace("FULL SERVER: login SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
    }


    public synchronized void logout(Client user) throws ServicesException {
        logger.trace("FULL SERVER: logout RECEIVED COMMAND @"+ LocalDate.now());
        Client localClient=loggedClients.remove(user.getId());
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
        List<Bilet> goodTickets = StreamSupport.stream(biletRepository.findAll().spliterator(), false).filter(x ->x.getIdClient() == null).collect(Collectors.toList());
        for (int i = 0; i < delta; i++) {
            Bilet bilet = goodTickets.get(i);
            bilet.setIdClient(loggedInClient.getId());
            bilet.setNumeClient(loggedInClient.getId());
            Bilet res = biletRepository.update(bilet);
            if (res == null)
                throw new ServicesException("BILET NOT FOUNT IN DB TO BE UPDATED!");
        }

        for (Client client: loggedClients.values()) {
            notificationService.newMeciUpdate(meci);
        }

        logger.trace("FULL SERVER: ticketsSold SENT OBSERVER COMMAND TO notifyTicketsBought @"+ LocalDate.now());
    }

}
