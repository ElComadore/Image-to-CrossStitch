package Interface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MonitorPanel extends JPanel {
    Image img;
    public MonitorPanel() {
        //add components

        try {
            img = ImageIO.read(new File("C:\\Users\\coeno\\Desktop\\CrossStitch\\Converter\\998Convert.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void paintComponent(Graphics g)
    {
        //paint background image
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        //g.drawImage(img, 0, 0, this);

    }

}
