package reducer.Process;

import java.awt.image.BufferedImage;

public class colourMatrix extends intro {
    private rgb[][] innerPixels;            //The complete clean cut
    private rgb[][] resized;
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

    private rgb[][] initialResize2(BufferedImage image){
        int cutRow = (image.getHeight() - row*hop);      //How many in you need to go, like the dough around the stencil
        int cutCol = (image.getWidth() - col*hop);
        int cutRowRatio;
        int cutColRatio;

        try {
            cutRowRatio = image.getHeight()/cutRow;
        }catch (ArithmeticException a){cutRowRatio = Integer.MAX_VALUE;}
        try {
            cutColRatio = image.getWidth()/cutCol;
        }catch (ArithmeticException a ){cutColRatio = Integer.MAX_VALUE;}

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

    /*private int greatestDivisor(int a, int b){
        if(a == 0 || b == 0){
            return Math.max(a,b);
        }
        int q = a/b;
        int r = a - q*b;

        if(r != 0){
             return greatestDivisor(b, r);
        }
        return b;
    }*/

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
    public rgb[][] getResized(){
        return  resized;
    }
}
