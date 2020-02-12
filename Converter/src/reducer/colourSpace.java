package reducer;

import java.util.ArrayList;
import java.util.List;

public class colourSpace{
    private List<rgb> colours;

    public colourSpace(colourMatrix mat){       //True initialization
        this.colours = new ArrayList<>();
        createColourList(mat);
        sortByAmount();                         //Sorting allows for easier reduction
        checkAmounts();                         //Check to be sure I have the right number of coloured pixels
        reduce();                               //Create a more concise list
        sortByAmount();                         //Possibly unnecessary, but is not that much
        System.out.println(colours.size());                 //Another check
        System.out.println(colours.get(0).getAmount());     //Another check
        checkAmounts();                                     //If it's the same as above we gucci
    }

    public colourSpace(colourSpace space, int i){       //Is for the second reduced based on user required amount
        this.colours = space.getColours();
        reduce(i);
        checkAmounts();                                 //To see if it's the same
        System.out.println(colours.size());
        System.out.println(colours.get(0).getAmount());
    }

    private void createColourList(colourMatrix mat){
        for(int i = 0; i < mat.getRow(); i++){
            for(int j = 0; j < mat.getCol(); j++){
                rgb aver = mat.getInnerPixels(i,j);
                if(colours.size() != 0){
                    checkList(aver);
                }else{
                    colours.add(aver);
                }
            }
        }
        checkAmounts();
    }
    private void checkList(rgb aver){
        for(rgb colour : colours){
            if(aver.getDec() == colour.getDec()){
                colour.anotherFew(aver.getAmount());
                return;
            }
        }
        colours.add(aver);
    }
    private void sortByAmount(){
        int stopPos = 0;
        while(this.colours.size()-stopPos > 0){
            int inARow = 1;
            for(int i = this.colours.size() - 1; i > stopPos; i--){
                if(this.colours.get(i-1).getAmount() < this.colours.get(i).getAmount()){
                    rgb temp = new rgb(this.colours.get(i-1));
                    this.colours.set(i-1, this.colours.get(i));
                    this.colours.set(i, temp);
                }else{
                    inARow++;
                }
            }
            stopPos++;
            if(inARow == this.colours.size()){
                break;
            }
        }
    }
    private void reduce(){
        while(colours.size() > 500){
            rgb curColour = colours.get(colours.size() - 1);
            double closest = -1;
            int id = -1;
            boolean notChanged = true;

            for(int i = 0; i < colours.size() - 1; i++){
                double dist = getDist(colours.get(i), curColour);
                if(dist < 0){
                    colours.get(i).anotherFew(curColour.getAmount());
                    notChanged = false;
                    break;
                }else if(closest == -1 || dist < closest){
                    closest = dist;
                    id = i;
                }
            }
            if(notChanged){
                colours.get(id).anotherFew(curColour.getAmount());
            }
            colours.remove(colours.size() - 1);
            reinsert(id);
        }
    }
    private void reinsert(int index){
        rgb temp = colours.get(index);
        for(rgb col : colours){
            if(col.getAmount() < temp.getAmount()){
                colours.add(colours.indexOf(col), temp);
                colours.remove(index + 1);
                break;
            }
        }
    }

    private void reduce(int amount){
        while(colours.size() > amount){
            rgb curColour = colours.get(colours.size() - 1);
            double closest = -1;
            int id = -1;
            boolean notChanged = true;

            for(int i = 0; i < colours.size() - 1; i++){
                double dist = getDist(colours.get(i), curColour);
                if(dist < 0){
                    colours.get(i).anotherFew(curColour.getAmount());
                    notChanged = false;
                    break;
                }else if(closest == -1 || dist < closest){
                    closest = dist;
                    id = i;
                }
            }
            if(notChanged){
                colours.get(id).anotherFew(curColour.getAmount());
            }
            colours.remove(colours.size() - 1);
        }
    }

    public double getDist(rgb first, rgb second){
        double red = Math.pow(first.getRed() - second.getRed(), 2);
        double green = Math.pow(first.getGreen()-second.getGreen(), 2);
        double blue = Math.pow((first.getBlue() - second.getBlue()), 2);
        return Math.sqrt(red + green + blue);
    }
    private void checkAmounts() {
        int sum = 0;
        for (rgb cols : colours) {
            sum = sum + cols.getAmount();
        }
        System.out.println(sum);
    }

    public List<rgb> getColours(){
        return this.colours;
    }
}
