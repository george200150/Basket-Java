import domain.Bilet;
import domain.Client;
import domain.Echipa;
import domain.Meci;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mvc.controllers.login.LoginFormController;
import repositories.*;
import services.*;
import validators.BiletValidator;
import validators.ClientValidator;
import validators.EchipaValidator;
import validators.MeciValidator;

import java.io.IOException;

public class MainApp extends Application {

    private CrudRepository<String, Bilet> biletCrudRepository;
    private BiletService biletService;
    private CrudRepository<String, Client> clientCrudRepository;
    private ClientService clientService;
    private CrudRepository<String, Echipa> echipaCrudRepository;
    private EchipaService echipaService;
    private CrudRepository<String, Meci> meciCrudRepository;
    private MeciService meciService;

    private MasterService masterService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        biletCrudRepository = new BiletDataBaseRepository(BiletValidator.getInstance());
        biletService = new BiletService(biletCrudRepository);

        clientCrudRepository = new ClientDataBaseRepository(ClientValidator.getInstance());
        clientService = new ClientService(clientCrudRepository);

        echipaCrudRepository = new EchipaDataBaseRepository(EchipaValidator.getInstance());
        echipaService = new EchipaService(echipaCrudRepository);

        meciCrudRepository = new MeciDataBaseRepository(MeciValidator.getInstance());
        meciService = new MeciService(meciCrudRepository);

        masterService = new MasterService(biletService, clientService, echipaService, meciService);

        init1(primaryStage);
        primaryStage.show();
    }

    private void init1(Stage primaryStage) throws IOException {

        FXMLLoader gradeLoader = new FXMLLoader();
        gradeLoader.setLocation(getClass().getResource("/views/login/LoginForm.fxml"));
        AnchorPane gradeLayout = gradeLoader.load();
        primaryStage.setScene(new Scene(gradeLayout));

        LoginFormController loginFormController = gradeLoader.getController();
        loginFormController.setService(masterService, primaryStage);

    }
}
