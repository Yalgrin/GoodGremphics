package pl.yalgrin.gremphics.shape;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public interface ICircle extends IShape {
    IntegerProperty startXProperty = new SimpleIntegerProperty(0), startYProperty = new SimpleIntegerProperty(0);
    IntegerProperty radiusProperty = new SimpleIntegerProperty(0);

    public default IntegerProperty startXProperty() {
        return startXProperty;
    }

    public default IntegerProperty startYProperty() {
        return startYProperty;
    }

    public default void setStart(int x, int y) {
        startXProperty.set(x);
        startYProperty.set(y);
    }

    public default IntegerProperty circleRadiusProperty() {
        return radiusProperty;
    }
}
