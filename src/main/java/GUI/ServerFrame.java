package GUI;

import Server.*;

import javax.swing.*;
import java.awt.*;

public class ServerFrame extends JFrame {
    JFrame frame;
    private final int width = 500, height = 500;

    Server server;

    public ServerFrame(Server server) {
        this.server = server;

        setLayout(null);

        frame = new JFrame();
        frame.setContentPane(this);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Server");
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
