package pl.yalgrin.gremphics.controller;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pl.yalgrin.gremphics.control.PolygonCanvas;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TransformationViewController extends AbstractController {
    @FXML
    public AnchorPane centerPane;

    @FXML
    public VBox propertyPane;

    private PolygonCanvas canvas;

    private Map<Spinner<Integer>, IntegerProperty> spinnerIntegerPropertyMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        canvas = new PolygonCanvas();
        centerPane.getChildren().add(canvas);
    }
}
