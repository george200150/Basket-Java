import model.validators.BiletValidator;
import model.validators.ClientValidator;
import model.validators.MeciValidator;
import server.ChatServicesImpl;
import services.IServices;

import repos.*;
import utils.AbstractServer;
import utils.ChatRpcConcurrentServer;
import utils.ServerException;

import java.io.IOException;
import java.util.Properties;


public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }

        Properties properties = new Properties();
        try {
            properties.load(JDBCInvariant.class.getResourceAsStream("/bd.config"));
            properties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find bd.config " + e);
            return;
        }
        JDBCInvariant jdbcInv = new JDBCInvariant(properties);


        ClientDataBaseRepository userRepo = new ClientDataBaseRepository(ClientValidator.getInstance());
        MeciDataBaseRepository meciRepo = new MeciDataBaseRepository(MeciValidator.getInstance());
        BiletDataBaseRepository biletRepo = new BiletDataBaseRepository(BiletValidator.getInstance());
        IServices chatServerImpl = new ChatServicesImpl(userRepo, meciRepo, biletRepo);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ChatRpcConcurrentServer(serverPort, chatServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
