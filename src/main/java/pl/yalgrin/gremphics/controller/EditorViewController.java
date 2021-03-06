package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import pl.yalgrin.gremphics.control.CanvasHolder;

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

    private WritableImage image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        canvasHolder = new CanvasHolder();

        AnchorPane.setBottomAnchor(canvasHolder, 0.0);
        AnchorPane.setTopAnchor(canvasHolder, 0.0);
        AnchorPane.setLeftAnchor(canvasHolder, 0.0);
        AnchorPane.setRightAnchor(canvasHolder, 0.0);

        centerPane.getChildren().add(canvasHolder);
    }

    public void setImage(WritableImage image) {
        this.image = image;

        canvasHolder.setImage(image);
        controllerData.setParameter(PARAM_IMAGE, image);
    }

    @Override
    public WritableImage getImage() {
        return image;
    }
}
