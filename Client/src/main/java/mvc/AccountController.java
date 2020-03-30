package mvc;

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
import model.domain.*;
import services.IObserver;
import services.IServices;
import services.ServicesException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AccountController implements IObserver {

    @FXML
    public TextField textFieldNume;
    @FXML
    public Spinner spinnerBilete;

    @FXML
    private Label labelStudent;

    @FXML
    TableColumn<Meci, String> meciViewColumnHome;
    @FXML
    TableColumn<Meci, String> meciViewColumnAway;
    @FXML
    TableColumn<Meci, String> meciViewColumnData;
    @FXML
    TableColumn<Meci, String> meciViewColumnTip;
    @FXML
    TableColumn<Meci, Integer> meciViewColumnBilete;
    @FXML
    TableView<Meci> tableViewMeciuri;


    private Client loggedInClient;//TODO: LOGIN CREDENTIALS
    private IServices server;
    private ObservableList<Meci> model = FXCollections.observableArrayList();
    private Stage dialogStage;



    public void setService(IServices server, Stage stage) {
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
        try {
            Iterable<Meci> meciuri = Arrays.asList(server.findAllMeci()); //called from proxy

            List<Meci> meciList = StreamSupport
                    .stream(meciuri.spliterator(), false)
                    .collect(Collectors.toList());

            model.setAll(meciList);
        } catch (ServicesException e) {
            e.printStackTrace();
            CustomAlert.showErrorMessage(dialogStage,"FAILED TO INITIALIZE THE DATA");
        }

    }


    public void handleBackToLogInChoice(ActionEvent actionEvent) {
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

            this.server.logout(loggedInClient, this);
            this.dialogStage.close();
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServicesException e) {
            e.printStackTrace();
        }

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
        Meci m = this.tableViewMeciuri.getSelectionModel().getSelectedItem();
        if (m == null) {
            CustomAlert.showErrorMessage(dialogStage, "NU ATI SELECTAT UN MECI!");
        } else {
            String name = this.textFieldNume.getText(); //TODO: put parameter NAME in BILET
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
                    server.ticketsSold(meci, loggedInClient); //TODO: TICKETS_SOLD !!!
                } catch (ServicesException e) {
                    e.printStackTrace();
                }
                CustomAlert.showMessage(dialogStage, Alert.AlertType.CONFIRMATION, "Tranzactie incheiata cu succes!", "Ati cumparat " + numarVandute + " bilete la meciul " + m.getId() + "!");
            } else {
                //eat it
            }
        }
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
            List<Meci> meciuri = null;
            try {
                meciuri = Arrays.asList(server.findAllMeciWithTickets());
            } catch (ServicesException e) {
                e.printStackTrace();
            }

            model.setAll(meciuri);
    }

    public void refreshMeciuri(ActionEvent actionEvent) {
        initModel();
    }

    @Override
    public void notifyTicketsSold(Meci meci) throws ServicesException {
        System.out.println("ACCOUNT_CONTROLLER: REQUEST TO REFRESH TABLE");
        if (this.model.contains(meci)) {
            this.model.remove(meci);
            this.model.add(meci);
            System.out.println("SUCCESSFULLY REFRESHED TABLE");
        } else {
            System.out.println("FAILED TO FIND MATCH !!! BAD BAD BAD BAD BAD BAD BAD BAD BAD");
        }
        initModel();

        System.out.println("ACCOUNT_CONTROLLER: FINISHED TO REFRESH TABLE");
    }
}

