package reducer;

import java.awt.image.BufferedImage;

public class colourMatrix extends intro{
    private rgb[][] innerPixels;
    private rgb[][] outerPixels;
    private int row;
    private int col;
    private int hop;

    public colourMatrix(BufferedImage image) {
        this.row = calcRow(image);
        this.col = calcCol(image, this.row);
        this.hop = calcHop(image);

        this.innerPixels = calcInnerPixels(image, hop);
        this.outerPixels = calcOuterPixels();
    }

    public colourMatrix(colourMatrix mat){
        this.row = mat.getRow();
        this.col = mat.getCol();
        this.hop = mat.getHop();

        this.innerPixels = new rgb[mat.getInnerPixels().length][mat.getInnerPixels()[0].length];
        this.outerPixels = null;
    }

    private int calcHop(BufferedImage image){
        int detSide = Math.max(image.getHeight(), image.getWidth());

        if(detSide == image.getWidth()){
            return detSide/col;
        }else{
            return detSide/row;
        }
    }

    private rgb[][] calcInnerPixels(BufferedImage image, int hop) {
        int skipRow = (image.getHeight() - row*hop)/2;
        int skipCol = (image.getWidth() - col*hop)/2;

        rgb[][] prime = new rgb[row][col];
        int x = -1;
        for(int i = skipRow; i < hop*row + skipRow-1; i = i + hop){
            x++;
            int y = -1;
            for(int j = skipCol; j < hop*col + skipCol-1; j = j + hop){
                y++;
                try {
                    prime[x][y] = averInner(i,j, image);
                }catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println(i + " " + j + " calc");
                }
            }
        }
        return prime;
    }

    private rgb[][] calcOuterPixels(){
        return null;
    }

    public rgb averInner(int i, int j, BufferedImage image){
        int redSum  = 0;
        int greenSum = 0;
        int blueSum = 0;
        int alphaSum = 0;

        for(int k = i; k < i + hop; k++){
            for(int l = j; l < j + hop; l++){
                try {
                    redSum = redSum + (image.getRGB(l,k) & 0xff);
                    greenSum = greenSum + ((image.getRGB(l,k) & 0xff00) >> 8);
                    blueSum = blueSum + ((image.getRGB(l,k) & 0xff0000) >> 16);
                    alphaSum = alphaSum + ((image.getRGB(l,k) & 0xff000000) >> 24);
                }catch (Exception e){System.out.println(k + " " + l + " Aver");}
            }
        }
        int redAver = redSum / (hop*hop);
        int greenAver = greenSum / (hop*hop);
        int blueAver = blueSum / (hop*hop);
        int alphaAver = alphaSum / (hop*hop);
        return new rgb(redAver, greenAver, blueAver, alphaAver);
    }

    public colourMatrix replace(colourSpace space){
        colourMatrix replaced = new colourMatrix(this);
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){
                rgb curColour = this.getInnerPixels(i,j);
                double closest = 999;
                rgb closeColour = null;
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
                replaced.setInnerPixels(closeColour, i, j);
            }
        }
    return replaced;
    }

    public BufferedImage toImage(){
        BufferedImage image = new BufferedImage(this.col*this.hop, this.row*this.hop, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < this.col; x++){
            for(int y = 0; y < this.row; y++){
                for(int xAcross = x*this.hop; xAcross < (x+1)*this.hop; xAcross++){
                    for(int yAcross = y*this.hop; yAcross < (y+1)*this.hop; yAcross++){
                        image.setRGB(xAcross, yAcross, this.getInnerPixels(y,x).getColour());
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
