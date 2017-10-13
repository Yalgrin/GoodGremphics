package pl.yalgrin.gremphics.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl.yalgrin.gremphics.io.ImageSaveParam;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class JpegOptionsController extends AbstractController {

    @FXML
    public GridPane parameterGridPane;

    @FXML
    public Button saveButton;

    @FXML
    public Button cancelButton;

    private Spinner<Double> spinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        spinner = new Spinner<>(0.0, 1.0, 0.9, 0.05);
        parameterGridPane.add(spinner, 1, 0);
    }

    @FXML
    public void saveButtonPressed(ActionEvent actionEvent) {
        Map<ImageSaveParam, Object> params = new HashMap<>();
        params.put(ImageSaveParam.JPEG_COMPRESSION, spinner.getValue().floatValue());
        getMainWindowController().saveImage((Image) controllerData.getParameter(EditorViewController.PARAM_IMAGE),
                (File) controllerData.getParameter(MainWindowController.PARAM_FILE), params);
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelButtonPressed(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
