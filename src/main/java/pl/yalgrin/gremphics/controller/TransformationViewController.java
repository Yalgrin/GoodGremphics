package pl.yalgrin.gremphics.controller;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pl.yalgrin.gremphics.control.PolygonCanvas;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    public Button translateDialogButton;
    public Button rotateDialogButton;
    public Button scaleDialogPressed;

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

    public void updateButtonVisibility(PolygonCanvas.Mode mode) {
        if (canvas.getSelectedShape() != null) {
            newShapeButton.setDisable(false);
            deleteShapeButton.setDisable(false);
            translateDialogButton.setDisable(false);
            rotateDialogButton.setDisable(false);
            scaleDialogPressed.setDisable(false);

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
            translateDialogButton.setDisable(true);
            rotateDialogButton.setDisable(true);
            scaleDialogPressed.setDisable(true);

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

    public void translateDialogPressed(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setTitle("Translation");
        dialog.setHeaderText("Specify vector");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Label labelX = new Label("X");
        Label labelY = new Label("Y");
        Spinner<Integer> spinnerX = new Spinner<>(0, 1000, 0, 1);
        spinnerX.setEditable(true);
        Spinner<Integer> spinnerY = new Spinner<>(0, 1000, 0, 1);
        spinnerY.setEditable(true);

        grid.add(labelX, 0, 0);
        grid.add(labelY, 0, 1);
        grid.add(spinnerX, 1, 0);
        grid.add(spinnerY, 1, 1);

        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(b -> {
            if (b == ButtonType.OK) {
                canvas.getSelectedShape().translate(spinnerX.getValue(), spinnerY.getValue());
                canvas.draw();
            }
        });
    }

    public void rotateDialogPressed(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setTitle("Translation");
        dialog.setHeaderText("Specify vector");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Label labelX = new Label("X");
        Label labelY = new Label("Y");
        Spinner<Integer> spinnerX = new Spinner<>(0, 1000, 0, 1);
        spinnerX.setEditable(true);
        Spinner<Integer> spinnerY = new Spinner<>(0, 1000, 0, 1);
        spinnerY.setEditable(true);

        Label labelAngle = new Label("Angle");
        Spinner<Integer> spinnerAngle = new Spinner<>(0, 359, 0, 1);
        spinnerAngle.setEditable(true);

        grid.add(labelX, 0, 0);
        grid.add(labelY, 0, 1);
        grid.add(spinnerX, 1, 0);
        grid.add(spinnerY, 1, 1);

        grid.add(labelAngle, 0, 2);
        grid.add(spinnerAngle, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(b -> {
            if (b == ButtonType.OK) {
                canvas.getSelectedShape().rotate(spinnerX.getValue(), spinnerY.getValue(), spinnerAngle.getValue() * Math.PI / 180);
                canvas.draw();
            }
        });
    }

    public void scaleDialogPressed(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setTitle("Translation");
        dialog.setHeaderText("Specify vector");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Label labelX = new Label("X");
        Label labelY = new Label("Y");
        Spinner<Integer> spinnerX = new Spinner<>(0, 1000, 0, 1);
        spinnerX.setEditable(true);
        Spinner<Integer> spinnerY = new Spinner<>(0, 1000, 0, 1);
        spinnerY.setEditable(true);

        Label labelScale = new Label("Scale");
        Spinner<Double> spinnerScale = new Spinner<>(0.1, 100.0, 1.0, 0.1);
        spinnerScale.setEditable(true);

        grid.add(labelX, 0, 0);
        grid.add(labelY, 0, 1);
        grid.add(spinnerX, 1, 0);
        grid.add(spinnerY, 1, 1);

        grid.add(labelScale, 0, 2);
        grid.add(spinnerScale, 1, 2);

        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(b -> {
            if (b == ButtonType.OK) {
                canvas.getSelectedShape().scale(spinnerX.getValue(), spinnerY.getValue(), spinnerScale.getValue(), spinnerScale.getValue());
                canvas.draw();
            }
        });
    }

    public PolygonCanvas getCanvas() {
        return canvas;
    }
}
