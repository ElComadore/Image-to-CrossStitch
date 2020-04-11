package reducer.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ReducerStatus extends Pane {
    private TextField info;
    private Button convert;

    public ReducerStatus(EventHandler<ActionEvent> convertHandler, Stage stage) {
        this.setWidth(stage.getWidth());
        this.setHeight((int)(stage.getHeight()*.2));
        convert = createConvert();
        convert.relocate(stage.getWidth()*.1, this.getHeight()/2);
        convert.setOnAction(convertHandler);


        info = createInfo();
        info.setDisable(true);
        info.relocate(stage.getWidth()*.6,0);
        info.setPrefSize(stage.getWidth()*.4,this.getHeight());

        this.getChildren().add(convert);
        this.getChildren().add(info);
    }
    public void updateHeight(double height){
        this.setHeight(height);
        this.convert.relocate(this.getWidth()*.1,this.getHeight()/2);
        this.info.setPrefSize(this.info.getPrefWidth(),this.getHeight());
    }
    public void updateWidth(double width){
        this.setWidth(width);
        this.convert.relocate(this.getWidth()*.1,this.convert.getLayoutY());
        this.info.setPrefSize(this.getWidth()*.4,this.getHeight());
        this.info.relocate(this.getWidth()*.6,0);
    }

    private Button createConvert(){
        return new Button("Convert");
    }
    public void isConvertable(Boolean b){
        convert.setDisable(!b);
    }

    private TextField createInfo(){
        return new TextField();
    }
    public void updateInfo(){}
    public void clearInfo(){}
}
