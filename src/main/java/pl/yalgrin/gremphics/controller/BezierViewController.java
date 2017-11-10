package pl.yalgrin.gremphics.controller;

import javafx.beans.property.IntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pl.yalgrin.gremphics.control.BezierCanvas;
import pl.yalgrin.gremphics.control.BezierPoint;
import pl.yalgrin.gremphics.shape.NamedProperty;

import java.net.URL;
import java.util.ResourceBundle;

public class BezierViewController extends AbstractController {
    @FXML
    public AnchorPane centerPane;

    @FXML
    public VBox propertyPane;

    private BezierCanvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        canvas = new BezierCanvas();
        centerPane.getChildren().add(canvas);

        canvas.getPoints().addListener((ListChangeListener<? super BezierPoint>) c -> {
            rebuildSidebar();
        });
    }

    private void rebuildSidebar() {
        propertyPane.getChildren().clear();
        for (BezierPoint point : canvas.getPoints()) {
            propertyPane.getChildren().add(new Label(Integer.toString(canvas.getPoints().indexOf(point))));
            for (NamedProperty namedProperty : point.getBoundProperties()) {
                Spinner<Integer> spinner = new Spinner<>(0, 1000, 0, 1);
                spinner.setEditable(true);
                IntegerProperty.integerProperty(spinner.getValueFactory().valueProperty()).bindBidirectional(namedProperty.propertyProperty());
                propertyPane.getChildren().add(spinner);
            }
        }
    }
}
