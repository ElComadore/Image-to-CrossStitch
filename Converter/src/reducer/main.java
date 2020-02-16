package reducer;

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
        File file = null;
        try {
            file = new File(getUserFile());
            image = ImageIO.read(file);
        }catch (IOException e){System.out.println(("Not an image my dude"));
        }

        colourMatrix colMat = new colourMatrix(image);          //Convert image to matrix
        colourSpace colSpace = new colourSpace(colMat);         //Generate colour space from matrix
        //colSpace.toPantone()                                                              //Change to pantone colour space
        colourSpace reduced = new colourSpace(colSpace, getUserColours(colSpace));          //Create a reduced colour list, <500
        colourMatrix replaced = colMat.replace(reduced);                                //Create a matrix out of the reduced colours
        BufferedImage done = replaced.toImage();                //Make the image while resizing it
        File outFile = new File(file.getName().substring(0, file.getName().length()-4) + "Convert" + file.getName().substring(file.getName().length()-4));
        ImageIO.write(done, file.getName().substring(file.getName().length()-3), outFile);
        System.exit(1);
    }

    static String getUserFile() {       //Gets the image, or simply file
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

    static int getUserColours(colourSpace space){       //Gets how many colours the user wants
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
    static void displayImage(BufferedImage img){        //Shows the image, need to make one that saves
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }
}