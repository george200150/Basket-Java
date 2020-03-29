package network;

import dto.*;
import model.domain.Bilet;
import model.domain.Echipa;
import model.domain.Meci;
import model.loggers.Log;
import protocols.Request;
import protocols.RequestType;
import protocols.Response;
import protocols.ResponseType;
import services.IObserver;
import services.IServices;
import model.domain.Client;
import services.ServicesException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatServicesRpcProxy implements IServices {
    private String host;
    private int port;
    private Boolean isInitialized = false;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ChatServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
        Log.logger.trace("CLIENT SIDE PROXY INIT @"+LocalDate.now());
    }

    @Override
    public void login(Client user, IObserver client) throws ServicesException {
        //initializeConnection();
        UserDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        sendRequest(req);
        Log.logger.trace("PROXY CLIENT: login SENT REQUEST @"+ LocalDate.now());
        Response response=readResponse();
        Log.logger.trace("PROXY CLIENT: login RECEIVED RESPONSE @"+ LocalDate.now());
        if (response.type()== ResponseType.OK){
            this.client=client;
            Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL login @"+ LocalDate.now());
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            Log.logger.traceExit("PROXY CLIENT: FAILED login @"+ LocalDate.now());
            throw new ServicesException(err);
        }
    }

    @Override
    public void logout(Client user, IObserver client) throws ServicesException {
        UserDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Log.logger.trace("PROXY CLIENT: logout SENT REQUEST @"+ LocalDate.now());
        Response response=readResponse();
        Log.logger.trace("PROXY CLIENT: logout RECEIVED RESPONSE @"+ LocalDate.now());
        closeConnection();
        Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL logout @"+ LocalDate.now());
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            Log.logger.traceExit("PROXY CLIENT: FAILED logout @"+ LocalDate.now());
            throw new ServicesException(err);
        }
    }

    @Override
    public Meci[] findAllMeci() throws ServicesException {
        initializeConnection(); //TODO: unfortunately, I HAD TO PUT THE CONNECTION INITIALIZATION IN HERE, AS THIS IS THE FIRST TIME WE USE THE SOCKETS (moybe fix this later...)
        Request req = new Request.Builder().type(RequestType.GET_MATCHES).build();
        sendRequest(req);
        Log.logger.trace("PROXY CLIENT: findAllMeci SENT REQUEST @"+ LocalDate.now());
        Response response = readResponse();
        Log.logger.trace("PROXY CLIENT: findAllMeci RECEIVED RESPONSE @"+ LocalDate.now());
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            Log.logger.traceExit("PROXY CLIENT: FAILED findAllMeci @"+ LocalDate.now());
            throw new ServicesException(err);
        }
        MeciDTO[] meciDTOS = (MeciDTO[]) response.data(); //TODO: MECIDTOS == NULL !!! (cred ca am uitat sa le pun pe undeva... in response.data...)
        Meci[] meciuri = DTOUtils.getFromDTO(meciDTOS);
        Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL findAllMeci @"+ LocalDate.now());
        return meciuri;
    }


    @Override
    public Bilet[] findAllBilet() throws ServicesException {
        Request req = new Request.Builder().type(RequestType.GET_TICKETS).build();
        sendRequest(req);
        Log.logger.trace("PROXY CLIENT: findAllBilet SENT REQUEST @"+ LocalDate.now());
        Response response = readResponse();
        Log.logger.trace("PROXY CLIENT: findAllBilet RECEIVED RESPONSE @"+ LocalDate.now());
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            Log.logger.traceExit("PROXY CLIENT: FAILED findAllBilet @"+ LocalDate.now());
            throw new ServicesException(err);
        }
        BiletDTO[] ticktsDTOs = (BiletDTO[]) response.data();
        Bilet[] tickets = DTOUtils.getFromDTO(ticktsDTOs);
        Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL findAllBilet @"+ LocalDate.now());
        return tickets;
    }


    @Override
    public void ticketsSold(Meci meci) throws ServicesException{
        MeciDTO meciDTO = DTOUtils.getDTO(meci);
        Request req = new Request.Builder().type(RequestType.TICKETS_SOLD).data(meciDTO).build();
        sendRequest(req);
        Log.logger.trace("PROXY CLIENT: ticketsSold SENT REQUEST @"+ LocalDate.now() + " WARNING: THIS FUNCTION HAS NO RESPONSE!!!");
        /*Response response=readResponse();
        if (response.type()== ResponseType.ERROR) {
            String err = response.data().toString(); // TODO: not ?????????????????????? TODO: I HAT TO REMOVE THIS PART IN ORDER TO ALLOW IT TO BE READ IN UPDATE() FROM RUN()
            throw new ServicesException(err);
        }*/
        Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL ticketsSold @"+ LocalDate.now()+ " WARNING: THIS FUNCTION HAS NO RESPONSE!!!");
    }


    @Override
    public void updateBilet(Bilet bilet) throws ServicesException {
        BiletDTO biletDTO = DTOUtils.getDTO(bilet);
        Request req = new Request.Builder().type(RequestType.UPDATE_BILET).data(biletDTO).build();
        sendRequest(req);
        Log.logger.trace("PROXY CLIENT: updateBilet SENT REQUEST @"+ LocalDate.now());
        Response response=readResponse();
        Log.logger.trace("PROXY CLIENT: updateBilet RECEIVED RESPONSE @"+ LocalDate.now());
        if (response.type()== ResponseType.ERROR) {
            String err = response.data().toString();
            Log.logger.traceExit("PROXY CLIENT: FAILED updateBilet @"+ LocalDate.now());
            throw new ServicesException(err);
        }
        // we are not reading anything here
        Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL updateBilet @"+ LocalDate.now());
    }


    @Override
    public Echipa findOneEchipa(String id) throws ServicesException {
        Request req=new Request.Builder().type(RequestType.GET_ECHIPA).data(id).build();
        sendRequest(req);
        Log.logger.trace("PROXY CLIENT: findOneEchipa SENT REQUEST @"+ LocalDate.now());
        Response response=readResponse();

        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            Log.logger.traceExit("PROXY CLIENT: FAILED findOneEchipa @"+ LocalDate.now());
            throw new ServicesException(err);
        }
        EchipaDTO echipaDTO = (EchipaDTO)response.data(); // daca este posibil sa dea exceptie peste network (se pierd date), atunci se topeste laptopul aici cand fac conversii pe DTO...
        Echipa echipa = DTOUtils.getFromDTO(echipaDTO);
        Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL findOneEchipa @"+ LocalDate.now());
        return echipa;
    }


    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
            Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL closeConnection @"+ LocalDate.now());
        } catch (IOException e) {
            Log.logger.traceExit("PROXY CLIENT: FAILED closeConnection @"+ LocalDate.now());
            e.printStackTrace();
        }

    }


    private void sendRequest(Request request) throws ServicesException {
        Log.logger.traceEntry("NETWORKING FROM CLIENT PROXY TO SERVER: INITIALIZING sendRequest @" + LocalDate.now(), request);
        try {
            output.writeObject(request);
            output.flush();
            Log.logger.traceExit("NETWORKING FROM CLIENT PROXY TO SERVER: SUCESSFUL sendRequest @" + LocalDate.now(), request);
        } catch (IOException e) {
            Log.logger.traceExit("NETWORKING FROM CLIENT PROXY TO SERVER: FAILED sendRequest @"+ LocalDate.now());
            throw new ServicesException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ServicesException {
        Response response=null;
        Log.logger.traceEntry("NETWORKING FROM CLIENT PROXY TO SERVER: INITIALIZING readResponse @" + LocalDate.now(), response);
        try{
            response=qresponses.take(); //TODO: qresponses size == 0... asta cand fac buy tickets request... (posibil sa trebuiasca sa fac cu executioner pool...)
            Log.logger.traceExit("NETWORKING FROM CLIENT PROXY TO SERVER: SUCESSFUL readResponse @" + LocalDate.now(), response);
        } catch (InterruptedException e) {
            Log.logger.traceExit("NETWORKING FROM CLIENT PROXY TO SERVER: FAILED readResponse @"+ LocalDate.now());
            e.printStackTrace();
        }
        return response;
    }


    private void initializeConnection() throws ServicesException {
        if (! this.isInitialized) {
            try {
                Log.logger.traceEntry("PROXY CLIENT: INITIALIZING initializeConnection @" + LocalDate.now());
                connection = new Socket(host, port); //TODO: THIS WILL THROW EXCEPTION WHEN HOST IS UNREACHABLE (hot fix)
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                input = new ObjectInputStream(connection.getInputStream());
                finished = false;
                this.isInitialized = true;
                startReader();
                Log.logger.traceExit("PROXY CLIENT: SUCCESSFUL initializeConnection @"+ LocalDate.now());
            } catch (IOException e) {
                Log.logger.traceExit("PROXY CLIENT: FAILED initializeConnection @"+ LocalDate.now());
                e.printStackTrace();
            }
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response){

        if (response.type()== ResponseType.TICKETS_SOLD){ //TODO: this is the handler for the TICKETS_SOLD request

            Meci meci = DTOUtils.getFromDTO((MeciDTO) response.data());
            System.out.println("pe biletul care a fost cumparat a fost scris numele clientului. pentru meciul "+meci);
            try {
                //TODO: THIS IS WHERE THE REAL OBSERVER SHOULD LIE, I GUESS...
                client.ticketsSold(meci);
            } catch (ServicesException e) {
                e.printStackTrace();
            }
        }
    }

    
    private boolean isUpdate(Response response){
        return response.type() == ResponseType.TICKETS_SOLD;
    }


    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    Log.logger.traceEntry("PROXY CLIENT: READING DATA FROM THE SERVER @"+ LocalDate.now(), response);
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        Log.logger.traceEntry("PROXY CLIENT: TRACE isUpdate((Response)response) == true @"+ LocalDate.now(), response);
                        handleUpdate((Response)response);
                    }else {
                        try {
                            Log.logger.traceEntry("PROXY CLIENT: TRACE qresponses.put((Response)response) @" + LocalDate.now(), response);
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    Log.logger.traceExit("PROXY CLIENT: FAILED run : IOException @"+ LocalDate.now(), e);
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    Log.logger.traceExit("PROXY CLIENT: FAILED run : ClassNotFoundException @"+ LocalDate.now(), e);
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
