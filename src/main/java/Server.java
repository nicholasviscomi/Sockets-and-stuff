import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {

    static ServerSocket ss;
    static Socket socket;
    static HashMap<Integer, Socket> clients = new HashMap<>();

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
            ClientHandler clientSocket = new ClientHandler(socket);
            new Thread(clientSocket).start();
        }

    }

    static HashMap<Integer, Socket> getClients() {
        return clients;
    }
}

//might not be multithreading correctly, try making the class implement runnable
class ClientHandler implements Runnable {
    Socket socket;
    ClientHandler(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        DataInputStream in = null;
        DataOutputStream out = null;

        int recipient = 0;

        try {
            in = new DataInputStream(socket.getInputStream()); //where you can get the message
            out = new DataOutputStream(socket.getOutputStream());//where you can write the message to the socket

            out.writeUTF(String.valueOf(socket.getPort()));
            out.flush();

            out.writeUTF("Enter username to chat with: ");
            String recipientStr = in.readUTF();
            recipient = Integer.parseInt(recipientStr);
            System.out.println("Attempting to connect to (" + recipient + ")");

            if (!userIsOnline(recipient)) {
                System.out.println("user not online :("); throw new Exception(); }
            else { System.out.println("user is online!"); }

        } catch (NumberFormatException nfe) { System.out.println("Invalid username");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("another ClientHandler exception ");
        }


        String clientMessage;
        while (true) {
            try {
                if (in == null) {
                    System.out.println("in == null");
                    break;
                }

                clientMessage = in.readUTF();
                System.out.println("Client: " + clientMessage); // writing the received message
                //classify the message and respond appropriately
//                if (clientMessage.contains("GET")) {
//                    System.out.println("Get request received");
//                    serverMessage = "you have gotten the new page";
//                    out.writeUTF(serverMessage);
//                    out.flush();
//                } else if (clientMessage.contains("POST")) {
//                    System.out.println("Post request received");
//                    serverMessage = "thank you for sending your data";
//                    out.writeUTF(serverMessage);
//                    out.flush();
//                } else if (clientMessage.contains("SET")) {
//                    System.out.println("Set request received");
//                    serverMessage = "thanks for setting the data";
//                    out.writeUTF(serverMessage);
//                    out.flush();
//                } else {
//                    System.out.println("Other input was sent");
//                    serverMessage = "unknown input (echo = " + clientMessage + ")";
//                    out.writeUTF(serverMessage);
//                    out.flush();
//                }

                DataOutputStream recipientOut = new DataOutputStream(Server.getClients().get(recipient).getOutputStream());
                recipientOut.writeUTF(clientMessage);
                recipientOut.flush();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error in ClientHandler loop");
                break;
            }
        }

        try {
            socket.close();
        } catch (Exception ignored) {}


    }

    boolean userIsOnline(int user) {
        //CLIENTS DOES NOT REMOVE A CLIENT WHEN THE GO OFFLINE
        System.out.println("clients: " + Server.getClients());
        return Server.getClients().get(user) != null;
    }
}
