import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    static ServerSocket ss;
    static Socket socket;

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(80);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean stop = false;
        while (true) {
            try {
                socket = ss.accept();
                System.out.println("Connected: " + socket.getLocalAddress());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            new ServerThread(socket).start();
        }

//        try {
//            ServerSocket ss = new ServerSocket(80);
//            Socket socket = ss.accept();
//            System.out.println("Connected: " + socket.getInetAddress());
//
//            DataInputStream in = new DataInputStream(socket.getInputStream()); //where you can get the message from the socket
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //where you can write the message to the socket
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//            String clientMessage = "", serverMessage;
//            while (!clientMessage.equals("stop")) {
//                clientMessage = in.readUTF(); // read message from client
//                System.out.println("Client: " + clientMessage);
//
//                serverMessage = br.readLine(); //message from command line
//                out.writeUTF(serverMessage); //write the message to output
//                out.flush();
//            }
//
//            //close everything
//            ss.close();
//            socket.close();
//            in.close();
//            out.close();
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

/*
GOAL:
    Client connects to the server and gets the initial message (done)
    From there, the client can make requests for new data (similar to web browser I think)
 */

class ServerThread extends Thread {
    Socket socket;
    ServerThread(Socket s) {
        this.socket = s;
    }

    public void start() {
        DataInputStream in;
        DataOutputStream out;
        BufferedReader br;
        try {
            in = new DataInputStream(socket.getInputStream()); //where you can get the message
            out = new DataOutputStream(socket.getOutputStream()); //where you can write the message to the socket
//            out.writeUTF("Successful connection, welcome"); //send a message to show success
            br = new BufferedReader(new InputStreamReader(System.in));


            String clientMessage = "", serverMessage, prev = "";
            while (!clientMessage.equalsIgnoreCase("stop")) {
                clientMessage = in.readUTF();
//                if (!clientMessage.equals(prev)) {
//                    prev = clientMessage;
                    System.out.println("Client: " + clientMessage);

                    serverMessage = br.readLine();
                    out.writeUTF(serverMessage);
//                    if (clientMessage.contains("GET")) {
//                        System.out.println("Get request received");
//                        serverMessage = "you have gotten the new page";
//                        out.writeUTF(serverMessage);
//                    }
//
//                    if (clientMessage.contains("POST")) {
//                        System.out.println("Post request received");
//                        serverMessage = "thank you for sending your data";
//                        out.writeUTF(serverMessage);
//                    }

                    out.flush();
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
