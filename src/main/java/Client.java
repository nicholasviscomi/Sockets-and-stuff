import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client {
    public static void main(String[] args) {
        DataOutputStream out = null;
        DataInputStream in = null;
        BufferedReader br = null;
        Socket socket;
        int recipient = 0;

        try {
            socket = new Socket("localhost", 80);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            br = new BufferedReader(new InputStreamReader(System.in));

            String username = in.readUTF();
            System.out.println("Username: " + username);

            System.out.print(in.readUTF()); //"enter username to chat with"
            recipient = Integer.parseInt(br.readLine());
            out.writeUTF(String.valueOf(recipient));
        } catch (NumberFormatException numberFormatException) {
            System.out.println("invalid username input");
            System.exit(0);
        } catch (Exception ignored) {
            System.out.println("exception in client initialization");
        }

        //NEED TO ADD AN ERROR ON THE CLIENT SIDE IF THE USER IS NOT ONLINE

        final DataOutputStream finalOut = out;
        final BufferedReader finalBr = br;
        Thread sendMessage = new Thread(() -> {
            while (true) {
                try {
                    String clientMessage = finalBr.readLine();
                    finalOut.writeUTF(clientMessage);
                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("failure in send loop :(");
                    System.exit(0);
                    break;
                }
            }
        });

        final DataInputStream finalIn = in;
        final int finalRecipient = recipient;
        Thread readMessage = new Thread(() -> {
            while (true) {
                try {
                    if (finalIn != null) {
                        String servMessage = finalIn.readUTF();
                        System.out.println(addPortNum(servMessage, finalRecipient));
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("failure in read loop :(");
                    System.exit(0);
                    break;
                }
            }
        });

        Thread errorListener = new Thread(() -> {
            while (true) {
                try {
                    int error = finalIn.readInt();
                    System.out.println();
                } catch (Exception e) {
                    System.exit(0);
                }
            }
        });

//            while (!clientMessage.equals("stop")) {
//                clientMessage = br.readLine(); //read message from terminal
//
//                out.writeUTF(addUsername(clientMessage, username)); //write the message to where teh server can get it
//                out.flush(); //clear the output stream
//
//                servMessage = in.readUTF(); //read message from the server
//                System.out.println(servMessage); //print server message
//            }

        readMessage.start();
        sendMessage.start();
        errorListener.start();
    }

    static String addPortNum(String msg, int username) {
        return "(" + username + ") " + msg;
    }
}

