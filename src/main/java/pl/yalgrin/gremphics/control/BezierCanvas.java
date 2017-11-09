package pl.yalgrin.gremphics.control;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

public class BezierCanvas extends Pane {

    private Canvas canvas;
    private BezierCurve bezierCurve;

    public BezierCanvas() {
        canvas = new Canvas(1000, 1000);
        bezierCurve = new BezierCurve(this);

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            BezierPoint point = bezierCurve.addPoint((int) event.getX(), (int) event.getY());
            getChildren().add(point);
            point.toFront();
            draw();
        });

        getChildren().addAll(canvas);
        draw();
    }

    public void onPointRemoved(BezierPoint point) {
        getChildren().remove(point);
        draw();
    }

    public void draw() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        bezierCurve.draw(graphicsContext);
    }
}
