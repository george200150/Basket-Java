package protocols;

import dto.*;
import model.domain.Client;
import model.domain.Meci;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IObserver;
import services.IServices;
import services.ServicesException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;


public class ChatClientRpcWorker implements Runnable, IObserver {
    static final Logger logger = LogManager.getLogger(ChatClientRpcWorker.class);
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ChatClientRpcWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
            logger.traceExit("PROXY SERVER: SUCCESSFUL ChatClientRpcWorker @"+ LocalDate.now());
        } catch (IOException e) {
            e.printStackTrace();
            logger.traceExit("PROXY SERVER: FAILED ChatClientRpcWorker @"+ LocalDate.now());
        }
    }


    public void run() { //TODO: aici intra din proxy, de la sendRequest()
        while(connected){
            try {
                Object request=input.readObject();
                logger.trace("PROXY SERVER: run RECEIVED REQUEST @"+ LocalDate.now());
                Response response=handleRequest((Request)request); //TODO: de aici se duce in orice handle request, dupa tip (functile asincrone din server - ChatServerImpl)
                if (response!=null){
                    sendResponse(response);
                    logger.trace("PROXY SERVER: run SENT RESPONSE @"+ LocalDate.now());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            logger.traceExit("PROXY SERVER: run : BEGIN SHUTDOWN @"+ LocalDate.now());
            input.close();
            output.close();
            connection.close();
            logger.traceExit("PROXY SERVER: run : COMPLETED SHUTDOWN @"+ LocalDate.now());
        } catch (IOException e) {
            logger.traceExit("PROXY SERVER: run : IOException @"+ LocalDate.now());
            System.out.println("Error "+e);
        }
    }


    @Override
    public void notifyTicketsSold(Meci meci) throws ServicesException {
        MeciDTO mecidto = DTOUtils.getDTO(meci);
        Response resp = new Response.Builder().type(ResponseType.UPDATE_TICKETS_IN_CLIENT_MATCH).data(mecidto).build();
        logger.trace("PROXY SERVER: ticketsSold BUILT RESPONSE @"+ LocalDate.now());
        System.out.println("Tickets sold for match " + meci);
        try {
            sendResponse(resp);
            logger.traceExit("PROXY SERVER: ticketsSold SENT RESPONSE @"+ LocalDate.now(), resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();


    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.LOGIN @"+ LocalDate.now());
            System.out.println("Login request ..."+request.type());
            UserDTO udto=(UserDTO)request.data();
            Client user= DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.login @"+ LocalDate.now());
                return okResponse;
            } catch (ServicesException e) {
                connected=false;
                logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.LOGIN @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.LOGOUT @"+ LocalDate.now());
            System.out.println("Logout request");
            UserDTO udto=(UserDTO)request.data();
            Client user= DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.logout @"+ LocalDate.now());
                return okResponse;

            } catch (ServicesException e) {
                logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.LOGOUT @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_MATCHES){
            logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.GET_MATCHES @"+ LocalDate.now());
            System.out.println("Get Matches request");
            try {
                Meci[] meciuri = server.findAllMeci();
                MeciDTO[] mecidtos = DTOUtils.getDTO(meciuri);
                logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.findAllMeci @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.GET_MATCHES).data(mecidtos).build(); // TODO: this is the response of the GET_MATCHES request

            } catch (ServicesException e) {
                logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.GET_MATCHES @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }


        if (request.type()== RequestType.GET_MATCHES_W_TICKETS){
            logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.GET_MATCHES_W_TICKETS @"+ LocalDate.now());
            System.out.println("Get Matches request");
            try {
                Meci[] meciuri = server.findAllMeciWithTickets();
                MeciDTO[] mecidtos = DTOUtils.getDTO(meciuri);
                logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.findAllMeci @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.GET_MATCHES_W_TICKETS).data(mecidtos).build(); // TODO: this is the response of the GET_MATCHES_W_TICKETS request

            } catch (ServicesException e) {
                logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.GET_MATCHES_W_TICKETS @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }


        if (request.type()== RequestType.TICKETS_SOLD) {
            logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.TICKETS_SOLD @" + LocalDate.now());
            System.out.println("TICKETS_SOLD update meci request");
            Object[] data = (Object[]) request.data();
            MeciDTO meciDTO = (MeciDTO) data[0];
            UserDTO client = (UserDTO) data[1];
            Meci meci = DTOUtils.getFromDTO(meciDTO);
            Client loggedInClient = DTOUtils.getFromDTO(client);
            try {
                server.ticketsSold(meci, loggedInClient); // TODO: this is the response of the TICKETS_SOLD request
                logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.ticketsSold @"+ LocalDate.now());

                return new Response.Builder().type(ResponseType.TICKETS_SOLD).data(meciDTO).build();

            } catch (ServicesException e) {
                logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.TICKETS_SOLD @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        logger.traceExit("PROXY SERVER: RETURN RESPONSE handleRequest @"+ LocalDate.now() + " WARNING: should never reach this point: request type not found !!!", response);
        return response;
    }


    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
        logger.traceExit("PROXY SERVER: SUCCESSFUL sendResponse @"+ LocalDate.now(), response);
    }
}
