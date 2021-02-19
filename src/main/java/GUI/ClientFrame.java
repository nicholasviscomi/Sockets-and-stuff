package GUI;

import Client.Client;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {

    JFrame frame;
    private int width = 500, height = 500;

    Client client;

    public ClientFrame(Client client) {
        this.client = client;

        setLayout(null);

        frame = new JFrame();
        frame.setContentPane(this);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Client");
        frame.setSize(width, height);
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    public static void main(String[] args) {}

}
