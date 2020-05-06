package apacheThrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import repos.JDBCInvariant;

import java.io.IOException;
import java.util.Properties;

public class TransformerServer {

    public static TransformerHandler handler;
    public static TransformerService.Processor processor;

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(JDBCInvariant.class.getResourceAsStream("/bd.config"));
            properties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find bd.config " + e);
            return;
        }
        new JDBCInvariant(properties); // initialize static fields in object before using anything from app logic

        try {
            handler = new TransformerHandler();
            processor = new TransformerService.Processor(handler);

            Runnable simple = () -> simple(processor);

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
            handler.shutdown(); // Hopefully, connections will go right...
        }
    }

    public static void simple(TransformerService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9091);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server....");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
            handler.shutdown(); // Hopefully, connections will go right...
        }
    }
}
