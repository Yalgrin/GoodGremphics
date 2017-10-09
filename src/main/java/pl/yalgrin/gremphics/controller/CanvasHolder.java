package pl.yalgrin.gremphics.controller;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import pl.yalgrin.gremphics.shape.IShape;

public class CanvasHolder extends Pane {
    private Canvas canvas = new Canvas(1000, 1000);
    private Shape currentlyDraggedShape;

    public CanvasHolder() {
        super();
        getChildren().add(canvas);
    }

    public void addShape(Shape shape) {
        getChildren().add(shape);
        canvas.toBack();
    }

    public Shape getShapeAt(int x, int y) {
        for (int i = getChildren().size() - 1; i >= 0; i--) {
            Node child = getChildren().get(i);

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

    public void setDraggingEnabled(boolean enabled) {
        for (Node child : getChildren()) {
            if (child == canvas) {
                continue;
            }

            IShape shape = (IShape) child;
            shape.setDragging(enabled);
        }
    }

    public void removeShape(Shape shape) {
        getChildren().removeAll(shape);
    }

    public void startDrag(Shape shape) {
        if (currentlyDraggedShape != null) {
            getChildren().removeAll(currentlyDraggedShape);
        }
        currentlyDraggedShape = shape;
        getChildren().add(currentlyDraggedShape);
    }

    public void cancelDrag() {
        if (currentlyDraggedShape != null) {
            getChildren().removeAll(currentlyDraggedShape);
        }
        currentlyDraggedShape = null;
    }

    public void endDrag() {
        currentlyDraggedShape = null;
    }
}
