package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repos.*;
import model.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ChatServicesImpl {
    private static final Logger logger = LogManager.getLogger(ChatServicesImpl.class);
    private ClientDataBaseRepository userRepository;
    private MeciDataBaseRepository meciRepository;
    //private BiletDataBaseRepository biletRepository;
    private BiletHBMRepository biletRepository;

    //public ChatServicesImpl(ClientDataBaseRepository uRepo, MeciDataBaseRepository mRepo, BiletDataBaseRepository bRepo) {
    public ChatServicesImpl(ClientDataBaseRepository uRepo, MeciDataBaseRepository mRepo, BiletHBMRepository bRepo) {
        userRepository = uRepo;
        meciRepository = mRepo;
        biletRepository = bRepo;
    }

    public synchronized Client loginThrift(String username, String password) {
        return userRepository.findClientByCredentials(username, password);
    }

    public synchronized List<MeciDTO> findAllMeciWithTickets() {
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
        return getMeciDTOS(meciuri);
    }

    public synchronized List<MeciDTO> findAllMeci() {
        logger.trace("FULL SERVER: findAllMeci RECEIVED COMMAND @"+ LocalDate.now());
        System.out.println("_DEBUG: FIND MATCHES");
        List<Meci> fromDBresult = StreamSupport.stream(meciRepository.findAll().spliterator(), false).collect(Collectors.toList());

        Meci[] meciuri = new Meci[fromDBresult.size()];
        for (int i = 0; i < fromDBresult.size(); i++) {
            meciuri[i] = fromDBresult.get(i);
        }
        logger.trace("FULL SERVER: findAllMeci SENT RETURN TO SERVER PROXY @"+ LocalDate.now());
        return getMeciDTOS(meciuri);
    }

    private List<MeciDTO> getMeciDTOS(Meci[] meciuri) {
        List<MeciDTO> meciDTOS = new ArrayList<>();
        long TICKS_AT_EPOCH = 621355968000000000L;
        for (Meci meci : meciuri) {
            TipMeciDTO tipDTO = TipMeciDTO.findByValue(meci.getTip().getNumVal());
            long tick = meci.getDate().getTime()*10000 + TICKS_AT_EPOCH;
            MeciDTO dto = new MeciDTO(meci.getId(), meci.getHome(), meci.getAway(), tick, tipDTO, meci.getNumarBileteDisponibile());
            meciDTOS.add(dto);
        }
        return meciDTOS;
    }

    //no need to logout, as there is no dict of clients, as before...

    public synchronized void ticketsSold(Meci meci, Client loggedInClient) {
        logger.trace("FULL SERVER: ticketsSold RECEIVED COMMAND @" + LocalDate.now());
        Meci ret = meciRepository.update(meci);

        int delta = ret.getNumarBileteDisponibile() - meci.getNumarBileteDisponibile(); // number of tickets bought
        List<Bilet> goodTickets = StreamSupport.stream(biletRepository.findAll().spliterator(), false).filter(x -> x.getIdClient() == null && x.getIdMeci().equals(meci.getId())).collect(Collectors.toList());
        // !!! this function @requires enough not assigned tickets to be in the database when called. else @throws IndexOutOfBoundsException
        for (int i = 0; i < delta; i++) {
            Bilet bilet = goodTickets.get(i);
            bilet.setIdClient(loggedInClient.getId());
            bilet.setNumeClient(loggedInClient.getNume());
            biletRepository.update(bilet);
        }
    }
}
