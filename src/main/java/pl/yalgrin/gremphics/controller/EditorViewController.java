package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorViewController extends AbstractController {

    @FXML
    private AnchorPane centerPane;

    @FXML
    private FlowPane buttonPane;

    @FXML
    private FlowPane propertyPane;

    @FXML
    private Button selectButton;

    private CanvasHolder canvasHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        canvasHolder = new CanvasHolder();
        centerPane.getChildren().add(canvasHolder);
    }

    public void setImage(BufferedImage image) {
        canvasHolder.setImage(image);
    }
}
