package pl.yalgrin.gremphics.shape;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class Rectangle extends javafx.scene.shape.Rectangle implements IShape {
    private boolean isDragging = false;
    private boolean draggingEnabled = false;
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

        setFill(Color.RED);

        setOnMouseDragged(this::onDrag);
        setOnMouseReleased(this::onRelease);

        startXProperty().addListener(this::xChanged);
        endXProperty().addListener(this::xChanged);
        startYProperty().addListener(this::yChanged);
        endYProperty().addListener(this::yChanged);
    }

    private void xChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (endXProperty().subtract(startXProperty()).lessThan(0).get()) {
            int endX = endXProperty().get();
            endXProperty().set(startXProperty().get());
            startXProperty().set(endX);
        }
    }

    private void yChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (endYProperty().subtract(startYProperty()).lessThan(0).get()) {
            int endY = endYProperty().get();
            endYProperty().set(startYProperty().get());
            startYProperty().set(endY);
        }
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

    @Override
    public void setDragging(boolean enabled) {
        draggingEnabled = enabled;
    }
}
