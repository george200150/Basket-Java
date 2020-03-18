package mvc.controllers.login;


import domain.Client;
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
import mvc.CustomAlert;
import mvc.controllers.AccountController;
import services.MasterService;

import java.io.IOException;

public class LoginFormController {
    @FXML
    TextField textFieldUserName;
    @FXML
    PasswordField passwordFieldUserPassword;
    @FXML
    Button buttonLogIn;

    private Stage dialogStage;
    private MasterService masterService;

    @FXML
    private void initialize() {
    }

    public void setService(MasterService masterService, Stage stage){
        this.masterService = masterService;
        this.dialogStage = stage;
    }

    public void handleLogIn(ActionEvent actionEvent) {

        String userName = this.textFieldUserName.getText();
        String password = this.passwordFieldUserPassword.getText();
        Client loggedInClient = this.masterService.findClientByCredentials(userName, password);

        if(loggedInClient != null){// check if account acces is granted

            try {
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

                AccountController studentAccountController = loader.getController();
                studentAccountController.setService(masterService, dialogStage, loggedInClient);

                this.dialogStage.close();
                dialogStage.show();
                dialogStage.setMaximized(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            CustomAlert.showMessage(null, Alert.AlertType.ERROR,"Log In","Nu ati introdus corect numele de utilizator sau parola!");
        }// TODO: MUST DECIDE IF DATABASE IS REACHABLE !!!
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
