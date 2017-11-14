package pl.yalgrin.gremphics.control;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class PolygonShape {
    private List<PolygonPoint> points = new ArrayList<>();
    private PolygonCanvas canvas;

    public PolygonShape(PolygonCanvas canvas) {
        this.canvas = canvas;
    }

    public List<PolygonPoint> getPoints() {
        return points;
    }

    public void draw(GraphicsContext gc) {
        if (points.size() < 1) {
            return;
        }

        PolygonPoint start = points.get(0);
        if (points.size() == 2) {
            PolygonPoint end = points.get(1);
            gc.moveTo(start.getX(), start.getY());
            gc.lineTo(end.getX(), end.getY());
            gc.stroke();
            gc.closePath();
            return;
        }

        gc.beginPath();
        gc.moveTo(start.getX(), start.getY());
        for (int i = 1; i < points.size(); i++) {
            PolygonPoint point = points.get(i);
            gc.lineTo(point.getX(), point.getY());
        }
        gc.lineTo(start.getX(), start.getY());
        gc.stroke();
        gc.closePath();
    }

    public void onPointDragged() {
        canvas.draw();
    }

    public void onPointRemoved(PolygonPoint point) {
        points.remove(point);
        canvas.onPointRemoved(point);
    }

    public PolygonPoint addPoint(int x, int y) {
        PolygonPoint point = new PolygonPoint(this, x, y);
        points.add(point);
        return point;
    }
}
