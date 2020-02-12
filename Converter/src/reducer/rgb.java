package reducer;

public class rgb {
    private int colour;             //The bit number
    private int red;
    private int green;
    private int blue;
    private int alpha;              //Not sure if I even need this
    private int dec;                //Replaceable by the colour, but fuck it
    private int amount;

    public rgb(int colour){
        this.colour = colour;
        this.red = colour & 0xff;
        this.green = (colour & 0xff00) >> 8;            //Bit conversion, which is kinda cool I guess
        this.blue = (colour & 0xff0000) >> 16;
        this.alpha = (colour & 0xff000000) >>16;
        this.dec = 1000000*this.red + 1000*this.green + this.blue;
        this.amount = 1;
    }
    public rgb(rgb colour){
        this.colour = colour.getColour();
        this.red = colour.getRed();
        this.green = colour.getGreen();
        this.blue = colour.getBlue();
        this.alpha = colour.getAlpha();
        this.dec = colour.getDec();
        this.amount = colour.getAmount();
    }
    public rgb(int red, int green, int blue, int alpha){
        this.colour = alpha << 24 | blue << 16 | green << 8 | red;      //The reconversion, this took , too long to figure out
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.dec = 1000000*this.red + 1000*this.green + this.blue;
        this.amount = 1;
    }

    public void anotherFew(int amt){
        this.amount = this.amount + amt;
    }
    public int getColour(){return this.colour; }
    public int getRed(){
        return this.red;
    }
    public int getGreen(){
        return this.green;
    }
    public int getBlue(){
        return this.blue;
    }
    public int getAlpha(){ return this.alpha; }
    public int getDec(){
        return this.dec;
    }
    public int getAmount(){
        return this.amount;
    }
}
