package GUI;

import Server.*;

import javax.swing.*;
import java.awt.*;


public class ServerFrame extends JPanel {

    private final JFrame frame;
    private final int width = 500, height = 500;

    JLabel connectionsTitle;
    int numConnections = 0;

    JTable table;

    String[][] tableData = new String[100][3];

    Server server;

    public ServerFrame(Server server) {
        this.server = server;
        server.serverFrame = this;

        frame = new JFrame();
        frame.setContentPane(this);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Server");
        frame.setSize(width, height);
        frame.pack();
        frame.setLocationRelativeTo(null);

        connectionsTitle = new JLabel("Number of Connections: " + numConnections);
        Dimension d1 = connectionsTitle.getPreferredSize();
        connectionsTitle.setBounds(250-(d1.width/2), 20, d1.width, d1.height);
        connectionsTitle.setVisible(true);
//        serverAddr = new JLabel(server.getServerSocket().getInetAddress().getHostAddress());
//        Dimension d2 = serverAddr.getPreferredSize();
//        serverAddr.setBounds(20, 15, 250-(d2.width/2), d2.height);
//        serverAddr.setVisible(true);

        String[] columnNames = {"Port #", "IP", "Connected to Port #"};
        table = new JTable(tableData, columnNames);
        table.setBounds(20, 100, 460, 400);
        table.setGridColor(Color.BLACK);
        table.getTableHeader().setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(table);

        frame.add(scroll);
        frame.add(connectionsTitle);
//        frame.add(serverAddr);
        frame.setVisible(true);
    }

    public void updateTable(String portNum, String IP, String connectedTo) {
        tableData[numConnections] = new String[] {portNum, IP, connectedTo};
    }

    public static void main(String[] args) {}
}
