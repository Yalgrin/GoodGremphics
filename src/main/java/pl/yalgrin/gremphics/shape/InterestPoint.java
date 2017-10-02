package pl.yalgrin.gremphics.shape;

import java.awt.*;

public abstract class InterestPoint {
    public abstract Point getPoint();

    public abstract void onMoved(int x, int y);
}
