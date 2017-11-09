package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import pl.yalgrin.gremphics.control.BezierCanvas;

import java.net.URL;
import java.util.ResourceBundle;

public class BezierViewController extends AbstractController {
    @FXML
    public AnchorPane centerPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        centerPane.getChildren().add(new BezierCanvas());
    }
}
