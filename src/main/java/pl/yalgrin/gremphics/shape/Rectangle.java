package pl.yalgrin.gremphics.shape;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.List;

public class Rectangle extends javafx.scene.shape.Rectangle implements IShape {
    private boolean isDragging = false;
    private int lastX, lastY;

    private IntegerProperty startXProperty = new SimpleIntegerProperty(0);
    private IntegerProperty startYProperty = new SimpleIntegerProperty(0);
    private IntegerProperty endXProperty = new SimpleIntegerProperty(0);
    private IntegerProperty endYProperty = new SimpleIntegerProperty(0);


    public Rectangle() {
        super(50, 50, 100, 100);

        xProperty().bind(startXProperty);
        yProperty().bind(startYProperty);
        widthProperty().bind(endXProperty.subtract(startXProperty));
        heightProperty().bind(endYProperty.subtract(startYProperty));

        setOnMouseDragged(this::onDrag);
        setOnMouseReleased(this::onRelease);
    }

    private void onDrag(MouseEvent event) {
        System.out.println(event.getX() + " " + event.getY());

        if (!isDragging) {
            lastX = (int) event.getX();
            lastY = (int) event.getY();
            isDragging = true;
            return;
        }

        int xDiff = (int) event.getX() - lastX;
        int yDiff = (int) event.getY() - lastY;

        startXProperty.set(startXProperty.getValue() + xDiff);
        startYProperty.set(startYProperty.getValue() + yDiff);
        endXProperty.set(endXProperty.getValue() + xDiff);
        endYProperty.set(endYProperty.getValue() + yDiff);

        lastX = (int) event.getX();
        lastY = (int) event.getY();
    }

    private void onRelease(MouseEvent event) {
        isDragging = false;
    }

    public IntegerProperty startXProperty() {
        return startXProperty;
    }

    public IntegerProperty startYProperty() {
        return startYProperty;
    }

    public IntegerProperty endXProperty() {
        return endXProperty;
    }

    public IntegerProperty endYProperty() {
        return endYProperty;
    }

    @Override
    public List<NamedProperty> getBoundProperties() {
        return Arrays.asList(new NamedProperty("START_X", startXProperty()),
                new NamedProperty("START_Y", startYProperty()),
                new NamedProperty("END_X", endXProperty()),
                new NamedProperty("END_Y", endYProperty()));
    }
}
