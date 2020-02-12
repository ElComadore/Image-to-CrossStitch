package reducer;

import java.awt.image.BufferedImage;
import java.util.Scanner;

public class intro {                //What the user sees upon start up

    public int calcRow(BufferedImage image){
        final Scanner sc = new Scanner(System.in);
        System.out.println("The dimensions of this image are " + image.getHeight() + "x" + image.getWidth());
        System.out.println("How many stitches down do you want (max: " + image.getHeight() +")?");
        int row = sc.nextInt();
        row = checkNum(row, image.getTileHeight());
        return row;
    }

    public int calcCol(BufferedImage image, int row){
        final Scanner sc = new Scanner(System.in);
        int maxCol = (image.getWidth()*row)/image.getHeight();
        System.out.println("And how many across (max: " + maxCol + ")?");
        int col = sc.nextInt();
        col = checkNum(col, maxCol);
        return col;
    }

    private int checkNum(int num, int max){
        final Scanner scNum = new Scanner(System.in);
        if(num > max) {
            System.out.println("Please input a valid number");
            num = scNum.nextInt();
            if (num > max) {
                System.exit(0);
            }
        }
        return num;
    }
}
