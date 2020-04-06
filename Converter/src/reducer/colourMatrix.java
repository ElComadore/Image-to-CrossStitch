package reducer;

import com.sun.source.tree.CatchTree;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class colourMatrix extends intro{
    private rgb[][] innerPixels;            //The complete clean cut
    private rgb[][] resized;
    private rgb[] topCutOff;
    private rgb[] bottomCutOff;
    private rgb[] leftCutOff;
    private rgb[] rightCutOff;
    private int row;
    private int col;
    private int hop;
    private int rowRat;
    private int colRat;

    public colourMatrix(BufferedImage image) {      //Initialization
        row = calcRow(image);
        col = calcCol(image, this.row);
        hop = calcHop(image);
        rowRat = image.getHeight() / hop;
        colRat = image.getWidth() / hop;

        resized = initialResize2(image);
        innerPixels = calcInnerPixels(resized);

        //innerPixels = calcInnerPixels(image);

        //if(image.getHeight() % row != 0 && hop > 1){
        //    topCutOff = calcTopCut(image);
        //    bottomCutOff = calcBottomCut(image);
        //}
        //if(image.getWidth() % col != 0 && hop > 1) {
        //    leftCutOff = calcLeftCut(image);
        //    rightCutOff = calcRightCut(image);
        //}
    }

    public colourMatrix(colourMatrix mat){      //To create an empty inner and outer pixel thing, probably not the greatest method
        this.row = mat.getRow();
        this.col = mat.getCol();
        this.hop = mat.getHop();
        this.rowRat = mat.getRowRat();
        this.colRat = mat.getColRat();

        this.resized = mat.getResized();
        this.innerPixels = new rgb[mat.getInnerPixels().length][mat.getInnerPixels()[0].length];
    }

    private int calcHop(BufferedImage image){                           //How many pixels are condensed into one
        return Math.min(image.getHeight() / row, image.getWidth() / col);
    }

    private rgb[][] calcInnerPixels(BufferedImage image) {  //Does the averaging for the clean cut stuff
        //int skipRow = (image.getHeight() - row*hop)/2;      //How many in you need to go, like the dough around the stencil
        //int skipCol = (image.getWidth() - col*hop)/2;

        int skipRow = (image.getHeight() - hop*rowRat)/2;
        int skipCol = (image.getWidth() - hop*colRat)/2;

        rgb[][] prime = new rgb[rowRat][colRat];      //A matrix of the right size
        int x = -1;                                                         //Now comes the real retarded garbage
        for(int i = skipRow; i < hop*rowRat + skipRow; i = i + hop){
            x++;
            int y = -1;
            for(int j = skipCol; j < hop*colRat + skipCol; j = j + hop){
                y++;
                try {
                    prime[x][y] = averImage(i,j, image);
                }catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println(i + " " + j + " calc");      //Error catching, there was some whack errors
                }
            }
        }
        return prime;
    }

    public rgb averImage(int i, int j, BufferedImage image){  //Aggregates the pixels
        int redSum  = 0;
        int greenSum = 0;
        int blueSum = 0;
        int alphaSum = 0;

        for(int k = i; k < i + hop; k++){
            for(int l = j; l < j + hop; l++){
                try {
                    redSum = redSum + (image.getRGB(l,k) & 0xff);                   //Fuck this, stupid fucking order
                    greenSum = greenSum + ((image.getRGB(l,k) & 0xff00) >> 8);
                    blueSum = blueSum + ((image.getRGB(l,k) & 0xff0000) >> 16);
                    alphaSum = alphaSum + ((image.getRGB(l,k) & 0xff000000) >> 24);
                }catch (Exception e){System.out.println(k + " " + l + " AverInner");}
            }
        }
        int redAver = redSum / (hop*hop);               //Exactly what you think
        int greenAver = greenSum / (hop*hop);
        int blueAver = blueSum / (hop*hop);
        int alphaAver = alphaSum / (hop*hop);
        return new rgb(redAver, greenAver, blueAver, alphaAver);
    }

    private rgb[][] initialResize(BufferedImage image){
        int skipRow = (image.getHeight() - row*hop);      //How many in you need to go, like the dough around the stencil
        int skipCol = (image.getWidth() - col*hop);

        double skipRowRatio = ((double) image.getHeight()) / skipRow;
        double skipColRatio = ((double) image.getWidth()) / skipCol;

        double skipRowDec = skipRowRatio - (int) skipRowRatio;
        double skipColDec = skipColRatio - (int) skipColRatio;

        rgb[][] resized = new rgb[row*hop][col*hop];

        int i = 0;

        int hoppedRow = 0;
        int hoppedCol = 0;

        int rowToHop =  (int) (skipRowRatio/2);
        boolean justHoppedRow = false;

        int gcdRow = greatestDivisor(image.getHeight(), skipRow);
        int gcdCol = greatestDivisor(image.getWidth(), skipCol);

        int skipRowTen = (int) (skipRowDec*10);
        int skipColTen = (int) (skipColDec*10);

        for(int k = 0; k < image.getHeight(); k++){
            int modRow;

            if(hoppedRow % 10 < skipRowTen){
                modRow = (int) Math.ceil(skipRowRatio);
            }else{
                modRow = (int) Math.floor(skipRowRatio);
            }

            if(justHoppedRow){
                justHoppedRow = false;
                rowToHop = rowToHop + modRow;
            }

            int colToHop = (int) (skipColRatio/2);
            boolean justHoppedCol = false;

            if(k != rowToHop){
                int j = 0;

                for (int l = 0; l < image.getWidth(); l++) {

                    int modCol;

                    if(hoppedCol % 10 < skipColTen){
                        modCol = (int) Math.ceil(skipColRatio);
                    }else{
                        modCol = (int) Math.floor(skipColRatio);
                    }

                    if(justHoppedCol){
                        justHoppedCol = false;
                        colToHop = colToHop + modCol;
                    }

                    if(l != colToHop){
                        resized[i][j] = new rgb(image.getRGB(l,k));
                        j++;
                    }else{
                        hoppedCol++;
                        justHoppedCol = true;
                    }

                    //if(l % (image.getWidth()/gcdCol) == 0 && l != 0){
                    //    hoppedCol = 0;
                    //    colToHop = (int) (skipColRatio) + l;
                    //}
                }
                i++;
            }else{
                hoppedRow++;
                justHoppedRow = true;
            }

            //if(k % (image.getHeight()/gcdRow) == 0 && k != 0){
            //    hoppedRow = 0;
            //    rowToHop = (int) (skipRowRatio) + k;
            //}
        }

        return resized;
    }

    private rgb[][] initialResize2(BufferedImage image){
        int cutRow = (image.getHeight() - row*hop);      //How many in you need to go, like the dough around the stencil
        int cutCol = (image.getWidth() - col*hop);

        int cutRowRatio = image.getHeight()/cutRow;
        int cutColRatio = image.getWidth()/cutCol;

        rgb[][] resized = new rgb[row*hop][col*hop];

        int k = 0;

        int hoppedRow = 0;


        for(int i = 0; i < image.getHeight(); i++){
            int hoppedCol = 0;
            int l = 0;

            if(i%cutRowRatio != 0){
                for(int j = 0; j < image.getWidth(); j++){
                    if(j%cutColRatio != 0){
                        resized[k][l] = new rgb(image.getRGB(j,i));
                        resized[k][col*hop-l-1] = new rgb(image.getRGB(image.getWidth()-j-1, i));
                        resized[row*hop-k-1][col*hop-l-1] = new rgb(image.getRGB(image.getWidth()-j-1,image.getHeight()-i-1));
                        resized[row*hop-k-1][l] = new rgb(image.getRGB(j, image.getHeight()-i-1));
                        l++;
                    }
                    else{
                        hoppedCol++;
                        if(2*hoppedCol == cutCol - (cutCol%2)){
                            for(int jPrime = j+(cutCol%2) + 1; jPrime < image.getWidth()-j; jPrime++){
                                resized[k][l] = new rgb(image.getRGB(jPrime,i));
                                resized[row*hop-k-1][l] = new rgb(image.getRGB(jPrime, image.getHeight()-i-1));
                                l++;
                            }
                            break;
                        }
                    }
                }
                k++;
            }
            else{
                hoppedRow++;
                if(2*hoppedRow == cutRow - (cutRow%2)){
                    for(int iPrime = i+(cutRow%2) + 1; iPrime < image.getHeight()-i;iPrime++){
                        l=0;
                        hoppedCol = 0;
                        for(int j = 0; j < image.getWidth(); j++) {
                            if (j % cutColRatio != 0) {
                                resized[k][l] = new rgb(image.getRGB(j,iPrime));
                                resized[k][col*hop-l-1] = new rgb(image.getRGB(image.getWidth()-j-1, iPrime));
                                resized[row*hop-k-1][col*hop-l-1] = new rgb(image.getRGB(image.getWidth()-j-1, image.getHeight()-iPrime-1));
                                resized[row*hop-k-1][l] = new rgb(image.getRGB(j, image.getHeight()-iPrime-1));
                                l++;
                            } else {
                                hoppedCol++;
                                if (2 * hoppedCol == cutCol - (cutCol%2)) {
                                    for (int jPrime = j + (cutCol%2) + 1; jPrime < image.getWidth() - j; jPrime++) {
                                        resized[k][l] = new rgb(image.getRGB(jPrime,iPrime));
                                        resized[row*hop-k-1][l] = new rgb(image.getRGB(jPrime, image.getHeight()-iPrime-1));
                                        l++;
                                    }
                                    break;
                                }
                            }
                        }
                        k++;
                    }
                    break;
                }
            }
        }


        return resized;
    }

    private int greatestDivisor(int a, int b){
        if(a == 0 || b == 0){
            return Math.max(a,b);
        }
        int q = a/b;
        int r = a - q*b;

        if(r != 0){
             return greatestDivisor(b, r);
        }
        return b;
    }

    private rgb[][] calcInnerPixels(rgb[][] mat){
        rgb[][] prime = new rgb[row][col];

        int x = 0;
        for(int i = 0; i < mat.length; i = i + hop){
            int y = 0;
            for(int j = 0; j < mat[0].length; j = j + hop){
                prime[x][y] = averMat(mat, i, j);
                y++;
            }
            x++;
        }
        return prime;
    }

    private rgb averMat(rgb[][] mat, int i, int j){
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int alphaSum = 0;

        for(int k = i; k < i + hop; k++){
            for(int l = j; l < j + hop; l++){
                redSum = redSum+ mat[k][l].getRed();
                greenSum = greenSum + mat[k][l].getGreen();
                blueSum = blueSum + mat[k][l].getBlue();
                alphaSum = alphaSum + mat[k][l].getAlpha();
            }
        }
        int redAver = redSum / (hop*hop);
        int greenAver = greenSum / (hop*hop);
        int blueAver = blueSum / (hop*hop);
        int alphaAver = alphaSum / (hop*hop);

        return new rgb(redAver, greenAver, blueAver, alphaAver);
    }

    private rgb[] calcTopCut(BufferedImage image){
        int skipRow = (image.getHeight() - row*hop)/2;      //How many in you need to go, like the dough around the stencil
        int skipCol = (image.getWidth() - col*hop)/2;

        rgb[] prime = new rgb[col+2];

        prime[0] = averAdapt(image, 0, 0, skipRow, skipCol);
        prime[prime.length-1] = averAdapt(image, 0,skipCol + hop*col, skipRow, skipCol);

        for(int i = 1; i <= col; i++){
            prime[i] = averAdapt(image, 0, skipCol + (i-1)*hop, skipRow, hop);
        }

        return prime;
    }

    private rgb[] calcBottomCut(BufferedImage image){
        int skipRow = (image.getHeight() - row*hop)/2;      //How many in you need to go, like the dough around the stencil
        int skipCol = (image.getWidth() - col*hop)/2;

        rgb[] prime = new rgb[col+2];

        prime[0] = averAdapt(image, skipRow + hop*row, 0, skipRow, skipCol);
        prime[prime.length-1] = averAdapt(image, skipRow + row *hop,skipCol + hop*col, skipRow, skipCol);

        for(int i = 1; i <= col; i++){
            prime[i] = averAdapt(image, skipRow + hop*row, skipCol + (i-1)*hop, skipRow, hop);
        }

        return prime;
    }

    private rgb averAdapt(BufferedImage image, int startRow, int startCol, int rows, int cols){
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int alphaSum = 0;

        int numOf = 0;

        for(int k = startRow; k < startRow + rows; k++){
            for(int l = startCol; l < startCol + cols; l++){
                try {
                    redSum = redSum + (image.getRGB(l,k) & 0xff);                   //Fuck this, stupid fucking order
                    greenSum = greenSum + ((image.getRGB(l,k) & 0xff00) >> 8);
                    blueSum = blueSum + ((image.getRGB(l,k) & 0xff0000) >> 16);
                    alphaSum = alphaSum + ((image.getRGB(l,k) & 0xff000000) >> 24);
                }catch (Exception e){System.out.println(k + " " + l + " AverAdapt");}
                numOf++;
            }
        }
        try {
            int redAver = redSum / numOf;               //Exactly what you think
            int greenAver = greenSum / numOf;
            int blueAver = blueSum / numOf;
            int alphaAver = alphaSum / numOf;
            return new rgb(redAver, greenAver, blueAver, alphaAver);
        }catch (ArithmeticException e){System.out.println("Don't ask me how");}
        return null;
    }

    private rgb[] calcLeftCut(BufferedImage image){
        int skipRow = (image.getHeight() - row*hop)/2;      //How many in you need to go, like the dough around the stencil
        int skipCol = (image.getWidth() - col*hop)/2;

        rgb[] prime = new rgb[row];

        for(int i = 0; i < col; i++){
            prime[i] = averAdapt(image, skipRow + i*hop, 0, hop, skipCol);
        }

        return prime;
    }

    private rgb[] calcRightCut(BufferedImage image){
        int skipRow = (image.getHeight() - row*hop)/2;      //How many in you need to go, like the dough around the stencil
        int skipCol = (image.getWidth() - col*hop)/2;

        rgb[] prime = new rgb[row];

        for(int i = 0; i < col; i++){
            prime[i] = averAdapt(image, skipRow + i*hop, skipCol + hop*col, hop, skipCol);
        }

        return prime;
    }

    public colourMatrix replace(colourSpace space){         //Replaces the matrix with the reduced number of colours
        colourMatrix replaced = new colourMatrix(this);
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                rgb curColour = getInnerPixels(i,j);
                double closest = 999;                           //I really should do what I do in the colour space, but fuck that
                rgb closeColour = null;                         //Also all of this is kinda bad
                for(rgb colour : space.getColours()){
                    double dist = space.getDist(colour, curColour);
                    if(dist < closest){
                        closest = dist;
                        closeColour = colour;
                    }
                    if(closest < 1){
                        break;
                    }
                }
                replaced.setInnerPixels(closeColour, i, j);         //God it's such a retarded way to do this, I should make some sort
            }                                                       //of key thingy, but ah maybe in a later optimization
        }
    return replaced;
    }

    public BufferedImage toImageRatio(){             //Converts the matrix into a "properly" resized image
        BufferedImage image = new BufferedImage(colRat*hop, rowRat*hop, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < colRat; x++){
            for(int y = 0; y < rowRat; y++){
                for(int xAcross = x*hop; xAcross < (x+1)*hop; xAcross++){
                    for(int yAcross = y*hop; yAcross < (y+1)*hop; yAcross++){         //Just bad, I should use the other setter
                        image.setRGB(xAcross, yAcross, this.getInnerPixels(y,x).getColour());   //but I'm not sure how
                    }
                }
            }
        }
        return image;
    }

    public BufferedImage toImageSplit(){
        BufferedImage image = new BufferedImage(resized[0].length, resized.length, BufferedImage.TYPE_INT_RGB);
        for(int x  = 0; x < col; x++){
            for(int y = 0; y < row; y ++){
                for(int xAcross = x*hop; xAcross < (x+1)*hop; xAcross++){
                    for(int yAcross = y*hop; yAcross < (y+1)*hop; yAcross++){
                        image.setRGB(xAcross, yAcross, this.getInnerPixels(y,x).getColour());
                    }
                }
            }
        }
        return image;
    }

    public void setInnerPixels(rgb col, int i, int j){
        this.innerPixels[i][j] = col;
    }
    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public int getHop(){
        return this.hop;
    }
    public int getRowRat(){ return this.rowRat;}
    public int getColRat(){ return  this.colRat;}
    public rgb[][] getInnerPixels(){return this.innerPixels; }
    public rgb getInnerPixels(int i, int j){
        return this.innerPixels[i][j];
    }
    public rgb[] getTopCutOff() {
        return topCutOff;
    }
    public rgb[] getBottomCutOff() {
        return bottomCutOff;
    }
    public rgb[] getLeftCutOff() {
        return leftCutOff;
    }
    public rgb[] getRightCutOff() {
        return rightCutOff;
    }
    public rgb[][] getResized(){
        return  resized;
    }
}
