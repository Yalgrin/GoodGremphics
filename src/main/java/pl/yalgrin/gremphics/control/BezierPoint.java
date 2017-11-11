package pl.yalgrin.gremphics.control;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import pl.yalgrin.gremphics.shape.NamedProperty;

import java.util.ArrayList;
import java.util.List;

public class BezierPoint extends Circle {
    private boolean isDragging = false;
    private double curShiftX, curShiftY;
    private List<NamedProperty> namedProperties = new ArrayList<>();

    public BezierPoint(BezierCurve bezierCurve, int x, int y) {
        super(x, y, 5);
        setFill(Color.BLUE);

        centerXProperty().addListener((observable, oldValue, newValue) -> bezierCurve.onPointDragged());
        centerYProperty().addListener((observable, oldValue, newValue) -> bezierCurve.onPointDragged());

        setOnMouseDragged(e -> {
            if (!isDragging || e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            curShiftX = e.getX();
            curShiftY = e.getY();
            setCenterX(curShiftX);
            setCenterY(curShiftY);
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

        NamedProperty namedPropertyX = new NamedProperty("X", centerXProperty());
        NamedProperty namedPropertyY = new NamedProperty("Y", centerYProperty());
        namedProperties.add(namedPropertyX);
        namedProperties.add(namedPropertyY);
    }

    public List<NamedProperty> getBoundProperties() {
        return namedProperties;
    }
}
