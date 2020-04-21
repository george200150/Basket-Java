import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.LoginFormController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.IServices;


public class StartRpcClient extends Application {

    public void start(Stage primaryStage) throws Exception {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        IServices server = (IServices) factory.getBean("chatService");
        System.out.println("Obtained a reference to remote chat server");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LoginForm.fxml"));
        Parent root=loader.load();

        LoginFormController ctrl = loader.getController();
        ctrl.setService(server, primaryStage);

        FXMLLoader cloader = new FXMLLoader();
        cloader.setLocation(getClass().getResource("/views/AccountView.fxml"));

        primaryStage.setTitle("MPP tema");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}