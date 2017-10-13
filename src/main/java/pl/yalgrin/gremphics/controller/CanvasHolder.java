package pl.yalgrin.gremphics.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasHolder extends Pane {
    private Canvas canvas;
    private Image image;

    private boolean isDragging = false;
    private double lastX, lastY;
    private double curShiftX, curShiftY;
    private double curScale = 1;

    public CanvasHolder() {

    }

    public void setImage(Image image) {
        this.image = image;
        canvas = new Canvas(image.getWidth() + 4, image.getHeight() + 4);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, image.getWidth() + 4, image.getHeight() + 4);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(1, 1, image.getWidth() + 2, image.getHeight() + 2);
        graphicsContext.drawImage(image, 2, 2);
        getChildren().addAll(canvas);


        setOnMouseDragged(e -> {
            if (!isDragging) {
                return;
            }
            curShiftX -= lastX - e.getX();
            curShiftY -= lastY - e.getY();
            canvas.relocate(curShiftX, curShiftY);
            lastX = e.getX();
            lastY = e.getY();
        });

        setOnMousePressed(e -> {
            isDragging = true;
            lastX = e.getX();
            lastY = e.getY();
        });

        setOnMouseReleased(e -> {
            isDragging = false;
        });

        setOnScroll(e -> {
            curScale *= Math.pow(1.05, e.getDeltaY() / 40.0);
            canvas.setScaleX(curScale);
            canvas.setScaleY(curScale);
        });
    }
}
