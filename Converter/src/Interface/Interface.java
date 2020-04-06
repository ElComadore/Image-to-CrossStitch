package Interface;

import javax.imageio.ImageIO;
import javax.management.MBeanAttributeInfo;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyStore;

public class Interface {
    public static void main(String[] Args) {

        JFrame frame = new JFrame("Testing testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JMenuBar menuBar = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Help");
        menuBar.add(m1);
        menuBar.add(m2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m12 = new JMenuItem("Save As");
        m1.add(m11);
        m1.add(m12);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter text");
        JTextField textField = new JTextField(20);
        JButton send = new JButton("Send");
        JButton clear = new JButton("Clear");
        panel.add(label);
        panel.add(textField);
        panel.add(send);
        panel.add(clear);

        MonitorPanel background = new MonitorPanel();
        JPanel blank1 = new JPanel();
        JPanel blank2 = new JPanel();

        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, background);
        frame.getContentPane().add(BorderLayout.EAST, blank1);
        frame.getContentPane().add(BorderLayout.WEST, blank2);
        frame.setVisible(true);
    }
}
