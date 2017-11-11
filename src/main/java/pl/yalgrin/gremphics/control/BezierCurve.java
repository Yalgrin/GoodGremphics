package pl.yalgrin.gremphics.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pl.yalgrin.gremphics.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class BezierCurve {

    private BezierCanvas canvas;
    private ObservableList<BezierPoint> points = FXCollections.observableArrayList();

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
        for (double t = 0.0; t <= 1.0; t += 0.002) {
            graphicsContext.lineTo(getCoord(t, Axis.X), getCoord(t, Axis.Y));
        }
        graphicsContext.stroke();
        graphicsContext.closePath();
    }

    private static Map<Pair<Double, Pair<Integer, Integer>>, Double> valueCache;
    private static Map<Pair<Integer, Integer>, BigDecimal> coeffCache;

    static {
        coeffCache = new HashMap<>();
        valueCache = new HashMap<>();
    }

    private double getCoord(double t, Axis axis) {
        double sum = 0.0;
        int pointCount = points.size();
        int degree = pointCount - 1;
        for (int i = 0; i < pointCount; i++) {
            sum += axis.getValue(points.get(i)) * getValue(t, degree, i);
        }
        return sum;
    }

    private static double getValue(double t, int degree, int i) {
        Pair<Double, Pair<Integer, Integer>> key = new Pair<>(t, new Pair<>(degree, i));
        if (valueCache.containsKey(key)) {
            return valueCache.get(key);
        }

        double v = binomialCoeff(degree, i).
                multiply(BigDecimal.valueOf(t).pow(i)).
                multiply(BigDecimal.valueOf(1 - t).pow(degree - i)).doubleValue();
        valueCache.put(key, v);
        return v;
    }

    private static BigDecimal binomialCoeff(int n, int k) {
        Pair<Integer, Integer> nk = new Pair<>(n, k);
        if (coeffCache.containsKey(nk)) {
            return coeffCache.get(nk);
        }
        BigDecimal res = binomialCoeffInternal(n, k);
        coeffCache.put(nk, res);
        return res;
    }

    private static BigDecimal binomialCoeffInternal(int n, int k) {
        if (n - k < k) {
            return binomialCoeff(n, n - k);
        }
        if (k == 0) {
            return BigDecimal.ONE;
        }
        if (k == 1) {
            return BigDecimal.valueOf(n);
        }
        BigDecimal res = BigDecimal.ONE;
        for (int i = n - k + 1; i <= n; i++) {
            res = res.multiply(BigDecimal.valueOf(i));
        }
        for (int i = 2; i <= k; i++) {
            res = res.divide(BigDecimal.valueOf(i), RoundingMode.HALF_DOWN);
        }
        return res;
    }

    public ObservableList<BezierPoint> getPoints() {
        return points;
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
