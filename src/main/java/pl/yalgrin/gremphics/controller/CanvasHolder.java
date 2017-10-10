package pl.yalgrin.gremphics.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;

public class CanvasHolder extends Pane {
    private Canvas canvas;
    private BufferedImage image;

    public CanvasHolder() {

    }

    public void setImage(BufferedImage image) {
        this.image = image;
        canvas = new Canvas(image.getWidth(), image.getHeight());
        canvas.getGraphicsContext2D().drawImage(SwingFXUtils.toFXImage(image, null), 0, 0);
        getChildren().addAll(canvas);
    }
}
