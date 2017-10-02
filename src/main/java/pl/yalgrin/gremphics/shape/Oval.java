package pl.yalgrin.gremphics.shape;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class Oval extends RectangularShape {
    @Override public void draw(GraphicsContext graphicsContext) {
        graphicsContext.fillRect(startX, startY, endX - startX, endY - startY);
    }

    @Override public List<InterestPoint> getResizePoints() {
        return null;
    }
}
