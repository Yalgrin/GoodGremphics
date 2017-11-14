package pl.yalgrin.gremphics.control;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PolygonPoint extends Circle {
    private boolean isDragging = false;
    private double curShiftX, curShiftY;
    private PolygonShape polygonShape;

    public PolygonPoint(PolygonShape polygonShape, int x, int y) {
        super(x, y, 5);
        setFill(Color.BLACK);

        this.polygonShape = polygonShape;

//        centerXProperty().addListener((observable, oldValue, newValue) -> polygonShape.onPointDragged());
//        centerYProperty().addListener((observable, oldValue, newValue) -> polygonShape.onPointDragged());

        setOnMouseDragged(e -> {
            if (!isDragging || e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            curShiftX = e.getX();
            curShiftY = e.getY();
            polygonShape.onPointDragged(this, curShiftX, curShiftY);
        });

        setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                polygonShape.onPointDragStarted(this, e.getX(), e.getY());
                isDragging = true;
            }
        });

        setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                polygonShape.onPointRemoved(this);
            } else if (e.getButton() == MouseButton.PRIMARY) {
                polygonShape.onPointClicked(this);
            }
        });

        setOnMouseReleased(e -> {
            isDragging = false;
            polygonShape.onPointDragStopped(this, e.getX(), e.getY());
        });
    }

    public double getX() {
        return getCenterX();
    }

    public double getY() {
        return getCenterY();
    }

    public void setX(double val) {
        setCenterX(val);
    }

    public void setY(double val) {
        setCenterY(val);
    }

    public PolygonShape getShape() {
        return polygonShape;
    }
}
