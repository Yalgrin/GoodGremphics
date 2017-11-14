package pl.yalgrin.gremphics.control;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

public class PolygonCanvas extends Pane {

    private Canvas canvas;
    private PolygonShape shape;

    public PolygonCanvas() {
        canvas = new Canvas(1000, 1000);
        shape = new PolygonShape(this);

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            PolygonPoint point = shape.addPoint((int) event.getX(), (int) event.getY());
            getChildren().add(point);
            point.toFront();
            draw();
        });

        getChildren().addAll(canvas);
        draw();
    }

    public void draw() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        shape.draw(graphicsContext);
    }

    public void onPointRemoved(PolygonPoint point) {
        getChildren().remove(point);
        draw();
    }
}
