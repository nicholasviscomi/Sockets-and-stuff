package GUI;

import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends JPanel implements ActionListener {

//    Frame frame;
    private final JFrame frame;
    private int width = 500, height = 500;

    JPanel init, connectToUser, chat;

    JTextField serverAddress;
    JButton connect, chatWith;

    Client client;

    public ClientFrame() {
        frame = new JFrame();
//        frame.setContentPane(this);
//        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Client");
        frame.setSize(width, height);
        frame.pack();
        frame.setLocationRelativeTo(null);

        serverAddress = new JTextField("Server Address");
        Dimension d1 = serverAddress.getPreferredSize();
        serverAddress.setBounds(100, 100, 200, 30);
        serverAddress.setVisible(true);

        connect = new JButton("Connect");
        Dimension d2 = connect.getPreferredSize();
        connect.setBounds(310, 100, 150, 30);
        connect.setBackground(Color.BLUE);
        connect.addActionListener(this);

        init = new JPanel();
        init.setPreferredSize(new Dimension(width, height));
        init.add(serverAddress);
        init.add(connect);

//        frame.add(serverAddress);
//        frame.add(connect);
        frame.setContentPane(init);
        frame.setSize(width, height);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect) {
            System.out.println("Attempting to connect");
            try {
                frame.remove(init);
                initConnectToUserFrame();
                frame.setContentPane(connectToUser);
                frame.validate();
                frame.repaint();
                System.out.println("new frame created");

                System.out.println("Creating Client");
//                client = new Client(serverAddress.getText(), 80);
                System.out.println("Client created");

            } catch (NumberFormatException nfe) {
                System.out.println("invalid user");
            } catch (Exception ignored) {
                System.out.println("error in client init");
            }
        } else if (e.getSource() == chatWith) {
            frame.remove(connectToUser);
            initChatFrame();
            frame.setContentPane(chat);
            frame.validate();
            frame.repaint();
        }
    }

    private void initConnectToUserFrame() {
        connectToUser = new JPanel();
        connectToUser.setPreferredSize(new Dimension(width, height));

        JLabel username = new JLabel("Username: ");
        username.setBounds(10, 10, 300, 50);
        username.setVisible(true);

        JTextField userToConnectTo = new JTextField("Username to chat with: ");
        userToConnectTo.setBounds(10, 230, 400, 50);
        userToConnectTo.setVisible(true);

        chatWith = new JButton("Chat");
        chatWith.setBounds(415, 230, chatWith.getPreferredSize().width, 50);
        chatWith.setVisible(true);
        chatWith.addActionListener(this);

        connectToUser.add(username);
        connectToUser.add(userToConnectTo);
        connectToUser.add(chatWith);
        connectToUser.setVisible(true);
    }

    private void initChatFrame() {
        chat = new JPanel();
        chat.setPreferredSize(new Dimension(width, height));


    }

    public static void main(String[] args) {
        new ClientFrame();
    }

}
