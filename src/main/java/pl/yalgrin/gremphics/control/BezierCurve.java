package pl.yalgrin.gremphics.control;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BezierCurve {

    private BezierCanvas canvas;
    private List<BezierPoint> points = new ArrayList<>();

    public BezierCurve(BezierCanvas canvas) {
        this.canvas = canvas;
    }

    public BezierPoint addPoint(int x, int y) {
        BezierPoint point = new BezierPoint(this, x, y);
        points.add(point);
        return point;
    }

    public void onPointDragged() {
        canvas.draw();
    }

    public void onPointRemoved(BezierPoint point) {
        points.remove(point);
        canvas.onPointRemoved(point);
    }

    public void draw(GraphicsContext graphicsContext) {
        if (points.size() < 2) {
            return;
        }

        graphicsContext.setLineWidth(2);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.beginPath();
        graphicsContext.moveTo(points.get(0).getCenterX(), points.get(0).getCenterY());
        int x, y;
        for (double t = 0.0; t <= 1.0; t += 0.001) {
            graphicsContext.lineTo(getCoord(t, Axis.X), getCoord(t, Axis.Y));
        }
        graphicsContext.stroke();
        graphicsContext.closePath();
    }

    private double getCoord(double t, Axis axis) {
        double sum = 0.0;
        for (int i = 0; i < points.size(); i++) {
            sum += axis.getValue(points.get(i)) * binomialCoeff(points.size() - 1, i) * Math.pow(t, i) * Math.pow(1 - t, points.size() - i - 1);
        }
        return sum;
    }

    private long binomialCoeff(int n, int k) {
        if (n - k < k) {
            return binomialCoeff(n, n - k);
        }
        if (k == 0) {
            return 1;
        }
        if (k == 1) {
            return n;
        }
        BigDecimal res = BigDecimal.ONE;
        for (int i = n - k + 1; i <= n; i++) {
            res = res.multiply(BigDecimal.valueOf(i));
        }
        for (int i = 2; i <= k; i++) {
            res = res.divide(BigDecimal.valueOf(i), RoundingMode.HALF_DOWN);
        }
        return res.longValueExact();
    }

    private enum Axis {
        X, Y;

        private double getValue(BezierPoint point) {
            switch (this) {
                case X:
                    return point.getCenterX();
                case Y:
                    return point.getCenterY();
            }
            return 0;
        }
    }
}
