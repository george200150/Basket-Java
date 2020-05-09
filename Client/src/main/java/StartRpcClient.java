import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.LoginFormController;
import network.ChatServicesRpcProxy;
import services.IServices;

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

        IServices server = new ChatServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LoginForm.fxml"));
        Parent root=loader.load();

        LoginFormController ctrl = loader.getController();
        ctrl.setService(server, primaryStage);

        FXMLLoader cloader = new FXMLLoader();
        cloader.setLocation(getClass().getResource("/views/AccountView.fxml"));

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}