package apacheThrift;

import model.domain.*;
import model.validators.BiletValidator;
import model.validators.ClientValidator;
import model.validators.MeciValidator;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import repos.BiletDataBaseRepository;
import repos.BiletHBMRepository;
import repos.ClientDataBaseRepository;
import repos.MeciDataBaseRepository;
import server.ChatServicesImpl;

import java.util.*;


public class TransformerHandler implements TransformerService.Iface {

    private MeciDataBaseRepository meciDataBaseRepository;
    private ClientDataBaseRepository clientDataBaseRepository;
    //private BiletDataBaseRepository biletDataBaseRepository;
    private BiletHBMRepository biletDataBaseRepository;
    private ChatServicesImpl chatServerImpl;
    private ArrayList<Client> myClients = new ArrayList<>();

    public TransformerHandler() {
        clientDataBaseRepository = new ClientDataBaseRepository(ClientValidator.getInstance());
        meciDataBaseRepository = new MeciDataBaseRepository(MeciValidator.getInstance());
        //biletDataBaseRepository = new BiletDataBaseRepository(BiletValidator.getInstance());
        biletDataBaseRepository = new BiletHBMRepository(BiletValidator.getInstance());
        chatServerImpl = new ChatServicesImpl(clientDataBaseRepository, meciDataBaseRepository, biletDataBaseRepository);
    }

    public void shutdown(){
        BiletHBMRepository.close();
    }

    private void alertClients() {
        for (Client myClient : myClients) {
            try {
                TTransport transport = new TSocket(myClient.getHost(), myClient.getPort());
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                MessageService.Client client = new MessageService.Client(protocol);
                client.sendMessage("notify");
                transport.close();
            } catch (TException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String login(String id, String username, String password, String host, int port) throws TException {
        //System.out.println("Client's server host: " + host);
        //System.out.println("Client's server port: " + port);
        Random random = new Random();
        String sessionId = Double.toString(random.nextDouble());;
        myClients.add(new Client(id, sessionId, password, username, host, port));

        if (chatServerImpl.loginThrift(username, password)) {
            return "ok";
        } else {
            return "error";
        }
    }

    @Override
    public List<MeciDTO> findAllMeciWithTickets() throws TException {
        return chatServerImpl.findAllMeciWithTickets();
    }

    @Override
    public List<MeciDTO> findAllMeci() throws TException {
        return chatServerImpl.findAllMeci();
    }

    @Override
    public void ticketsSold(MeciDTO meci, ClientDTO client) throws TException {
        TipMeci tipNonDTO = null;
        int enumValue = meci.getTip().getValue();
        if(enumValue == 1) tipNonDTO = TipMeci.CALIFICARE;
        else if (enumValue == 2) tipNonDTO = TipMeci.SAISPREZECIME;
        else if (enumValue == 3) tipNonDTO = TipMeci.OPTIME;
        else if (enumValue == 4) tipNonDTO = TipMeci.SFERT;
        else if (enumValue == 5) tipNonDTO = TipMeci.SEMIFINALA;
        else if (enumValue == 6) tipNonDTO = TipMeci.FINALA;

        long TICKS_AT_EPOCH = 621355968000000000L;
        Date date = new Date((meci.getDate() - TICKS_AT_EPOCH)/10000);

        Meci meciNonDTO = new Meci(meci.getId(),meci.getHome(),meci.getAway(),date,tipNonDTO,meci.getNumarBileteDisponibile());
        Client loggedInClient = new Client(client.id, client.sessionId, client.password,client.nume,client.ip,client.port);
        chatServerImpl.ticketsSold(meciNonDTO, loggedInClient);
        alertClients();
    }
}
