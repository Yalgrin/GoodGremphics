package pl.yalgrin.gremphics.control;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class CanvasHolder extends Pane {
    private Canvas canvas;
    private Image image;

    private boolean isDragging = false;
    private double lastX, lastY;
    private double curShiftX, curShiftY;
    private double curScale = 1, lastTranslateX = 0, lastTranslateY = 0;

    public CanvasHolder() {

    }

    public void setImage(Image image) {
        this.image = image;
        getChildren().clear();
        canvas = new Canvas(image.getWidth(), image.getHeight());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(image, 0, 0);
        getChildren().addAll(canvas);

        setOnMouseDragged(e -> {
            if (!isDragging) {
                return;
            }
            curShiftX -= lastX - e.getX();
            curShiftY -= lastY - e.getY();
            canvas.relocate(curShiftX, curShiftY);
            lastTranslateX = canvas.getLayoutX();
            lastTranslateY = canvas.getLayoutY();
            lastX = e.getX();
            lastY = e.getY();
        });

        setOnMousePressed(e -> {
            isDragging = true;
            lastX = e.getX();
            lastY = e.getY();
        });

        setOnMouseReleased(e -> isDragging = false);

        setOnScroll(e -> {
            curScale *= Math.pow(1.05, e.getDeltaY() / 40.0);
            canvas.setScaleX(curScale);
            canvas.setScaleY(curScale);
        });

        canvas.setLayoutX(lastTranslateX);
        canvas.setLayoutY(lastTranslateY);
        canvas.setScaleX(curScale);
        canvas.setScaleY(curScale);
    }
}
