package pl.yalgrin.gremphics.shape;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.List;

public abstract class Shape {
    protected Point pivotPoint = new Point();

    public abstract void draw(GraphicsContext graphicsContext);

    public abstract List<InterestPoint> getResizePoints();

    public Point getPivotPoint() {
        return pivotPoint;
    }

    public void setPivotPoint(Point pivotPoint) {
        this.pivotPoint = pivotPoint;
    }
}
