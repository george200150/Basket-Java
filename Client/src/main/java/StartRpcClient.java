import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.LoginFormController;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import protocols.ams.ChatServerAMSRpcProxy;
import services.IChatServicesAMS;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClient extends Application {

    private static int defaultServerPort = 55555;
    private static String defaultServer = "localhost"; // in case there is not props file


    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("client.server.host", defaultServer);
        int serverPort = defaultServerPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("client.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultServerPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        // proxy object retreived on startup from Spring
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
        IChatServicesAMS server = context.getBean("chatServices", ChatServerAMSRpcProxy.class);


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LoginForm.fxml"));
        Parent root=loader.load();

        LoginFormController ctrl = loader.getController();
        ctrl.setService(server, primaryStage);

        // !!! DO NOT, DELETE THIS SET LOCATION INSTRUCTION (else you will not be able to instantiate the ctrl in login)
        FXMLLoader cloader = new FXMLLoader();
        cloader.setLocation(getClass().getResource("/views/AccountView.fxml"));

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}