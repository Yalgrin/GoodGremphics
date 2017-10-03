package pl.yalgrin.gremphics.shape;

import javafx.scene.input.MouseEvent;

public class Circle extends javafx.scene.shape.Circle implements ICircle {
    private boolean isDragging = false;
    private int lastX, lastY;

    public Circle() {
        super(50, 50, 100);

        setStart(200, 200);
        circleRadiusProperty().set(100);

        centerXProperty().bind(startXProperty);
        centerYProperty().bind(startYProperty);
        radiusProperty().bind(circleRadiusProperty());
        radiusProperty().bind(circleRadiusProperty());

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

        lastX = (int) event.getX();
        lastY = (int) event.getY();
    }

    private void onRelease(MouseEvent event) {
        isDragging = false;
    }

    @Override public InterestPoint getInterestPoint() {
        return null;
    }
}
