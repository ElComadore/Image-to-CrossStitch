package reducer;

import java.awt.image.BufferedImage;

public class colourMatrix extends intro{
    private rgb[][] innerPixels;            //The complete clean cut
    private rgb[][] outerPixels;            //The outer rim that needs extra
    private int row;
    private int col;
    private int hop;

    public colourMatrix(BufferedImage image) {      //Initialization
        this.row = calcRow(image);
        this.col = calcCol(image, this.row);
        this.hop = calcHop(image);

        this.innerPixels = calcInnerPixels(image, hop);
        this.outerPixels = calcOuterPixels();
    }

    public colourMatrix(colourMatrix mat){      //To create an empty inner and outer pixel thing, probably not the greatest method
        this.row = mat.getRow();
        this.col = mat.getCol();
        this.hop = mat.getHop();

        this.innerPixels = new rgb[mat.getInnerPixels().length][mat.getInnerPixels()[0].length];
        this.outerPixels = null;
    }

    private int calcHop(BufferedImage image){                           //How many pixels are condensed into one
        int detSide = Math.max(image.getHeight(), image.getWidth());

        if(detSide == image.getWidth()){        //This is some bullshit maths that just sorta works
            return detSide/col;
        }else{
            return detSide/row;
        }
    }

    private rgb[][] calcInnerPixels(BufferedImage image, int hop) {  //Does the averaging for the clean cut stuff
        int skipRow = (image.getHeight() - row*hop)/2;      //How many in you need to go, like the dough around the stencil
        int skipCol = (image.getWidth() - col*hop)/2;

        rgb[][] prime = new rgb[row][col];      //A matrix of the right size
        int x = -1;                                                         //Now comes the real retarded garbage
        for(int i = skipRow; i < hop*row + skipRow-1; i = i + hop){
            x++;
            int y = -1;
            for(int j = skipCol; j < hop*col + skipCol-1; j = j + hop){
                y++;
                try {
                    prime[x][y] = averInner(i,j, image);
                }catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println(i + " " + j + " calc");      //Error catching, there was some whack errors
                }
            }
        }
        return prime;
    }

    private rgb[][] calcOuterPixels(){
        return null;
    }

    public rgb averInner(int i, int j, BufferedImage image){  //Aggregates the pixels
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
                }catch (Exception e){System.out.println(k + " " + l + " Aver");}
            }
        }
        int redAver = redSum / (hop*hop);               //Exactly what you think
        int greenAver = greenSum / (hop*hop);
        int blueAver = blueSum / (hop*hop);
        int alphaAver = alphaSum / (hop*hop);
        return new rgb(redAver, greenAver, blueAver, alphaAver);
    }

    public colourMatrix replace(colourSpace space){         //Replaces the matrix with the reduced number of colours
        colourMatrix replaced = new colourMatrix(this);
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){
                rgb curColour = this.getInnerPixels(i,j);
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

    public BufferedImage toImage(){             //Converts the matrix into a "properly" resized image
        BufferedImage image = new BufferedImage(this.col*this.hop, this.row*this.hop, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < this.col; x++){
            for(int y = 0; y < this.row; y++){
                for(int xAcross = x*this.hop; xAcross < (x+1)*this.hop; xAcross++){
                    for(int yAcross = y*this.hop; yAcross < (y+1)*this.hop; yAcross++){         //Just bad, I should use the other setter
                        image.setRGB(xAcross, yAcross, this.getInnerPixels(y,x).getColour());   //but I'm not sure how
                    }
                }
            }
        }
        return image;
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
    public rgb[][] getInnerPixels(){return this.innerPixels; }
    public void setInnerPixels(rgb col, int i, int j){
        this.innerPixels[i][j] = col;
    }
    public rgb getInnerPixels(int i, int j){
        return this.innerPixels[i][j];
    }
    public rgb[][] getOuterPixels(){
        return this.outerPixels;
    }
}
