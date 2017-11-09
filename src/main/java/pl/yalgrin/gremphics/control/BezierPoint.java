package pl.yalgrin.gremphics.control;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BezierPoint extends Circle {
    private boolean isDragging = false;
    private double curShiftX, curShiftY;

    public BezierPoint(BezierCurve bezierCurve, int x, int y) {
        super(x, y, 5);
        setFill(Color.BLUE);

        setOnMouseDragged(e -> {
            if (!isDragging || e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            curShiftX = e.getX();
            curShiftY = e.getY();
            setCenterX(curShiftX);
            setCenterY(curShiftY);
            bezierCurve.onPointDragged();
        });

        setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                isDragging = true;
            }
        });

        setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                bezierCurve.onPointRemoved(this);
            }
        });

        setOnMouseReleased(e -> isDragging = false);
    }
}
