package pl.yalgrin.gremphics.shape;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Line extends Shape {
    private class BeginningPoint extends InterestPoint {

        @Override
        public Point getPoint() {
            return beginningPoint;
        }

        @Override
        public void onMoved(int x, int y) {
            beginningPoint.setLocation(x, y);
        }
    }

    private class EndPoint extends InterestPoint {

        @Override
        public Point getPoint() {
            return endPoint;
        }

        @Override
        public void onMoved(int x, int y) {
            endPoint.setLocation(x, y);
        }
    }

    private Point beginningPoint = new Point(50, 50), endPoint = new Point(150, 150);
    private InterestPoint begIntPoint = new BeginningPoint(), endIntPnt = new EndPoint();

    public Line() {
        pivotPoint.setLocation(100, 100);
    }

    @Override
    public void setPivotPoint(Point pivotPoint) {
        beginningPoint.setLocation(beginningPoint.getX() - this.pivotPoint.getX() + pivotPoint.getX(),
                beginningPoint.getY() - this.pivotPoint.getY() + pivotPoint.getY());
        endPoint.setLocation(endPoint.getX() - this.pivotPoint.getX() + pivotPoint.getX(),
                endPoint.getY() - this.pivotPoint.getY() + pivotPoint.getY());
        super.setPivotPoint(pivotPoint);
    }

    @Override public void draw(GraphicsContext graphicsContext) {
        graphicsContext.strokeLine(beginningPoint.getX(), beginningPoint.getY(), endPoint.getX(), endPoint.getY());
    }

    @Override
    public List<InterestPoint> getResizePoints() {
        return Arrays.asList(begIntPoint, endIntPnt);
    }
}
