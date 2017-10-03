package pl.yalgrin.gremphics.shape;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public interface IRectangle extends IShape {
    IntegerProperty startXProperty = new SimpleIntegerProperty(0), startYProperty = new SimpleIntegerProperty(0);
    IntegerProperty endXProperty = new SimpleIntegerProperty(0), endYProperty = new SimpleIntegerProperty(0);

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

    public default IntegerProperty endXProperty() {
        return endXProperty;
    }

    public default IntegerProperty endYProperty() {
        return endYProperty;
    }

    public default void setEnd(int x, int y) {
        endXProperty.set(x);
        endYProperty.set(y);
    }
}
