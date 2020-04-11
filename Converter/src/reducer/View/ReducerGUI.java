package reducer.View;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import reducer.Events.*;
import reducer.Process.Reducer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;


import static reducer.Events.ModelEvent.Type.*;


public class ReducerGUI extends Application implements IEventHandler {

    private Reducer reducer;
    private ReducerMenu menu;
    private ReducerStatus status;

    public static void main(String[] Args){
        launch();
    }

    //Event Handling
    @Override
    public void onModelEvent(ModelEvent evt) {
        switch (evt.type){
            case REDUCING:
                render(REDUCING);
                break;
            case REPLACING:
                render(REPLACING);
                break;
            default:
                throw new IllegalArgumentException("Some whack shit");
        }
    }

    private void render(ModelEvent.Type type){}

    private void handleFileMenu(ActionEvent e){
        String s = ((MenuItem) e.getSource()).getText();
        switch (s) {
            case "Open":
                reducer.openFile();
                status.isConvertable(true);
                break;
            case "Save As":
                reducer.saveFileAs();
                break;
            case "Pause":
                reducer.pauseConvert();
                break;
            case "Resume":
                reducer.resumeConvert();
                break;
            default:
                throw new IllegalArgumentException("No such choice " + s);

        }
    }

    private void handleOptionsMenu(ActionEvent e){
        RadioMenuItem i = (RadioMenuItem) e.getSource();
        if(!i.isSelected()){
            System.out.println(i.getText());
        }
    }

    private void handleHelpMenu(ActionEvent e){
        String s = ((MenuItem) e.getSource()).getText();
        switch (s){
            case "Guide":
                openGuide();
                break;
            default:
                throw new IllegalArgumentException("No such option " + s);
        }
    }
    private void openGuide(){}

    private void handleConvertButton(ActionEvent e){
        reducer.start();
        status.isConvertable(false);
    }

    //Setting the Stage

    @Override
    public void start(Stage stage) throws IOException {
        stage.setWidth(700);
        stage.setHeight(700);

        GridPane gridPane = new GridPane();
        HBox menuBox = new HBox(1);
        HBox statusBox = new HBox(1);
        HBox imageBox = new HBox(1);

        //Image process
        Image img = new Image(ReducerGUI.class.getResourceAsStream("998Convert.png"));
        ImageView imgView = new ImageView(img);
        imgView.setPreserveRatio(true);

        imageBox.getChildren().add(imgView);

        //MenuTop
        menu = new ReducerMenu(this::handleFileMenu, this::handleOptionsMenu, this::handleHelpMenu);
        menuBox.getChildren().add(menu);

        //Status bottom
        status = new ReducerStatus(this::handleConvertButton, stage);
        statusBox.getChildren().add(status);

        gridPane.addRow(0,menuBox);
        gridPane.addRow(1,imageBox);
        gridPane.addRow(2, statusBox);


        //The Stage
        Scene scene = new Scene(gridPane);
        scene.heightProperty().addListener((c,o,n)->imgView.setFitHeight((Double)n*.7));
        scene.heightProperty().addListener((c,o,n)->status.updateHeight((Double) n*.3));
        scene.widthProperty().addListener((c,o,n)->status.updateWidth((Double)n));


        stage.setScene(scene);
        imgView.setFitHeight(scene.getHeight());

        stage.setMinHeight(300);
        stage.setMinWidth(300);
        stage.setTitle("Converter");
        stage.show();

        reducer = new Reducer();
        menu.fixMenusNotRunning();
        status.isConvertable(false);

        EventBus.INSTANCE.register(this);
    }

}
