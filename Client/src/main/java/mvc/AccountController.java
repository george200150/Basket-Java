package mvc;

import basket.model.domain.Client;
import basket.model.domain.Meci;
import basket.model.domain.TipMeci;
import basket.model.notification.Notification;
import basket.model.notification.NotificationType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import protocols.ams.ChatServerAMSRpcProxy;
import services.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class AccountController implements NotificationSubscriber {
    @FXML public TextField textFieldNume;
    @FXML public Spinner spinnerBilete;

    @FXML private Label labelStudent;

    @FXML TableColumn<Meci, String> meciViewColumnHome;
    @FXML TableColumn<Meci, String> meciViewColumnAway;
    @FXML TableColumn<Meci, String> meciViewColumnData;
    @FXML TableColumn<Meci, String> meciViewColumnTip;
    @FXML TableColumn<Meci, Integer> meciViewColumnBilete;
    @FXML TableView<Meci> tableViewMeciuri;

    private Client loggedInClient;
    private IChatServicesAMS server;
    private ObservableList<Meci> model = FXCollections.observableArrayList();
    private Stage dialogStage;

    private NotificationReceiver receiver;


    public AccountController() {
    }
    public AccountController(IChatServicesAMS server) {
        this.server = server;
    }
    public void setReceiver(NotificationReceiver receiver) {
        this.receiver = receiver;
    }

    public void setService(IChatServicesAMS server, Stage stage) {
        this.dialogStage = stage;
        this.server = server;
        initModel();
    }

    public void setLoggedInClient(Client loggedInClient){
        this.loggedInClient = loggedInClient;
        this.labelStudent.setText(loggedInClient.getId());
        this.textFieldNume.setText(loggedInClient.getId());
    }

    private void initModel() {
        Runnable runnable = () -> {
            try {
                Iterable<Meci> meciuri = Arrays.asList(server.findAllMeci()); //called from proxy

                List<Meci> meciList = StreamSupport
                        .stream(meciuri.spliterator(), false)
                        .collect(Collectors.toList());

                model.setAll(meciList);
            } catch (ServicesException e) {
                e.printStackTrace();
                CustomAlert.showErrorMessage(dialogStage, "FAILED TO INITIALIZE THE DATA");
            }
        };
        Platform.runLater(runnable);
    }


    public void handleBackToLogInChoice(ActionEvent actionEvent) {
        Runnable runnable = () -> {
            try {
                // create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/LoginForm.fxml"));

                AnchorPane root = (AnchorPane) loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Log In");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                LoginFormController loginFormController = loader.getController();
                loginFormController.setService(server, dialogStage);

                this.server.logout(loggedInClient);
                this.dialogStage.close();
                dialogStage.show();

            } catch (IOException | ServicesException e) {
                e.printStackTrace();
            }
        };
        Platform.runLater(runnable);
    }



    @FXML
    public void initialize() {
        meciViewColumnBilete.setCellFactory(param -> new TableCell<Meci, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) { // long live stack overflow
                if (!empty) {
                    int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
                    int numarBilete = param
                            .getTableView().getItems()
                            .get(currentIndex).getNumarBileteDisponibile();
                    if (numarBilete == 0) {
                        setStyle("-fx-font-weight: bold");
                        setStyle("-fx-text-fill: red");
                        setText("SOLD OUT");
                    }
                    else{
                        setText(Integer.toString(numarBilete));
                        setStyle("-fx-font-weight: normal");
                        setStyle("-fx-text-fill: black");
                    }
                }
            }
        });

        meciViewColumnHome.setCellValueFactory(new PropertyValueFactory<Meci, String>("home"));
        meciViewColumnAway.setCellValueFactory(new PropertyValueFactory<Meci, String>("away"));
        meciViewColumnData.setCellValueFactory(new PropertyValueFactory<Meci, String>("date"));
        meciViewColumnTip.setCellValueFactory(new PropertyValueFactory<Meci, String>("tip"));
        meciViewColumnBilete.setCellValueFactory(new PropertyValueFactory<Meci, Integer>("numarBileteDisponibile"));

        tableViewMeciuri.setItems(model);
    }

    public void handleBuyTickets(ActionEvent actionEvent) {
        Runnable runnable = () -> {
            Meci m = this.tableViewMeciuri.getSelectionModel().getSelectedItem();
            if (m == null) {
                CustomAlert.showErrorMessage(dialogStage, "NU ATI SELECTAT UN MECI!");
            } else {
                String name = this.textFieldNume.getText();
                String id = m.getId();
                String home = m.getHome();
                String away = m.getAway();
                Date date = m.getDate();
                TipMeci tip = m.getTip();
                int numarPrecedent = m.getNumarBileteDisponibile();
                int numarVandute = (int) this.spinnerBilete.getValue();
                int numarActualizat = numarPrecedent - numarVandute;
                if (numarActualizat >= 0) { //remaining tickets number cannot be negative.
                    Meci meci = new Meci(id, home, away, date, tip, numarActualizat);
                    try {
                        loggedInClient.setNume(name);
                        server.ticketsSold(meci, loggedInClient);
                    } catch (ServicesException e) {
                        e.printStackTrace();
                    }
                    CustomAlert.showMessage(dialogStage, Alert.AlertType.CONFIRMATION, "Tranzactie incheiata cu succes!", "Ati cumparat " + numarVandute + " bilete la meciul " + m.getId() + "!");
                } else {
                    //eat it
                }
            }
        };
        Platform.runLater(runnable);
    }

    public void handleLoadModelMeci(MouseEvent mouseEvent) {
        Meci item = this.tableViewMeciuri.getSelectionModel().getSelectedItem();
        if (item == null)
            return;
        int min = 0;
        int max = item.getNumarBileteDisponibile();
        int initialValue = 1;
        this.spinnerBilete.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));
        this.spinnerBilete.getValueFactory().setValue(initialValue);
    }

    public void handleFilterDescTickets(ActionEvent actionEvent) {
        Runnable runnable = () -> {
            List<Meci> meciuri = null;
            try {
                meciuri = Arrays.asList(server.findAllMeciWithTickets());
            } catch (ServicesException e) {
                e.printStackTrace();
            }

            model.setAll(meciuri);
        };
        Platform.runLater(runnable);
    }

    public void refreshMeciuri(ActionEvent actionEvent) {
        Runnable runnable = () -> {
            initModel();
        };
        Platform.runLater(runnable);
    }


    @Override
    public void notificationReceived(Notification notif) {
        try {
            System.out.println("Ctrl notificationReceived ... " + notif.getType());
            Runnable runnable = () -> {
                if (notif.getType() == NotificationType.NEW_MECI_UPDATE) {
                    System.out.println("ACCOUNT_CONTROLLER: REQUEST TO REFRESH TABLE");
                    for (Meci m : model) {
                        if (m.getId().equals(notif.getMeci().getId())) {
                            model.remove(m);
                            model.add(notif.getMeci());
                            break;
                        }
                    }
                    tableViewMeciuri.setItems(model);
                    System.out.println("ACCOUNT_CONTROLLER: FINISHED TO REFRESH TABLE");
                }
            };
            Platform.runLater(runnable);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

