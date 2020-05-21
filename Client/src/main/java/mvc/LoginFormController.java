package mvc;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import basket.model.domain.Client;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.IChatServicesAMS;
import services.NotificationReceiver;
import services.ServicesException;

import java.io.IOException;


public class LoginFormController {
    @FXML
    TextField textFieldUserName;
    @FXML
    PasswordField passwordFieldUserPassword;
    @FXML
    Button buttonLogIn;

    private Stage dialogStage;
    private IChatServicesAMS server;


    @FXML
    private void initialize() {
    }

    public void setService(IChatServicesAMS server, Stage stage) {
        this.server = server;
        this.dialogStage = stage;
    }

    public void handleLogIn(ActionEvent actionEvent) {
        Runnable runnable = () -> {
            String userName = this.textFieldUserName.getText();
            String password = this.passwordFieldUserPassword.getText();
            Client user = new Client(userName, password);
            try { // NO MESSAGES ARE SHOWN WHEN THE LOGIN FAILS... IT JUST RUNS... AND THERE ARE NPE-s ON THE SERVER NOW
                // create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/AccountView.fxml"));

                AnchorPane root = (AnchorPane) loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Log In Client");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                AccountController accountController = loader.getController();
                accountController.setService(server, dialogStage);

                accountController.setLoggedInClient(user);

                ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
                NotificationReceiver receiver = context.getBean("notificationReceiver", NotificationReceiver.class);

                accountController.setReceiver(receiver);
                receiver.start(accountController);

                this.server.login(user); //LOGIN HERE, AFTER WE HAVE INITIALISED THE CONTROLLER.

                this.dialogStage.close();
                dialogStage.show();

            } catch (ServicesException | IOException e) {
                e.printStackTrace();
                CustomAlert.showMessage(null, Alert.AlertType.ERROR, "Log In", "Nu ati introdus corect numele de utilizator sau parola!");
            }
        };
        Platform.runLater(runnable);
    }

    public void handleUsrKeyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            this.passwordFieldUserPassword.requestFocus();
        }
    }

    public void handlePsswdKeyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            this.buttonLogIn.fire();
        }
    }
}
