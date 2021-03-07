package GUI;

//import Client.*;
import Server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Frame extends JPanel implements ActionListener {
    static final long serialVersionUID = 1L;
    
    private final JFrame frame;
    private final int width = 500, height = 500;

    private final JComboBox<String> typeOfUser;
    private String options[] = {"Client", "Server"};

    private final JButton select;

    boolean serverIsCreated = false, clientIsCreated = false;
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
        typeOfUser.setFont(new Font("default", Font.PLAIN, 22));
        typeOfUser.setVisible(true);

        select = new JButton("Start");
        select.setFont(new Font("default", Font.BOLD, 24));
        Dimension d2 = select.getPreferredSize();
        select.setBounds(new Rectangle(250-(d2.width/2), 300-(d2.height/2), d2.width, d2.height));
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
                assert !clientIsCreated;
                System.out.println("Create: "  + typeOfUser.getSelectedItem());
//                frame.getContentPane().removeAll();
//                frame.repaint();

//                ClientFrame clientFrame = new ClientFrame(this);
//                clientFrame.setFrame();
                new ClientFrame();
                clientIsCreated = true;

                frame.repaint();
            } else if (selected.equals("Server")) {
                assert !clientIsCreated;
                System.out.println("Create: "  + typeOfUser.getSelectedItem());
//                frame.getContentPane().removeAll();
//                frame.repaint();
                Server server = new Server();
//                ServerFrame serverFrame = new ServerFrame(server, this);
//                serverFrame.setFrame();
                new ServerFrame(server);
                serverIsCreated = true;

                frame.repaint();
            }

        }
    }
}
