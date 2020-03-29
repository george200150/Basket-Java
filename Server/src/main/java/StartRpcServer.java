import model.validators.BiletValidator;
import model.validators.ClientValidator;
import model.validators.EchipaValidator;
import model.validators.MeciValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }

        ApplicationContext context=new ClassPathXmlApplicationContext("Basket-Java.xml");
        JDBCInvariant jdbcInv = context.getBean(JDBCInvariant.class);


        ClientDataBaseRepository userRepo = new ClientDataBaseRepository(ClientValidator.getInstance());
        MeciDataBaseRepository meciRepo = new MeciDataBaseRepository(MeciValidator.getInstance());
        BiletDataBaseRepository biletRepo = new BiletDataBaseRepository(BiletValidator.getInstance());
        EchipaDataBaseRepository echipaRepo = new EchipaDataBaseRepository(EchipaValidator.getInstance());
        IServices chatServerImpl = new ChatServicesImpl(userRepo, meciRepo, biletRepo, echipaRepo);
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
