package Server;

import GUI.ServerFrame;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Server {

    private ServerSocket ss;
    Socket socket;
    public ServerFrame serverFrame;

    private final HashMap<Integer, Socket> clients = new HashMap<>();
    private final HashMap<Integer, ClientHandler> clientHandlers = new HashMap<>();
    private final HashMap<Integer, Integer> connections = new HashMap<>();

    public Server() {
        try {
            ss = new ServerSocket(80);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Server self = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        socket = ss.accept();
                        clients.put(socket.getPort(), socket);
                        connections.put(socket.getPort(), 0);

                        serverFrame.updateTable(String.valueOf(socket.getPort()), String.valueOf(socket.getInetAddress().getHostAddress()), "nil");
                        System.out.println("Connected: " + socket.getInetAddress().getHostAddress() + " Username: " + socket.getPort());
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    ClientHandler clientHandler = new ClientHandler(socket, self);

                    new Thread(clientHandler).start();
                }
            }
        });

    }

    public static void main(String[] args) {
        new Server();
    }

    public ServerSocket getServerSocket() { return ss; }
    public HashMap<Integer, Socket> getClients() {
        return clients;
    }
    public HashMap<Integer, Integer> getConnections() { return connections; }
    public HashMap<Integer, ClientHandler> getClientHandlers() { return clientHandlers; }
}





