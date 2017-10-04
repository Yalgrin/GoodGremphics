package pl.yalgrin.gremphics.controller;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class CanvasHolder extends Pane {
    private Canvas canvas = new Canvas(500, 500);

    public CanvasHolder() {
        super();
        getChildren().add(canvas);
    }

    public void addShape(Shape shape) {
        System.out.println(shape.hashCode());
        getChildren().add(shape);
        System.out.println(getChildren());
        canvas.toBack();
    }

    public Shape getShapeAt(int x, int y) {
        for (Node child : getChildren()) {
            if (child == canvas) {
                continue;
            }

            Shape shape = (Shape) child;
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    public void removeShape(Shape shape) {
        getChildren().removeAll(shape);
    }
}
