package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class BezierViewController extends AbstractController {
    @FXML
    public AnchorPane centerPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Canvas canvas = new Canvas(1000, 1000);
        centerPane.getChildren().addAll(canvas);

        Point a = new Point(100, 100);
        Point b = new Point(400, 0);
        Point c = new Point(150, 1000);
        Point d = new Point(450, 100);
        GraphicsContext g2d = canvas.getGraphicsContext2D();
        g2d.setLineWidth(2);
        g2d.setStroke(Color.BLACK);
        g2d.setFill(Color.BLACK);
        PixelWriter pixelWriter = g2d.getPixelWriter();
        g2d.moveTo(100, 100);
        int x, y;
        for (double t = 0.0; t <= 1.0; t += 0.001) {
            x = (int) (a.getX() * Math.pow(1 - t, 3) + 3 * b.getX() * t * Math.pow(1 - t, 2) + 3 * c.getX() * t * t * (1 - t) + d.getX() * t * t * t);
            y = (int) (a.getY() * Math.pow(1 - t, 3) + 3 * b.getY() * t * Math.pow(1 - t, 2) + 3 * c.getY() * t * t * (1 - t) + d.getY() * t * t * t);
            g2d.lineTo(x, y);
        }
        g2d.stroke();
        g2d.closePath();
    }
}
