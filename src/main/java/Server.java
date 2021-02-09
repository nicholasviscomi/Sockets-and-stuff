import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    static ServerSocket ss;
    static Socket socket;
//    static ArrayList<Socket> sockets = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(80);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = ss.accept();
                System.out.println("Connected: " + socket.getInetAddress().getHostAddress());
//                sockets.add(socket);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            ClientHandler clientSocket = new ClientHandler(socket);
            new Thread(clientSocket).start();
        }

    }
}

/*
GOAL:
    Client connects to the server and gets the initial message (done)
    From there, the client can make requests for new data (similar to web browser I think)
    Use SQL so the people that connect to the server can get, set, post data
 */

//might not be multithreading correctly, try making the class implement runnable
class ClientHandler implements Runnable {
    Socket socket;
    ClientHandler(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        DataInputStream in;
        DataOutputStream out;

        try {
            in = new DataInputStream(socket.getInputStream()); //where you can get the message
            out = new DataOutputStream(socket.getOutputStream()); //where you can write the message to the socket

            String clientMessage, serverMessage;

            while (!(clientMessage = in.readUTF()).equalsIgnoreCase("quit")) {

                // writing the received message
                System.out.println("Client: " + clientMessage);
                //classify the message and respond appropriately
                if (clientMessage.contains("GET")) {
                    System.out.println("Get request received");
                    serverMessage = "you have gotten the new page";
                    out.writeUTF(serverMessage);
                    out.flush();
                } else if (clientMessage.contains("POST")) {
                    System.out.println("Post request received");
                    serverMessage = "thank you for sending your data";
                    out.writeUTF(serverMessage);
                    out.flush();
                } else if (clientMessage.contains("SET")) {
                    System.out.println("Set request received");
                    serverMessage = "thanks for setting the data";
                    out.writeUTF(serverMessage);
                    out.flush();
                } else {
                    System.out.println("Other input was sent");
                    serverMessage = "unknown input (echo = " + clientMessage + ")";
                    out.writeUTF(serverMessage);
                    out.flush();
                }

            }

            socket.close();

        } catch (Exception ignored) { }
    }
}
