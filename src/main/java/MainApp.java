import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mvc.controllers.login.LoginFormController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.*;


import java.io.IOException;

public class MainApp extends Application {

    private MasterService masterService;

    public static void main(String[] args) {
        launch(args);
    }

    static MasterService getMasterService(){
        ApplicationContext context=new ClassPathXmlApplicationContext("Basket-Java.xml");
        MasterService service=context.getBean(MasterService.class);
        return service;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        masterService = getMasterService();

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
