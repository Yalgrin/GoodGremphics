package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EditorViewController extends AbstractController {

    public static final String PARAM_IMAGE = "PARAM_IMAGE";

    @FXML
    private AnchorPane centerPane;

    @FXML
    private FlowPane buttonPane;

    @FXML
    private FlowPane propertyPane;

    private CanvasHolder canvasHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        canvasHolder = new CanvasHolder();
        centerPane.getChildren().add(canvasHolder);
    }

    public void setImage(Image image) {
        canvasHolder.setImage(image);
        controllerData.setParameter(PARAM_IMAGE, image);
    }
}
