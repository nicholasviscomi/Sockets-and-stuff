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


class ClientHandler implements Runnable {
    Socket socket;
    DataOutputStream out;

    ClientHandler(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        DataInputStream in = null;
        out = null;

        int recipient = 0;

        try {
            in = new DataInputStream(socket.getInputStream()); //where you can get the message
            out = new DataOutputStream(socket.getOutputStream()); //where you can write the message to the socket
        } catch (IOException ignored) {}

        try {
            assert out != null; assert in != null;
            out.writeUTF(String.valueOf(socket.getPort())); //send user their port number aka username
            out.flush();

            recipient = getUserToChatWith(out, in);
            connectToUser(recipient, out);

        } catch (NumberFormatException nfe) {

            //input to username was not a number
            //should already be handled in Client.java
            try {
                out.writeUTF(Error.InvalidUsername.name());
            } catch (IOException e) {
                System.out.println("error writing invalid username");
            }

        } catch (NoSuchUserException noSuchUserException) {

            //no user was found online
            try {
                out.writeUTF(Error.NoSuchUser.name());
            } catch (IOException e) {
                System.out.println("error writing No such user");
            }

        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("another ClientHandler exception ");
        }

        //ERROR: HAVE A BOOLEAN THAT SAYS WHETHER SOMEONE IS ALREADY CHATTING SO THERE ARE NO OVERLAPS IN CONVERSATIONS

        String clientMessage;
        while (true) {
            try {

                clientMessage = in.readUTF();
                if (clientMessage.equalsIgnoreCase("leave")) { //close both clients, ideally would just disconnect them

                    Socket recipientSocket = Server.getClients().get(recipient);
                    out = new DataOutputStream(socket.getOutputStream()); //stop writing to the other client

                    Server.clientHandlers.get(recipientSocket.getPort()).out = new DataOutputStream(recipientSocket.getOutputStream()); //reset who the other user writes to

                    recipient = getUserToChatWith(out, in);
                    connectToUser(recipient, out);
                } else {

//                DataOutputStream recipientOut = new DataOutputStream(Server.getClients().get(recipient).getOutputStream());
                    out.writeUTF(clientMessage);
                    out.flush();
                }

            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("error in ClientHandler loop");
                break;
            }
        }

        try {
            socket.close();
        } catch (Exception ignored) {}


    }

    public int getUserToChatWith(DataOutputStream out, DataInputStream in) throws Exception {
        out.writeUTF("Enter username to chat with: ");
        String recipientStr = in.readUTF();
        int recipient = Integer.parseInt(recipientStr);
        System.out.println("Attempting to connect to (" + recipient + ")");

        return recipient;
    }

    public void connectToUser(int recipient, DataOutputStream out) throws Exception {
        if (!userIsOnline(recipient)) { //make sure user is finding a real user and not themselves
            throw new NoSuchUserException();
        } else {
            System.out.println("Connected to (" + recipient + ")");
            this.out = new DataOutputStream(Server.getClients().get(recipient).getOutputStream());
        }
    }

    boolean userIsOnline(int id) {
        //CLIENTS DOES NOT REMOVE A CLIENT WHEN THE GO OFFLINE
//        System.out.println("clients: " + Server.getClients());
        HashMap<Integer, Socket> clients = Server.getClients();
        Socket user = clients.get(id);

        return (user != null) && (!(user.getPort() == socket.getPort()));
    }
}


