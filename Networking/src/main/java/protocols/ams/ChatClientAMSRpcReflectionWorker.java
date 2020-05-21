package protocols.ams;

import basket.model.domain.Client;
import basket.model.domain.Meci;
import dto.DTOUtils;
import dto.MeciDTO;
import dto.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocols.*;
import services.IChatServicesAMS;
import services.ServicesException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;


public class ChatClientAMSRpcReflectionWorker implements Runnable {
    private static final Logger logger = LogManager.getLogger(ChatClientAMSRpcReflectionWorker.class);
    private IChatServicesAMS server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ChatClientAMSRpcReflectionWorker(IChatServicesAMS server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                System.out.println("Received request");
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException|ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }





    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        /*Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
            e.printStackTrace();
        }
        return response;*/
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.LOGIN @"+ LocalDate.now());
            System.out.println("Login request ..."+request.type());
            UserDTO udto=(UserDTO)request.data();
            Client user= DTOUtils.getFromDTO(udto);
            try {
                server.login(user);
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
                server.logout(user);
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
                server.ticketsSold(meci, loggedInClient); // this is the response of the TICKETS_SOLD request
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

    /*private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());
        UserDTO udto=(UserDTO)request.data();
        User user=DTOUtils.getFromDTO(udto);
        try {
            server.login(user);
            return okResponse;
        } catch (ChatException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        System.out.println("Logout request...");
        UserDTO udto=(UserDTO)request.data();
        User user=DTOUtils.getFromDTO(udto);
        try {
            server.logout(user);
            connected=false;
            return okResponse;

        } catch (ChatException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleSEND_MESSAGE_ALL(Request request) {
        System.out.println("SendMessageAllRequest ...");
        MessageDTO mdto=(MessageDTO)request.data();
        Message message=DTOUtils.getFromDTO(mdto);
        try {
            server.sendMessageToAll(message);
            return okResponse;
        } catch (ChatException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_LOGGED_USERS(Request request) {
        System.out.println("GetLoggedFriends Request ...");
        try {
            User[] friends=server.getLoggedUsers();
            UserDTO[] frDTO=DTOUtils.getDTO(friends);
            return new Response.Builder().type(ResponseType.GET_LOGGED_USERS).data(frDTO).build();
        } catch (ChatException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }*/

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }
}
