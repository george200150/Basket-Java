package mvc.controllers;

import domain.*;
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
import mvc.CustomAlert;
import mvc.controllers.login.LoginFormController;
import services.MasterService;
import utils.events.MeciChangeEvent;
import utils.observers.MeciObserver;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AccountController implements MeciObserver {

    @FXML
    public TextField textFieldNume;
    @FXML
    public Spinner spinnerBilete;

    @FXML
    private Label labelStudent;

    @FXML
    TableColumn<MeciDTO, String> meciViewColumnHome;
    @FXML
    TableColumn<MeciDTO, String> meciViewColumnAway;
    @FXML
    TableColumn<MeciDTO, String> meciViewColumnData;
    @FXML
    TableColumn<MeciDTO, String> meciViewColumnTip;
    @FXML
    TableColumn<MeciDTO, String> meciViewColumnBilete; //TODO: create DTO in order to show "SOLD OUT" message
    @FXML
    TableView<MeciDTO> tableViewMeciuri;


    private Client loggedInClient;//LOGIN CREDENTIALS
    private MasterService service;
    private ObservableList<MeciDTO> model = FXCollections.observableArrayList();
    private Stage dialogStage;



    public void setService(MasterService masterService, Stage stage, Client loggedInClient) {
        this.dialogStage = stage;
        this.loggedInClient = loggedInClient;
        service = masterService;
        service.addObserverMeci(this);
        initModel();
        this.labelStudent.setText(loggedInClient.toString());
        this.textFieldNume.setText(loggedInClient.toString());
    }

    private void initModel() {

        Iterable<Meci> meciuri = service.findAllMeci();
        List<Meci> meciList = StreamSupport
                .stream(meciuri.spliterator(), false)
                .collect(Collectors.toList());

        // setStyle("-fx-text-fill: red")

        model.setAll(toDTO(meciList));
    }

    private List<MeciDTO> toDTO(List<Meci> meciList) {
        return meciList.stream()
                .map(m -> {
                    String id = m.getId();
                    int bilete = m.getNumarBileteDisponibile();
                    TipMeci tip = m.getTip();
                    Date date = m.getDate();
                    Echipa home = service.findOneEchipa(m.getHome());
                    Echipa away = service.findOneEchipa(m.getAway());
                    return new MeciDTO(id, home, away, date, tip, bilete);
                })
                .collect(Collectors.toList());
    }

    public void handleBackToLogInChoice(ActionEvent actionEvent) {

        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/login/LoginForm.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Log In");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            LoginFormController loginFormController = loader.getController();
            loginFormController.setService(service, dialogStage);

            this.dialogStage.close();
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize() {
        meciViewColumnHome.setCellValueFactory(new PropertyValueFactory<MeciDTO, String>("homeString"));
        meciViewColumnAway.setCellValueFactory(new PropertyValueFactory<MeciDTO, String>("awayString"));
        meciViewColumnData.setCellValueFactory(new PropertyValueFactory<MeciDTO, String>("date"));
        meciViewColumnTip.setCellValueFactory(new PropertyValueFactory<MeciDTO, String>("tip"));
        meciViewColumnBilete.setCellValueFactory(new PropertyValueFactory<MeciDTO, String>("numarBileteSauSoldOut"));

        tableViewMeciuri.setItems(model);

    }

    @Override
    public void updateMeci(MeciChangeEvent meciChangeEvent) {
        initModel();
    }

    public void handleBuyTickets(ActionEvent actionEvent) {
        MeciDTO m = this.tableViewMeciuri.getSelectionModel().getSelectedItem();
        if (m == null) {
            CustomAlert.showErrorMessage(dialogStage, "NU ATI SELECTAT UN MECI!");
        } else {
            String name = this.textFieldNume.getText(); //TODO: put parameter NAME in BILET
            String id = m.getId();
            String home = m.getHome().getId();
            String away = m.getAway().getId();
            Date date = m.getDate();
            TipMeci tip = m.getTip();
            int numarPrecedent = m.getNumarBilete();
            int numarVandute = (int) this.spinnerBilete.getValue();
            int numarActualizat = numarPrecedent - numarVandute;
            Iterable<Bilet> bilete = service.findAllBilet();
            List<Bilet> bileteList = StreamSupport.stream(bilete.spliterator(), false)
                    .filter(x -> x.getIdMeci().equals(m.getId()))
                    .filter(x -> x.getIdClient() == null) // get all the unassigned tickets for the selected match
                    .collect(Collectors.toList());

            if (bileteList.size() >= numarVandute) { //must be (else logic error)
                for (int i = 0; i < numarVandute; i++) {
                    Bilet bilet = bileteList.get(i);
                    bilet.setIdClient(loggedInClient.getId()); //TODO: also set name, maybe     (considered that when a match is created, all the tickets are instantiated without a customer (id == null)
                    service.updateBilet(bilet);
                }// assigned all the tickets to the client
                Meci meci = new Meci(id, home, away, date, tip, numarActualizat);
                service.updateMeci(meci);
                CustomAlert.showMessage(dialogStage, Alert.AlertType.CONFIRMATION, "Transaction Succeeded", "You have bought " + numarVandute + " tickets!");
            }
            else {
                //eat it
            }
        }
    }

    public void handleLoadModelMeci(MouseEvent mouseEvent) {
        int min = 0;
        int max = this.tableViewMeciuri.getSelectionModel().getSelectedItem().getNumarBilete();
        int initialValue = 1;
        this.spinnerBilete.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));
        this.spinnerBilete.getValueFactory().setValue(initialValue);
    }

    public void handleFilterDescTickets(ActionEvent actionEvent) {
        Iterable<Meci> meciuri = service.findAllMeci();
        List<Meci> meciList = StreamSupport
                .stream(meciuri.spliterator(), false)
                .filter(x-> x.getNumarBileteDisponibile() > 0)
                .sorted(Comparator.comparingInt(Meci::getNumarBileteDisponibile))
                .collect(Collectors.toList());

        model.setAll(toDTO(meciList));
    }
}

