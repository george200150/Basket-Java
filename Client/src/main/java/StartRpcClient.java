import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.AccountController;
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

        /*IServices server = new ChatServicesRpcProxy(serverIP, serverPort);

        ClientController ctrl = new ClientController(server);


        LoginWindow logWin = new LoginWindow("Basket App", ctrl);
        logWin.setSize(200, 200);
        logWin.setLocation(150, 150);
        logWin.setVisible(true);*/
        IServices server = new ChatServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LoginForm.fxml"));
        Parent root=loader.load();


        LoginFormController ctrl = loader.getController();
        ctrl.setService(server, primaryStage);


        FXMLLoader cloader = new FXMLLoader();
        cloader.setLocation(getClass().getResource("/views/AccountView.fxml"));
        Parent croot=cloader.load();

        AccountController chatCtrl = cloader.getController();


        //TODO: fac sell_ticket
        //TODO: fac mai multe request-uri / fac transmiterea prin observer la toti clientii logati
        //TODO: ajung (cumva... ca abia acum se vede raul) in AccountController si fac updateTabele, ca sa se vada decrementarea.
        //TODO: cand fac toDto(un meci), pentru request-ul de findOneTeam(id) imi da eroare si mi se deconecteaza socket-ul pentru orice alta comanda din GUI ce urmeaza.

        //TODO: presupun ca uit un listen pe port undeva si cineva preia un request mult prea devreme.. si cand as avea eu nevoie de request, nu mi-l gaseste...
        // ori nici macar nu il transmit response-ul/request-ul (poate uit pe undeva...)
        //ori chiar nu imi dau seama...
        //TODO: jurnalizare pentru fiecare chestie...

        /*REMOVED THIS: ctrl.setAccountController(chatCtrl);*/
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}