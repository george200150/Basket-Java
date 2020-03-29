package mvc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import model.domain.Client;
import services.IServices;
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
    private AccountController accountController;
    //private MasterService masterService;
    private IServices server;
    private Client user;

    Parent mainChatParent;

    public void setServer(IServices s) {
        server = s;
    }


    public void setParent(Parent p) {
        mainChatParent = p;
    }

    /*public void setAccountController(AccountController accountController) {
        this.accountController = accountController;
    }*/

    @FXML
    private void initialize() {
    }

    public void setService(IServices server, Stage stage) {
        this.server = server;
        this.dialogStage = stage;
    }

    public void handleLogIn(ActionEvent actionEvent) {

        String userName = this.textFieldUserName.getText();
        String password = this.passwordFieldUserPassword.getText();
        Client user = new Client(userName, password);
        try { //TODO: NO MESSAGES ARE SHOWN WHEN THE LOGIN FAILS... IT JUST RUNS... AND THERE ARE NPE-s ON THE SERVER NOW
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

            accountController = loader.getController();
            accountController.setService(server, dialogStage);
            accountController.setLoggedInClient(user); //TODO: separate tasks


            this.server.login(user, accountController); //LOGIN HERE, AFTER WE HAVE INITIALISED THE CONTROLLER.

            this.dialogStage.close();
            dialogStage.show();
            dialogStage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServicesException e) {
            e.printStackTrace();
            CustomAlert.showMessage(null, Alert.AlertType.ERROR, "Log In", "Nu ati introdus corect numele de utilizator sau parola!");
        }
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
