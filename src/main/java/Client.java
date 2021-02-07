import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalTime;


public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 80);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));



            String servMessage = in.readUTF(), clientMessage;
            System.out.println("Server: " + servMessage);

//            clientMessage = br.readLine();
            out.writeUTF("connection Started" + " (" + LocalTime.now() + ")");

//            clientMessage = br.readLine(); //have the program not end for a little bit
//            out.writeUTF(clientMessage);

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

