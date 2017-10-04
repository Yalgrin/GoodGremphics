package pl.yalgrin.gremphics.shape;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class NamedProperty {
    private String name;
    private IntegerProperty property;

    public NamedProperty(String name, IntegerProperty property) {
        this.name = name;
        this.property = property;
    }

    public NamedProperty(String name, DoubleProperty doubleProperty) {
        this(name, new SimpleIntegerProperty());
        this.property.bindBidirectional(doubleProperty);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProperty() {
        return property.get();
    }

    public IntegerProperty propertyProperty() {
        return property;
    }

    public void setProperty(int property) {
        this.property.set(property);
    }
}
