package GUI;

import Client.*;
import Server.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Frame extends JPanel implements ActionListener {

    private JFrame frame;
    private final int width = 500, height = 500;

    private JComboBox<String> typeOfUser;
    private String options[] = {"Client", "Server"};

    private JButton select;


    public Frame() {
        setLayout(null);

        frame = new JFrame();
        frame.setContentPane(this);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Main");
        frame.setSize(width, height);
        frame.pack();
        frame.setLocationRelativeTo(null);

        typeOfUser = new JComboBox<>(options);
        Dimension d1 = typeOfUser.getPreferredSize();
        typeOfUser.setBounds(new Rectangle(250-(d1.width), 230-(d1.height), d1.width * 2, d1.height * 2));
        typeOfUser.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        typeOfUser.setVisible(true);

        select = new JButton("Start");
        Dimension d2 = select.getPreferredSize();
        select.setBounds(new Rectangle(250-(d2.width), 300-(d2.height), d2.width * 2, d2.height * 2));
        select.setFont(new Font("Times New Roman", Font.BOLD, 24));
        select.setForeground(Color.BLACK);
        select.addActionListener(this);
        select.setVisible(true);

        frame.add(typeOfUser);
        frame.add(select);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == select) {
            String selected = (String) typeOfUser.getSelectedItem();
            assert selected != null;
            if (selected.equals("Client")) {
                System.out.println("Create: "  + typeOfUser.getSelectedItem());
                Client client = new Client();
                new ClientFrame(client);
            } else if (selected.equals("Server")) {
                System.out.println("Create: "  + typeOfUser.getSelectedItem());
                //maybe can make server object oriented since this is the only place I will be instantiating a Server object
                //Pass this server object to anyone other class that may need it
                Server server = new Server();
                new ServerFrame(server);
            }

        }
    }
}
