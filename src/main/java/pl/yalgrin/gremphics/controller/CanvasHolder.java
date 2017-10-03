package pl.yalgrin.gremphics.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import pl.yalgrin.gremphics.shape.Circle;
import pl.yalgrin.gremphics.shape.Line;
import pl.yalgrin.gremphics.shape.Rectangle;

public class CanvasHolder extends Pane {
    private Canvas canvas = new Canvas(500, 500);

    public CanvasHolder() {
        super();
        getChildren().add(canvas);
        getChildren().add(new Rectangle());
        getChildren().add(new Circle());
        getChildren().add(new Line());
    }
}
