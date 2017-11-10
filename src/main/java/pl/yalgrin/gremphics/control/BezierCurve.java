package pl.yalgrin.gremphics.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
            sum += binomialCoeff(points.size() - 1, i).
                    multiply(BigDecimal.valueOf(t).pow(i)).
                    multiply(BigDecimal.valueOf(1 - t).pow(points.size() - i - 1)).
                    multiply(BigDecimal.valueOf(axis.getValue(points.get(i)))).
                    doubleValue();
        }
        return sum;
    }

    private class TwoIntegers {
        private int a, b;

        public TwoIntegers(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TwoIntegers that = (TwoIntegers) o;

            if (a != that.a) return false;
            return b == that.b;
        }

        @Override public int hashCode() {
            int result = a;
            result = 31 * result + b;
            return result;
        }
    }

    private Map<TwoIntegers, BigDecimal> coeffCache = new HashMap<>();

    private BigDecimal binomialCoeff(int n, int k) {
        TwoIntegers nk = new TwoIntegers(n, k);
        if (coeffCache.containsKey(nk)) {
            return coeffCache.get(nk);
        }
        BigDecimal res = binomialCoeffInternal(n, k);
        coeffCache.put(nk, res);
        return res;
    }

    private BigDecimal binomialCoeffInternal(int n, int k) {
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
