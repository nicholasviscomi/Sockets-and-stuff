import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 80);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String clientMessage = "", servMessage;
            while (!clientMessage.equals("stop")) {
                clientMessage = br.readLine(); //read message from terminal
                out.writeUTF(clientMessage); //write the message to where teh server can get it
                out.flush(); //clear the output stream

                servMessage = in.readUTF(); //read message from the server
                System.out.println("Server: " + servMessage);
            }

            //close everything
            in.close();
            out.close();
            socket.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
