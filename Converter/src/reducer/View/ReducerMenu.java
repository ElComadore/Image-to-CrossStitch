package reducer.View;

import javafx.event.*;
import javafx.scene.control.*;


import java.util.Arrays;
import java.util.List;


public class ReducerMenu extends MenuBar {

    private final Menu File;
    private final Menu Options;
    private final Menu Help;

    public ReducerMenu(EventHandler<ActionEvent> fileHandler, EventHandler<ActionEvent> optionsHandler,
                       EventHandler<ActionEvent> helpHandler){

        File = createFileMenu();
        Options = createOptionsMenu();
        Help = createHelpMenu();

        File.getItems().forEach(item -> item.setOnAction(fileHandler));
        Options.getItems().forEach(item -> item.setOnAction(optionsHandler));
        Help.getItems().forEach(item -> item.setOnAction(helpHandler));

        this.getMenus().addAll(File, Options, Help);
    }

    public void fixMenusNotRunning(){
    }
    public void fixMenusRunning(){
    }

    private MenuItem getItemsByText(Menu menu, String text){
        return null;
    }

    private Menu createFileMenu(){
        Menu File = new Menu("File");
        List<MenuItem> items = Arrays
                .asList(new MenuItem("Pause"),
                        new MenuItem("Resume"),
                        new MenuItem("Open"),
                        new MenuItem("Save As"));
        File.getItems().addAll(items);
        return File;
    }
    private Menu createOptionsMenu(){
        Menu Options = new Menu("Options");
        ToggleGroup toggleGroup = new ToggleGroup();
        List<RadioMenuItem> items = Arrays
                .asList(new RadioMenuItem("Euclidean"),
                        new RadioMenuItem("Other methods"));

        for(RadioMenuItem r : items){
            r.setToggleGroup(toggleGroup);
        }
        items.get(0).setSelected(true);
        Options.getItems().addAll(items);
        return Options;
    }

    private Menu createHelpMenu(){
        Menu Help = new Menu("Help");
        List<MenuItem> items = Arrays
                .asList(new MenuItem("Guide"));
        return Help;
    }
}
