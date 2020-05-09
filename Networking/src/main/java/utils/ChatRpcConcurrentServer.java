package utils;

import protocols.ChatClientRpcWorker;
import services.IServices;

import java.net.Socket;


public class ChatRpcConcurrentServer extends AbsConcurrentServer {
    private IServices chatServer;
    public ChatRpcConcurrentServer(int port, IServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ChatClientRpcWorker worker = new ChatClientRpcWorker(chatServer, client);

        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
