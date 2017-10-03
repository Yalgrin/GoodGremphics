package pl.yalgrin.gremphics.shape;

import javafx.scene.input.MouseEvent;

public class Rectangle extends javafx.scene.shape.Rectangle implements IRectangle {
    private boolean isDragging = false;
    private int lastX, lastY;

    public Rectangle() {
        super(50, 50, 100, 100);

        setStart(50, 50);
        setEnd(200, 200);

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

    @Override public InterestPoint getInterestPoint() {
        return null;
    }
}
