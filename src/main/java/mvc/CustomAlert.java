package mvc;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CustomAlert {
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }

    public static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}

        /*
        1. Login. Dupa autentificarea cu succes, o noua fereastra se deschide in care sunt afișate toate meciurile
        (Echipa A vs Echipa B, Echipa B vs Echipa C, etc., Semifinala 1, Semifinala 2, Finala), pretul unui bilet si
        numarul locurilor disponibile. (V)

        2. Vanzare. Dupa autentificarea cu succes persoana de la casa poate vinde bilete la un anumit meci. Pentru
        vanzarea unui bilet se introduce numele clientului si numarul de locuri dorite. Dupa efectuarea unei vanzari,
        persoanele de la celelalte case vad lista actualizata a meciurilor si numarul locurilor disponibile. Daca la un
        meci nu mai sunt locuri disponbile, mesajul "SOLD OUT" este afișat langa meci cu culoare roșie.

        3. Cautare. Persoana poate vizualiza meciurile pentru care mai sunt locuri disponibile, în ordine descrescatoare
        dupa numarul locurilor disponibile.

        4. Logout.

        */