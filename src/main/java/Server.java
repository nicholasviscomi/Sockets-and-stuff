import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(80);
            Socket socket = ss.accept();
            System.out.println("Connected: " + socket.getInetAddress());

            DataInputStream in = new DataInputStream(socket.getInputStream()); //where you can get the message from the socket
            DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //where you can write the message to the socket
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String clientMessage = "", serverMessage;
            while (!clientMessage.equals("stop")) {
                clientMessage = in.readUTF(); // read message from client
                System.out.println("Client: " + clientMessage);

                serverMessage = br.readLine(); //message from command line
                out.writeUTF(serverMessage); //write the message to output
                out.flush();
            }

            //close everything
            ss.close();
            socket.close();
            in.close();
            out.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//class ServerThread {
//    Socket socket;
//    ServerThread(Socket s) {
//        this.socket = s;
//    }
//
//    public void start() {
//        DataInputStream in;
//        DataOutputStream out;
//        BufferedReader br;
//        try {
//            in = new DataInputStream(socket.getInputStream()); //where you can get the message from the socket
//            out = new DataOutputStream(socket.getOutputStream()); //where you can write the message to the socket
//            br = new BufferedReader(new InputStreamReader(System.in));
//        } catch (Exception e) {
//            return;
//        }
//
//        String message;
//        while (true) {
//            try {
//                System.out.println("Client (" + socket.getLocalAddress() + "): " + in.readUTF());
//
//                message = br.readLine();
//                if (message == null) {
//                    socket.close();
//                    in.close();
//                    out.close();
//                    br.close();
//                    return;
//                } else {
//                    out.writeUTF(message);
//                    out.flush();
//                }
//            } catch (Exception e) {
//                return;
//            }
//        }
//    }
//}
