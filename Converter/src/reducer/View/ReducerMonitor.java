package reducer.View;

import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;

public class ReducerMonitor extends Pane {
    BufferedImage image;

    public ReducerMonitor(BufferedImage image){
        this.image = image;
    }
}
