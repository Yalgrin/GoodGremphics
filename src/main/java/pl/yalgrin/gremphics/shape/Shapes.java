package pl.yalgrin.gremphics.shape;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum Shapes {
    LINE(Line.class), RECTANGLE(Rectangle.class), OVAL(Oval.class);

    private Class<? extends Shape> shapeClass;

    Shapes(Class<? extends Shape> shapeClass) {
        this.shapeClass = shapeClass;
    }

    public Shape instantiate() {
        try {
            Constructor<? extends Shape> contructor = shapeClass.getConstructor();
            return contructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
