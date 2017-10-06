package pl.yalgrin.gremphics.shape;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.List;

public class Circle extends javafx.scene.shape.Circle implements IShape {
    private boolean isDragging = false;
    private boolean draggingEnabled = false;
    private int lastX, lastY;

    public Circle() {
        super(50, 50, 100);

        setCenterX(200);
        setCenterY(200);
        radiusProperty().set(100);

        setOnMouseDragged(this::onDrag);
        setOnMouseReleased(this::onRelease);
    }

    private void onDrag(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY || !draggingEnabled) {
            return;
        }

        if (!isDragging) {
            lastX = (int) event.getX();
            lastY = (int) event.getY();
            isDragging = true;
            return;
        }

        int xDiff = (int) event.getX() - lastX;
        int yDiff = (int) event.getY() - lastY;

        centerXProperty().set(centerXProperty().getValue() + xDiff);
        centerYProperty().set(centerYProperty().getValue() + yDiff);

        lastX = (int) event.getX();
        lastY = (int) event.getY();
    }

    private void onRelease(MouseEvent event) {
        isDragging = false;
    }

    @Override
    public List<NamedProperty> getBoundProperties() {
        return Arrays.asList(new NamedProperty("CENTER_X", centerXProperty()),
                new NamedProperty("CENTER_Y", centerYProperty()),
                new NamedProperty("RADIUS", radiusProperty()));
    }

    @Override
    public void setDragging(boolean enabled) {
        draggingEnabled = enabled;
    }
}
