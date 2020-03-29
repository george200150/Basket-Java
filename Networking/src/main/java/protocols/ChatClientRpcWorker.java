package protocols;



import dto.*;
import model.domain.Bilet;
import model.domain.Client;
import model.domain.Echipa;
import model.domain.Meci;
import model.loggers.Log;
import services.IObserver;
import services.IServices;
import services.ServicesException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;


public class ChatClientRpcWorker implements Runnable, IObserver {
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
            Log.logger.traceExit("PROXY SERVER: SUCCESSFUL ChatClientRpcWorker @"+ LocalDate.now());
        } catch (IOException e) {
            e.printStackTrace();
            Log.logger.traceExit("PROXY SERVER: FAILED ChatClientRpcWorker @"+ LocalDate.now());
        }
    }


    public void run() { //TODO: aici intra din proxy, de la sendRequest()
        while(connected){
            try {
                Object request=input.readObject();
                Log.logger.trace("PROXY SERVER: run RECEIVED REQUEST @"+ LocalDate.now());
                Response response=handleRequest((Request)request); //TODO: de aici se duce in orice handle request, dupa tip (functile asincrone din server - ChatServerImpl)
                if (response!=null){
                    sendResponse(response);
                    Log.logger.trace("PROXY SERVER: run SENT RESPONSE @"+ LocalDate.now());
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
            Log.logger.traceExit("PROXY SERVER: run : BEGIN SHUTDOWN @"+ LocalDate.now());
            input.close();
            output.close();
            connection.close();
            Log.logger.traceExit("PROXY SERVER: run : COMPLETED SHUTDOWN @"+ LocalDate.now());
        } catch (IOException e) {
            Log.logger.traceExit("PROXY SERVER: run : IOException @"+ LocalDate.now());
            System.out.println("Error "+e);
        }
    }


    @Override
    public void ticketsSold(Meci meci) throws ServicesException {
        MeciDTO mecidto = DTOUtils.getDTO(meci);
        Response resp = new Response.Builder().type(ResponseType.TICKETS_SOLD).data(mecidto).build();
        Log.logger.trace("PROXY SERVER: ticketsSold BUILT RESPONSE @"+ LocalDate.now());
        System.out.println("Tickets sold for match " + meci);
        try {
            sendResponse(resp);
            Log.logger.traceExit("PROXY SERVER: ticketsSold SENT RESPONSE @"+ LocalDate.now(), resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();


    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            Log.logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.LOGIN @"+ LocalDate.now());
            System.out.println("Login request ..."+request.type());
            UserDTO udto=(UserDTO)request.data();
            Client user= DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                Log.logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.login @"+ LocalDate.now());
                return okResponse;
            } catch (ServicesException e) {
                connected=false;
                Log.logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.LOGIN @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            Log.logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.LOGOUT @"+ LocalDate.now());
            System.out.println("Logout request");
            UserDTO udto=(UserDTO)request.data();
            Client user= DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                Log.logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.logout @"+ LocalDate.now());
                return okResponse;

            } catch (ServicesException e) {
                Log.logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.LOGOUT @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_MATCHES){
            Log.logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.GET_MATCHES @"+ LocalDate.now());
            System.out.println("Get Matches request");
            try {
                Meci[] meciuri = server.findAllMeci();
                MeciDTO[] mecidtos = DTOUtils.getDTO(meciuri);
                Log.logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.findAllMeci @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.GET_MATCHES).data(mecidtos).build(); // TODO: this is the response of the GET_MATCHES request

            } catch (ServicesException e) {
                Log.logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.GET_MATCHES @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }


        if (request.type()== RequestType.GET_TICKETS){
            Log.logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.GET_TICKETS @"+ LocalDate.now());
            System.out.println("Get Matches request");
            try {
                Bilet[] bilete = server.findAllBilet();
                BiletDTO[] biletedtos = DTOUtils.getDTO(bilete);
                Log.logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.findAllBilet @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.GET_TICKETS).data(biletedtos).build(); // TODO: this is the response of the GET_MATCHES request

            } catch (ServicesException e) {
                Log.logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.GET_TICKETS @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }


        if (request.type()== RequestType.GET_ECHIPA){
            Log.logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.GET_ECHIPA @"+ LocalDate.now());
            System.out.println("Get Matches request");
            String id = (String) request.data();
            try {
                Echipa echipa = server.findOneEchipa(id);
                EchipaDTO echipaDto = DTOUtils.getDTO(echipa);
                Log.logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.findOneEchipa @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.GET_ECHIPA).data(echipaDto).build(); // TODO: this is the response of the GET_ECHIPA request
                //return okResponse;

            } catch (ServicesException e) {
                Log.logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.GET_ECHIPA @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }


        if (request.type()== RequestType.UPDATE_BILET){
            Log.logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.GET_ECHIPA @"+ LocalDate.now());
            System.out.println("Update Bilet request");
            BiletDTO biletDTO = (BiletDTO)request.data();
            Bilet bilet = DTOUtils.getFromDTO(biletDTO);
            try {
                server.updateBilet(bilet); // TODO: this is the response of the UPDATE_BILET request
                Log.logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.updateBilet @"+ LocalDate.now());
                return okResponse;

            } catch (ServicesException e) {
                Log.logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.UPDATE_BILET @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }


        if (request.type()== RequestType.TICKETS_SOLD){
            Log.logger.trace("PROXY SERVER: handleRequest RECEIVED REQUEST type==RequestType.TICKETS_SOLD @"+ LocalDate.now());
            System.out.println("TICKETS_SOLD update meci request");
            MeciDTO meciDTO = (MeciDTO)request.data();
            Meci meci = DTOUtils.getFromDTO(meciDTO);
            try {
                server.ticketsSold(meci); // TODO: this is the response of the TICKETS_SOLD request
                Log.logger.trace("PROXY SERVER: handleRequest SENT COMMAND TO SERVER server.ticketsSold @"+ LocalDate.now());
                //TODO: CRED CA NU MAI TREBUIE FACUT ASTA, DE VREME CE AVEM DEJA O FUNCTIE DE TIP OBSERVER CARE VA FACE BROADCAST LA RESPONSE...
                //return new Response.Builder().type(ResponseType.TICKETS_SOLD).data(meciDTO).build();
                //las asta aici doar pentru un test...
                //TODO: CRED CA NU MAI TREBUIE FACUT ASTA, DE VREME CE AVEM DEJA O FUNCTIE DE TIP OBSERVER CARE VA FACE BROADCAST LA RESPONSE...
                //TODO: CRED CA NU MAI TREBUIE FACUT ASTA, DE VREME CE AVEM DEJA O FUNCTIE DE TIP OBSERVER CARE VA FACE BROADCAST LA RESPONSE...

            } catch (ServicesException e) {
                Log.logger.traceExit("PROXY SERVER: FAILED handleRequest type==RequestType.TICKETS_SOLD @"+ LocalDate.now());
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        Log.logger.traceExit("PROXY SERVER: RETURN RESPONSE handleRequest @"+ LocalDate.now() + "WARNING: should never reach this point: request type not found !!!", response);
        return response;
    }


    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
        Log.logger.traceExit("PROXY SERVER: SUCCESSFUL sendResponse @"+ LocalDate.now(), response);
    }
}
