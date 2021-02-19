package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Server {

    static ServerSocket ss;
    static Socket socket;

    static HashMap<Integer, Socket> clients = new HashMap<>();
    static HashMap<Integer, ClientHandler> clientHandlers = new HashMap<>();
    static HashMap<Integer, Integer> connections = new HashMap<>();

    public Server() {
        try {
            ss = new ServerSocket(80);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = ss.accept();
                clients.put(socket.getPort(), socket);
                connections.put(socket.getPort(), 0);
                System.out.println("Connected: " + socket.getInetAddress().getHostAddress() + " Username: " + socket.getPort());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            ClientHandler clientHandler = new ClientHandler(socket);

            new Thread(clientHandler).start();
        }
    }

    public static void main(String[] args) { }

    static HashMap<Integer, Socket> getClients() {
        return clients;
    }
    static HashMap<Integer, Integer> getConnections() { return connections; }
}





