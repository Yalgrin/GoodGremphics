package pl.yalgrin.gremphics.controller;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    public Button newShapeButton;
    public Button deleteShapeButton;
    public Button addPointButton;
    public Button selectionModeButton;
    public Button translateModeButton;
    public Button rotateModeButton;
    public Button scaleModeButton;
    public Button operationDialogButton;

    private PolygonCanvas canvas;

    private Map<Spinner<Integer>, IntegerProperty> spinnerIntegerPropertyMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        canvas = new PolygonCanvas();
        centerPane.getChildren().add(canvas);

        updateButtonVisibility(canvas.getMode());

        canvas.modePropertyProperty().addListener((observable, oldValue, newValue) -> updateButtonVisibility(newValue));
        canvas.selectedShapeProperty().addListener((observable, oldValue, newValue) -> updateButtonVisibility(canvas.getMode()));
    }

    private void updateButtonVisibility(PolygonCanvas.Mode mode) {
        if (canvas.getSelectedShape() != null) {
            newShapeButton.setDisable(false);
            deleteShapeButton.setDisable(false);
            operationDialogButton.setDisable(false);

            switch (mode) {
                case SELECT_MODE:
                    addPointButton.setDisable(false);
                    selectionModeButton.setDisable(true);
                    translateModeButton.setDisable(false);
                    rotateModeButton.setDisable(false);
                    scaleModeButton.setDisable(false);
                    break;
                case ADD_POINT_MODE:
                    addPointButton.setDisable(true);
                    selectionModeButton.setDisable(false);
                    translateModeButton.setDisable(false);
                    rotateModeButton.setDisable(false);
                    scaleModeButton.setDisable(false);
                    break;
                case SCALE_MODE:
                    addPointButton.setDisable(false);
                    selectionModeButton.setDisable(false);
                    translateModeButton.setDisable(false);
                    rotateModeButton.setDisable(false);
                    scaleModeButton.setDisable(true);
                    break;
                case ROTATE_MODE:
                    addPointButton.setDisable(false);
                    selectionModeButton.setDisable(false);
                    translateModeButton.setDisable(false);
                    rotateModeButton.setDisable(true);
                    scaleModeButton.setDisable(false);
                    break;
                case TRANSLATE_MODE:
                    addPointButton.setDisable(false);
                    selectionModeButton.setDisable(false);
                    translateModeButton.setDisable(true);
                    rotateModeButton.setDisable(false);
                    scaleModeButton.setDisable(false);
                    break;
            }
        } else {
            newShapeButton.setDisable(true);
            deleteShapeButton.setDisable(true);
            addPointButton.setDisable(canvas.getMode().equals(PolygonCanvas.Mode.ADD_POINT_MODE));
            selectionModeButton.setDisable(canvas.getShapes().isEmpty() || canvas.getMode().equals(PolygonCanvas.Mode.SELECT_MODE));
            translateModeButton.setDisable(true);
            rotateModeButton.setDisable(true);
            scaleModeButton.setDisable(true);
            operationDialogButton.setDisable(true);
        }
    }

    public void newShapePressed(ActionEvent actionEvent) {
        canvas.setSelectedShape(null);
    }

    public void deleteShapePressed(ActionEvent actionEvent) {
        canvas.deleteShape(canvas.getSelectedShape());
    }

    public void addPointPressed(ActionEvent actionEvent) {
        canvas.setMode(PolygonCanvas.Mode.ADD_POINT_MODE);
    }

    public void selectModePressed(ActionEvent actionEvent) {
        canvas.setMode(PolygonCanvas.Mode.SELECT_MODE);
    }

    public void translateModePressed(ActionEvent actionEvent) {
        canvas.setMode(PolygonCanvas.Mode.TRANSLATE_MODE);
    }

    public void rotateModePrressed(ActionEvent actionEvent) {
        canvas.setMode(PolygonCanvas.Mode.ROTATE_MODE);
    }

    public void scaleModePressed(ActionEvent actionEvent) {
        canvas.setMode(PolygonCanvas.Mode.SCALE_MODE);
    }

    public void operationDialogPressed(ActionEvent actionEvent) {
    }
}
