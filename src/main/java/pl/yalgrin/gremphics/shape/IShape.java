package pl.yalgrin.gremphics.shape;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public interface IShape {
    IntegerProperty pivotXProperty = new SimpleIntegerProperty(0), pivotYProperty = new SimpleIntegerProperty(0);

    public default IntegerProperty pivotXProperty() {
        return pivotXProperty;
    }

    public default IntegerProperty pivotYProperty() {
        return pivotYProperty;
    }

    public default void setPivot(int x, int y) {
        pivotXProperty.set(x);
        pivotYProperty.set(y);
    }

    public InterestPoint getInterestPoint();
}
