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

    public void onPointDragged(PolygonPoint point, double x, double y) {
        canvas.onPointDragged(point, x, y);
    }

    public void onPointDragStarted(PolygonPoint point, double x, double y) {
        canvas.onPointDragStarted(point, x, y);
    }

    public void onPointDragStopped(PolygonPoint point, double x, double y) {
        canvas.onPointDragStopped(point, x, y);
    }

    public void onPointRemoved(PolygonPoint point) {
        points.remove(point);
        canvas.onPointRemoved(point);
    }

    public void onPointClicked(PolygonPoint point) {
        canvas.onPointClicked(point);
    }

    public PolygonPoint addPoint(int x, int y) {
        PolygonPoint point = new PolygonPoint(this, x, y);
        points.add(point);
        return point;
    }

    public void setMode(PolygonCanvas.Mode mode) {
        canvas.setMode(mode);
    }

    public PolygonCanvas.Mode getMode() {
        return canvas.getMode();
    }

    public void translate(double x, double y) {
        for (PolygonPoint point : points) {
            point.setX(point.getX() + x);
            point.setY(point.getY() + y);
        }
    }

    public void rotate(double x0, double y0, double startX, double startY, double rotationX, double rotationY) {
        rotate(x0, y0, radsBetween(x0, y0, startX, startY, rotationX, rotationY));
    }

    public void rotate(double x0, double y0, double rad) {
        for (PolygonPoint point : points) {
            double x = point.getX();
            double y = point.getY();
            point.setX(x0 + (x - x0) * Math.cos(rad) - (y - y0) * Math.sin(rad));
            point.setY(y0 + (x - x0) * Math.sin(rad) + (y - y0) * Math.cos(rad));
        }
    }

    public void scale(double x0, double y0, double startX, double startY, double endX, double endY) {
        double scaleX = (endX - x0) / (startX - x0);
        double scaleY = (endY - y0) / (startY - y0);
        if (startX == x0) {
            scaleX = 0.0;
        }
        if (startY == y0) {
            scaleY = 0.0;
        }
        scale(x0, y0, scaleX, scaleY);
    }

    public void scale(double x0, double y0, double scaleX, double scaleY) {
        if (Double.isNaN(scaleX)) {
            scaleX = 0.0;
        }
        if (Double.isNaN(scaleY)) {
            scaleY = 0.0;
        }
        System.out.println("KX: " + scaleX + ", KY: " + scaleY);
        for (PolygonPoint point : points) {
            double x = point.getX();
            double y = point.getY();
            point.setX(x * scaleX + (1 - scaleX) * x0);
            point.setY(y * scaleY + (1 - scaleY) * y0);
        }
    }

    private double radsBetween(double centerX, double centerY, double firstX, double firstY, double secondX, double secondY) {
        double rad = Math.atan2(centerX - firstX, centerY - firstY) - Math.atan2(centerX - secondX, centerY - secondY);
        if (rad < 0.0) {
            rad += Math.PI * 2;
        }
        return rad;
    }
}
