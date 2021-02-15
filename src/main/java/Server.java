import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Server {

    static ServerSocket ss;
    static Socket socket;

    static HashMap<Integer, Socket> clients = new HashMap<>();
    static HashMap<Integer, ClientHandler> clientHandlers = new HashMap<>();

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(80);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = ss.accept();
                clients.put(socket.getPort(), socket);
                System.out.println("Connected: " + socket.getInetAddress().getHostAddress() + " Username: " + socket.getPort());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            ClientHandler clientHandler = new ClientHandler(socket);
            clientHandlers.put(socket.getPort(), clientHandler);
            new Thread(clientHandler).start();
        }

    }

    static HashMap<Integer, Socket> getClients() {
        return clients;
    }
}





