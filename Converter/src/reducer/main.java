package reducer;

import jdk.jshell.execution.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class main {
    public static void main(String[] args) throws IOException {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(getUserFile()));
        }catch (IOException e){
        }

        int colour = image.getRGB(0,0);
        System.out.println(colour);
        rgb col = new rgb(colour);
        System.out.println(col.getRed());
        System.out.println(col.getGreen());
        System.out.println(col.getBlue());

        colourMatrix colMat = new colourMatrix(image);
        colourSpace colSpace = new colourSpace(colMat);
        //colSpace.toPantone()
        colourSpace reduced = new colourSpace(colSpace, getUserColours(colSpace));
        colourMatrix replaced = colMat.replace(reduced);
        BufferedImage done = replaced.toImage();
        displayImage(done);
    }

    static String getUserFile() {
        String fullFileName = null;
        JFrame yourJFrame = new JFrame();
        FileDialog fd = new FileDialog(yourJFrame, "Choose a file", FileDialog.LOAD);
        fd.setDirectory("C:\\");
        fd.setVisible(true);
        String filename = fd.getFile();

        if (filename != null) {
            fullFileName = fd.getDirectory() + fd.getFile();
            System.out.println("The file you chose was: " + fd.getFile());
        }else {
            System.out.println("You have not selected anything!");
            System.exit(0);
        }
        return fullFileName;
    }

    static int getUserColours(colourSpace space){
        final Scanner sc = new Scanner(System.in);
        System.out.println("How many colours do you want (max: " + space.getColours().size() + ")?");
        int amount = sc.nextInt();
        if(amount > space.getColours().size()){
            System.out.println("Please enter a valid number: ");
            amount = sc.nextInt();
            if(amount > space.getColours().size()){
                System.exit(0);
            }
        }
        return amount;
    }
    static void displayImage(BufferedImage img){
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }
}