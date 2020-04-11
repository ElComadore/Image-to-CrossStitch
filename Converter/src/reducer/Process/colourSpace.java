package reducer.Process;

import java.util.ArrayList;
import java.util.List;

public class colourSpace{
    private List<rgb> colours;

    public colourSpace(colourMatrix mat){       //True initialization
        colours = new ArrayList<>();
        createColourList(mat);
        sortByAmount();                         //Sorting allows for easier reduction
        checkAmounts();                         //Check to be sure I have the right number of coloured pixels
        System.out.println(colours.size());
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

    private void createColourList(colourMatrix mat){        //Concentrates the matrix into a minimum colour list
        for(rgb[] rgbInner : mat.getInnerPixels()){
            for(rgb inners : rgbInner){
                if(colours.size() == 0){
                    colours.add(inners);
                }else{
                    checkList(inners);
                }
            }
        }

        checkAmounts();                 //Always with the checking, though it really aint necessary
    }
    private void checkList(rgb aver){   //Checks to see if the image is in the colour list
        for(rgb colour : colours){
            try {
                if (aver.getDec() == colour.getDec()) {
                    try {

                        colour.anotherFew(aver.getAmount());
                    } catch (Exception e) {
                        System.out.println("Error in the adding of amounts");
                        System.exit(0);
                    }
                    return;
                }
            }catch (NullPointerException e){System.out.println("Error in the decimals"); System.exit(0);}
        }
        colours.add(aver);
    }
    private void sortByAmount(){            //Bubble sort by amount
        int stopPos = 0;
        while(colours.size()-stopPos > 0){
            int inARow = 1;
            for(int i = colours.size() - 1; i > stopPos; i--){
                if(colours.get(i-1).getAmount() < colours.get(i).getAmount()){
                    rgb temp = new rgb(colours.get(i-1));
                    colours.set(i-1, colours.get(i));
                    colours.set(i, temp);
                }else{
                    inARow++;
                }
            }
            stopPos++;
            if(inARow == colours.size()){
                break;
            }
        }
    }
    private void reduce(){                  //Initial reduction
        while(colours.size() > 500){
            rgb curColour = colours.get(colours.size() - 1); //Start at the bottom
            double closest = -1;
            int id = -1;
            boolean notChanged = true;

            for(int i = 0; i < colours.size() - 1; i++){
                double dist = getDist(colours.get(i), curColour);   //Euclidean distance
                if(dist < 0){                                       //If you want to make it a softer boundary. instead of nearest
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
            reinsert(id);                               //Reorganises the list so that the colour is in the right place
        }                                               //Gets weird results if you don't do this
    }
    private void reinsert(int index){                   //Insert into list at right area
        rgb temp = colours.get(index);
        for(rgb col : colours){
            if(col.getAmount() < temp.getAmount()){
                colours.add(colours.indexOf(col), temp);
                colours.remove(index + 1);          //List is one longer, so you need to delete the one 'below'
                break;
            }
        }
    }

    private void reduce(int amount){                //User controlled reduction, otherwise similar
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
            reinsert(id);
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
