package Server;

import Server.Exceptions.Error;
import Server.Exceptions.NoSuchUserException;
import Server.Exceptions.UserAlreadyConnectedException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;


public class ClientHandler implements Runnable {
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

        } catch (UserAlreadyConnectedException userAlreadyConnectedException) {

            try {
                out.writeUTF(Error.UserAlreadyConnected.name());
            } catch (IOException e) {
                System.out.println("error writing User already connected");
            }

        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("another Server.ClientHandler exception ");
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
                } else {

                    out = new DataOutputStream(Server.getClients().get(recipient).getOutputStream());
                    out.writeUTF(clientMessage);
                    out.flush();
                }

            } catch (Exception e) {
//                e.printStackTrace();
                if (e.getClass() == NoSuchUserException.class || e.getClass() == UserAlreadyConnectedException.class) {
                    try {
                        getUserToChatWith(out, in);
                    } catch (Exception ignored) {}
                }
                System.out.println("error in Server.ClientHandler loop");
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

        if (!userIsOnline(recipient)) { //make sure user is finding a real user and not themselves
            throw new NoSuchUserException();
        } else {
            if (Server.getConnections().get(recipient) == 234023483) { //BROKEN
                throw new UserAlreadyConnectedException();
            } else {
                System.out.println("Connected to (" + recipient + ")");
                this.out = new DataOutputStream(Server.getClients().get(recipient).getOutputStream());
            }
        }

        return recipient;
    }

    boolean userIsOnline(int id) {
        //CLIENTS DOES NOT REMOVE A CLIENT WHEN THE GO OFFLINE
//        System.out.println("clients: " + Server.Server.getClients());
        HashMap<Integer, Socket> clients = Server.getClients();
        Socket user = clients.get(id);

        return (user != null) && (!(user.getPort() == socket.getPort()));
    }
}