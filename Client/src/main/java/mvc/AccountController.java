package mvc;

import model.domain.MeciDTOShow;
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
    TableColumn<MeciDTOShow, String> meciViewColumnHome;
    @FXML
    TableColumn<MeciDTOShow, String> meciViewColumnAway;
    @FXML
    TableColumn<MeciDTOShow, String> meciViewColumnData;
    @FXML
    TableColumn<MeciDTOShow, String> meciViewColumnTip;
    @FXML
    TableColumn<MeciDTOShow, String> meciViewColumnBilete; //created DTO in order to show "SOLD OUT" message
    @FXML
    TableView<MeciDTOShow> tableViewMeciuri;


    private Client loggedInClient;//TODO: LOGIN CREDENTIALS
    private IServices server;
    private ObservableList<MeciDTOShow> model = FXCollections.observableArrayList();
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

        Iterable<Meci> meciuri = null;
        try {
            meciuri = Arrays.asList(server.findAllMeci()); //called from proxy
        } catch (ServicesException e) {
            e.printStackTrace();
        }
        List<Meci> meciList = StreamSupport
                .stream(meciuri.spliterator(), false)
                .collect(Collectors.toList());

        model.setAll(toDTO(meciList));
    }

    private MeciDTOShow toDTO(Meci m){
        String id = m.getId();
        int bilete = m.getNumarBileteDisponibile();
        TipMeci tip = m.getTip();
        Date date = m.getDate();
        Echipa home = null;
        try {
            home = server.findOneEchipa(m.getHome()); // call in proxy
        } catch (ServicesException e) {
            e.printStackTrace();
        }
        Echipa away = null;
        try {
            away = server.findOneEchipa(m.getAway()); // call in proxy
        } catch (ServicesException e) {
            e.printStackTrace();
        }
        return new MeciDTOShow(id, home, away, date, tip, bilete); //TODO: CAREFUL!!! MAY FAIL BECAUSE OF LOST INFORMATION OVER NETWORK (idk, still TCP, but I'm sceptical)
    }

    private List<MeciDTOShow> toDTO(List<Meci> meciList) {
        return meciList.stream()
                .map(m -> {
                    String id = m.getId();
                    int bilete = m.getNumarBileteDisponibile();
                    TipMeci tip = m.getTip();
                    Date date = m.getDate();
                    Echipa home = null;
                    try {
                        home = server.findOneEchipa(m.getHome()); // call in proxy
                    } catch (ServicesException e) {
                        e.printStackTrace();
                    }
                    Echipa away = null;
                    try {
                        away = server.findOneEchipa(m.getAway()); // call in proxy
                    } catch (ServicesException e) {
                        e.printStackTrace();
                    }
                    return new MeciDTOShow(id, home, away, date, tip, bilete); //TODO: CAREFUL!!! MAY FAIL BECAUSE OF LOST INFORMATION OVER NETWORK (idk, still TCP, but I'm sceptical)
                })
                .collect(Collectors.toList());
    }

    public void handleBackToLogInChoice(ActionEvent actionEvent) {
        // TODO: ??? NOTIFY OTHER CUSTOMERS THAT CURRENT USER HAS LOGGED OUT ???
        // TODO: use the clients repository for saving currently logged in clients ???
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
            /*TODO: REMOVED THIS: loginFormController.setAccountController(this);*/

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
        meciViewColumnBilete.setCellFactory(param -> new TableCell<MeciDTOShow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) { // long live stack overflow (when moving the column things happen...)
                if (!empty) {
                    int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
                    String numarBilete = param
                            .getTableView().getItems()
                            .get(currentIndex).getNumarBileteSauSoldOut();
                    if (numarBilete.equals("SOLD OUT")) {
                        setStyle("-fx-font-weight: bold");
                        setStyle("-fx-text-fill: red");
                        setText(numarBilete);
                    }
                    else{
                        setText(numarBilete);
                        setStyle("-fx-font-weight: normal");
                        setStyle("-fx-text-fill: black");
                    }
                }
            }
        });

        meciViewColumnHome.setCellValueFactory(new PropertyValueFactory<MeciDTOShow, String>("homeString"));
        meciViewColumnAway.setCellValueFactory(new PropertyValueFactory<MeciDTOShow, String>("awayString"));
        meciViewColumnData.setCellValueFactory(new PropertyValueFactory<MeciDTOShow, String>("date"));
        meciViewColumnTip.setCellValueFactory(new PropertyValueFactory<MeciDTOShow, String>("tip"));
        meciViewColumnBilete.setCellValueFactory(new PropertyValueFactory<MeciDTOShow, String>("numarBileteSauSoldOut"));

        tableViewMeciuri.setItems(model);

    }

    public void handleBuyTickets(ActionEvent actionEvent) {
        MeciDTOShow m = this.tableViewMeciuri.getSelectionModel().getSelectedItem();
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
            Iterable<Bilet> bilete = null;
            try {
                bilete = Arrays.asList(server.findAllBilet()); // call in proxy
            } catch (ServicesException e) {
                e.printStackTrace();
            }
            List<Bilet> bileteList = StreamSupport.stream(bilete.spliterator(), false)
                    .filter(x -> x.getIdMeci().equals(m.getId()))
                    .filter(x -> x.getIdClient() == null) // get all the unassigned tickets for the selected match
                    .collect(Collectors.toList());

            if (bileteList.size() >= numarVandute) { //must be (else logic error)
                for (int i = 0; i < numarVandute; i++) {
                    Bilet bilet = bileteList.get(i);
                    bilet.setIdClient(loggedInClient.getId());
                    bilet.setNumeClient(name);
                    try {
                        server.updateBilet(bilet); //TODO: was surrounded with TRY/CATCH (networking part)
                    } catch (ServicesException e) {
                        e.printStackTrace();
                    }
                }// assigned all the tickets to the client
                Meci meci = new Meci(id, home, away, date, tip, numarActualizat);
                try {
                    server.ticketsSold(meci); //TODO: TICKETS_SOLD !!!
                } catch (ServicesException e) {
                    e.printStackTrace();
                }
                CustomAlert.showMessage(dialogStage, Alert.AlertType.CONFIRMATION, "Tranzactie incheiata cu succes!", "Ati cumparat " + numarVandute + " bilete la meciul " + m.getId() + "!");
            }
            else {
                //eat it
            }
        }
    }

    public void handleLoadModelMeci(MouseEvent mouseEvent) {
        MeciDTOShow item = this.tableViewMeciuri.getSelectionModel().getSelectedItem();
        if (item == null)
            return;
        int min = 0;
        int max = item.getNumarBilete();
        int initialValue = 1;
        this.spinnerBilete.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));
        this.spinnerBilete.getValueFactory().setValue(initialValue);
    }

    public void handleFilterDescTickets(ActionEvent actionEvent) {
        Iterable<Meci> meciuri = null;
        try {
            meciuri = Arrays.asList(server.findAllMeci());
        } catch (ServicesException e) {
            e.printStackTrace();
        }
        List<Meci> meciList = StreamSupport
                .stream(meciuri.spliterator(), false)
                .filter(x-> x.getNumarBileteDisponibile() > 0)
                .sorted((m1,m2)-> m2.getNumarBileteDisponibile() - m1.getNumarBileteDisponibile())
                .collect(Collectors.toList());

        model.setAll(toDTO(meciList));
    }

    public void refreshMeciuri(ActionEvent actionEvent) {
        initModel();
    }

    @Override
    public void ticketsSold(Meci meci) throws ServicesException {
        System.out.println("ACCOUNT_CONTROLLER: REQUEST TO REFRESH TABLE");
        MeciDTOShow meciDTO = toDTO(meci);
        if (this.model.contains(meciDTO)){ // IMPLEMENTED COMPARATOR ON ID
            this.model.remove(meciDTO);
            this.model.add(meciDTO);
            System.out.println("SUCCESSFULLY REFRESHED TABLE");
        }
        else{
            System.out.println("FAILED TO FIND MATCH !!! BAD BAD BAD BAD BAD BAD BAD BAD BAD");
        }
    }
}

