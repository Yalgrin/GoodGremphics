package pl.yalgrin.gremphics.shape;

import javafx.beans.property.IntegerProperty;

public abstract class InterestPoint {
    public abstract IntegerProperty XProperty();

    public abstract IntegerProperty YProperty();

    public abstract void onMoved(int x, int y);
}
