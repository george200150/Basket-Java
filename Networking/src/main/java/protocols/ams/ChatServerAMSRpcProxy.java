package protocols.ams;

import dto.*;
import basket.model.domain.Meci;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocols.Request;
import protocols.RequestType;
import protocols.Response;
import protocols.ResponseType;
import services.*;
import basket.model.domain.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatServerAMSRpcProxy implements IChatServicesAMS {
    private static final Logger logger = LogManager.getLogger(ChatServerAMSRpcProxy.class);
    private String host;
    private int port;
    private Boolean isInitialized = false;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ChatServerAMSRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
        logger.trace("CLIENT SIDE PROXY INIT @"+LocalDate.now());
    }

    @Override
    public void login(Client user) throws ServicesException {
        if (!isInitialized) {
            initializeConnection();
            this.isInitialized = true;
        }
        UserDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        sendRequest(req);
        logger.trace("PROXY CLIENT: login SENT REQUEST @"+ LocalDate.now());
        Response response=readResponse();
        logger.trace("PROXY CLIENT: login RECEIVED RESPONSE @"+ LocalDate.now());
        if (response.type()== ResponseType.OK){
            logger.traceExit("PROXY CLIENT: SUCCESSFUL login @"+ LocalDate.now());
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            logger.traceExit("PROXY CLIENT: FAILED login @"+ LocalDate.now());
            throw new ServicesException(err);
        }
    }

    @Override
    public void logout(Client user) throws ServicesException {
        UserDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        logger.trace("PROXY CLIENT: logout SENT REQUEST @"+ LocalDate.now());
        Response response=readResponse();
        logger.trace("PROXY CLIENT: logout RECEIVED RESPONSE @"+ LocalDate.now());
        closeConnection();
        this.isInitialized = false;
        logger.traceExit("PROXY CLIENT: SUCCESSFUL logout @"+ LocalDate.now());
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            logger.traceExit("PROXY CLIENT: FAILED logout @"+ LocalDate.now());
            throw new ServicesException(err);
        }
    }

    @Override
    public Meci[] findAllMeciWithTickets() throws ServicesException{
        Request req = new Request.Builder().type(RequestType.GET_MATCHES_W_TICKETS).build();
        sendRequest(req);
        logger.trace("PROXY CLIENT: findAllMeciWithTickets SENT REQUEST @"+ LocalDate.now());
        Response response = readResponse();
        logger.trace("PROXY CLIENT: findAllMeciWithTickets RECEIVED RESPONSE @"+ LocalDate.now());
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            logger.traceExit("PROXY CLIENT: FAILED findAllMeciWithTickets @"+ LocalDate.now());
            throw new ServicesException(err);
        }
        MeciDTO[] meciDTOS = (MeciDTO[]) response.data();
        Meci[] meciuri = DTOUtils.getFromDTO(meciDTOS);
        logger.traceExit("PROXY CLIENT: SUCCESSFUL findAllMeciWithTickets @"+ LocalDate.now());
        return meciuri;
    }

    @Override
    public Meci[] findAllMeci() throws ServicesException {
        if(! isInitialized){
            initializeConnection();
            this.isInitialized = true;
        }
        Request req = new Request.Builder().type(RequestType.GET_MATCHES).build();
        sendRequest(req);
        logger.trace("PROXY CLIENT: findAllMeci SENT REQUEST @"+ LocalDate.now());
        Response response = readResponse();
        logger.trace("PROXY CLIENT: findAllMeci RECEIVED RESPONSE @"+ LocalDate.now());
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            logger.traceExit("PROXY CLIENT: FAILED findAllMeci @"+ LocalDate.now());
            throw new ServicesException(err);
        }
        MeciDTO[] meciDTOS = (MeciDTO[]) response.data();
        Meci[] meciuri = DTOUtils.getFromDTO(meciDTOS);
        logger.traceExit("PROXY CLIENT: SUCCESSFUL findAllMeci @"+ LocalDate.now());
        return meciuri;
    }


    @Override
    public void ticketsSold(Meci meci, Client loggedInClient) throws ServicesException{
        MeciDTO meciDTO = DTOUtils.getDTO(meci);
        UserDTO userDTO = DTOUtils.getDTO(loggedInClient);
        Object[] sendData = new Object[2];
        sendData[0] = meciDTO;
        sendData[1] = userDTO;
        Request req = new Request.Builder().type(RequestType.TICKETS_SOLD).data(sendData).build();
        sendRequest(req);
        logger.trace("PROXY CLIENT: ticketsSold SENT REQUEST @"+ LocalDate.now() + " WARNING: THIS FUNCTION HAS NO RESPONSE!!!");
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ServicesException(err);
        }
        logger.traceExit("PROXY CLIENT: SUCCESSFUL ticketsSold @"+ LocalDate.now()+ " WARNING: THIS FUNCTION HAS NO RESPONSE!!!");
    }


    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            logger.traceExit("PROXY CLIENT: SUCCESSFUL closeConnection @"+ LocalDate.now());
        } catch (IOException e) {
            logger.traceExit("PROXY CLIENT: FAILED closeConnection @"+ LocalDate.now());
            e.printStackTrace();
        }

    }


    private void sendRequest(Request request) throws ServicesException {

        if(! isInitialized){
            initializeConnection();
            this.isInitialized = true;
        }

        logger.traceEntry("NETWORKING FROM CLIENT PROXY TO SERVER: INITIALIZING sendRequest @" + LocalDate.now(), request);
        try {
            logger.debug("E S T E   P O S I B I L   S A     F I U   B L O C A T     D E     S C R I E R E     !!! - output.writeObject(request);");
            output.writeObject(request);
            logger.traceEntry("--- scriere: {}",request);
            output.flush();
            logger.traceExit("NETWORKING FROM CLIENT PROXY TO SERVER: SUCESSFUL sendRequest @" + LocalDate.now(), request);
        } catch (IOException e) {
            logger.traceExit("NETWORKING FROM CLIENT PROXY TO SERVER: FAILED sendRequest @"+ LocalDate.now());
            throw new ServicesException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ServicesException {
        Response response=null;
        logger.traceEntry("NETWORKING FROM CLIENT PROXY TO SERVER: INITIALIZING readResponse @" + LocalDate.now());
        try{
            logger.debug("E S T E   P O S I B I L   S A     F I U   B L O C A T     D E     C I T I R E     !!! - response=qresponses.take();");
            response=qresponses.take();
            logger.traceEntry("--- citire: {}",response);
            logger.traceExit("NETWORKING FROM CLIENT PROXY TO SERVER: SUCESSFUL qresponses.take readResponse {} @" + LocalDate.now(), response);
        } catch (InterruptedException e) {
            logger.traceExit("NETWORKING FROM CLIENT PROXY TO SERVER: FAILED readResponse @"+ LocalDate.now());
            e.printStackTrace();
        }
        return response;
    }


    private void initializeConnection() throws ServicesException {
        if (! this.isInitialized) {
            try {
                logger.traceEntry("PROXY CLIENT: INITIALIZING initializeConnection @" + LocalDate.now());
                connection = new Socket(host, port); //TODO: THIS WILL THROW EXCEPTION WHEN HOST IS UNREACHABLE (hot fix)
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                input = new ObjectInputStream(connection.getInputStream());
                finished = false;
                this.isInitialized = true;
                startReader();
                logger.traceExit("PROXY CLIENT: SUCCESSFUL initializeConnection @"+ LocalDate.now());
            } catch (IOException e) {
                logger.traceExit("PROXY CLIENT: FAILED initializeConnection @"+ LocalDate.now());
                e.printStackTrace();
            }
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private boolean isUpdate(Response response){
        return response.type() == ResponseType.UPDATE_TICKETS_IN_CLIENT_MATCH;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    logger.traceEntry("PROXY CLIENT: READING DATA FROM THE SERVER @"+ LocalDate.now(), response);
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        // pass (ActiveMQ handles this now)
                    }else {
                        try {
                            logger.traceEntry("PROXY CLIENT: TRACE qresponses.put((Response)response) @" + LocalDate.now(), response);
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    logger.traceExit("PROXY CLIENT: FAILED run (because other end of socket disconnected from server) : IOException {} @"+ LocalDate.now(), e);
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    logger.traceExit("PROXY CLIENT: FAILED run : ClassNotFoundException @"+ LocalDate.now(), e);
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
