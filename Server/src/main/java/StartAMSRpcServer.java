import org.springframework.context.support.ClassPathXmlApplicationContext;
import utils.AbstractServer;
import utils.ChatRpcAMSConcurrentServer;
import utils.ServerException;


public class StartAMSRpcServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-server.xml");
        AbstractServer server=context.getBean("chatTCPServer", ChatRpcAMSConcurrentServer.class);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}


// C:\Users\George\Downloads\apache-activemq-5.15.12-bin\apache-activemq-5.15.12\bin>activemq start