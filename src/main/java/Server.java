import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
            out = new DataOutputStream(socket.getOutputStream()); //where you can write the message to the socket

            out.writeUTF(String.valueOf(socket.getPort())); //send user their port number aka username
            out.flush();

            out.writeUTF("Enter username to chat with: ");
            String recipientStr = in.readUTF();
            recipient = Integer.parseInt(recipientStr);
            System.out.println("Attempting to connect to (" + recipient + ")");

            if (!userIsOnline(recipient)) { //make sure user is finding a real user and not themselves
                System.out.println("no such user :(");
                throw new NoSuchUserException();
            } else {
                System.out.println("user is online!");
            }

        } catch (NumberFormatException nfe) {

            //input to username was not a number
            System.out.println("Invalid username");
            try {
                assert out != null;
                out.writeInt(ServerErrors.toInt(Error.InvalidUsername));
            } catch (IOException e) {
                System.out.println("error writing invalid username");
            }

        } catch (NoSuchUserException noSuchUserException) {

            //no user was ofnud online
            try {
                out.writeInt(ServerErrors.toInt(Error.NoSuchUser));
            } catch (IOException e) {
                System.out.println("error writing No such user");
            }

            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("error closing socket");
            }

        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("another ClientHandler exception ");
        }

        //ALLOW USERS TO DISCONNECT FROM EACH OTHER NOT JUST GET KICKED

        String clientMessage;
        while (true) {
            try {
                if (in == null) {
                    System.out.println("in == null");
                    break;
                }

                clientMessage = in.readUTF();
                if (clientMessage.equalsIgnoreCase("leave")) { //close both clients, ideally would just disconnect them
                    socket.close();
                    Server.getClients().get(recipient).close();
                }

                DataOutputStream recipientOut = new DataOutputStream(Server.getClients().get(recipient).getOutputStream());
                recipientOut.writeUTF(clientMessage);
                recipientOut.flush();

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

    boolean userIsOnline(int id) {
        //CLIENTS DOES NOT REMOVE A CLIENT WHEN THE GO OFFLINE
//        System.out.println("clients: " + Server.getClients());
        HashMap<Integer, Socket> clients = Server.getClients();
        Socket user = clients.get(id);

        return (user != null) && (!(user.getPort() == socket.getPort()));
    }
}

class NoSuchUserException extends Exception {}
