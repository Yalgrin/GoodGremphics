package pl.yalgrin.gremphics.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl.yalgrin.gremphics.processing.ColorProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class PointOperationsController extends AbstractController {

    @FXML
    public GridPane parameterGridPane;

    @FXML
    public Button saveButton;

    @FXML
    public Button cancelButton;

    @FXML
    private RadioButton addRadio;

    @FXML
    private RadioButton subtractRadio;

    @FXML
    private RadioButton multiplyRadio;

    @FXML
    private RadioButton divideRadio;

    @FXML
    private RadioButton brightenRadio;

    @FXML
    private RadioButton darkenRadio;

    @FXML
    private RadioButton greyscaleRadio;

    @FXML
    private ToggleGroup taskGroup;

    @FXML
    private Label redLabel;

    @FXML
    private Label greenLabel;

    @FXML
    private Label blueLabel;

    private Spinner<Integer> redSpinner, greenSpinner, blueSpinner;
    private Spinner<Double> redDoubleSpinner, greenDoubleSpinner, blueDoubleSpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        addRadio.setUserData(ColorProcessor.PointOperation.ADD);
        subtractRadio.setUserData(ColorProcessor.PointOperation.SUBTRACT);
        multiplyRadio.setUserData(ColorProcessor.PointOperation.MULTIPLY);
        divideRadio.setUserData(ColorProcessor.PointOperation.DIVIDE);
        brightenRadio.setUserData(ColorProcessor.PointOperation.BRIGHTEN);
        darkenRadio.setUserData(ColorProcessor.PointOperation.DARKEN);
        greyscaleRadio.setUserData(ColorProcessor.PointOperation.GREYSCALE);

        redSpinner = getSpinner();
        greenSpinner = getSpinner();
        blueSpinner = getSpinner();

        redDoubleSpinner = getDoubleSpinner();
        greenDoubleSpinner = getDoubleSpinner();
        blueDoubleSpinner = getDoubleSpinner();

        taskGroup.selectToggle(addRadio);
        updateSpinners((ColorProcessor.PointOperation) addRadio.getUserData());

        taskGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> updateSpinners((ColorProcessor.PointOperation) newValue.getUserData()));

    }

    private void updateSpinners(ColorProcessor.PointOperation operation) {
        switch (operation) {
            case ADD:
            case SUBTRACT:
                redLabel.setText("Red");
                redLabel.setVisible(true);
                greenLabel.setVisible(true);
                blueLabel.setVisible(true);
                redSpinner.setVisible(true);
                greenSpinner.setVisible(true);
                blueSpinner.setVisible(true);
                redDoubleSpinner.setVisible(false);
                greenDoubleSpinner.setVisible(false);
                blueDoubleSpinner.setVisible(false);
                addIntSpinners();
                break;
            case MULTIPLY:
            case DIVIDE:
                redLabel.setText("Red");
                redLabel.setVisible(true);
                greenLabel.setVisible(true);
                blueLabel.setVisible(true);
                redSpinner.setVisible(false);
                greenSpinner.setVisible(false);
                blueSpinner.setVisible(false);
                redDoubleSpinner.setVisible(true);
                greenDoubleSpinner.setVisible(true);
                blueDoubleSpinner.setVisible(true);
                addDoubleSpinners();
                break;
            case BRIGHTEN:
            case DARKEN:
                redLabel.setText("Value");
                redLabel.setVisible(true);
                greenLabel.setVisible(false);
                blueLabel.setVisible(false);
                redSpinner.setVisible(true);
                greenSpinner.setVisible(false);
                blueSpinner.setVisible(false);
                redDoubleSpinner.setVisible(false);
                greenDoubleSpinner.setVisible(false);
                blueDoubleSpinner.setVisible(false);
                addIntSpinners();
                break;
            case GREYSCALE:
                redLabel.setVisible(false);
                greenLabel.setVisible(false);
                blueLabel.setVisible(false);
                redSpinner.setVisible(false);
                greenSpinner.setVisible(false);
                blueSpinner.setVisible(false);
                redDoubleSpinner.setVisible(false);
                greenDoubleSpinner.setVisible(false);
                blueDoubleSpinner.setVisible(false);
                break;

        }
    }

    private void addIntSpinners() {
        parameterGridPane.getChildren().removeAll(redSpinner, greenSpinner, blueSpinner, redDoubleSpinner, greenDoubleSpinner, blueDoubleSpinner);
        parameterGridPane.add(redSpinner, 1, 0);
        parameterGridPane.add(greenSpinner, 1, 1);
        parameterGridPane.add(blueSpinner, 1, 2);
    }

    private void addDoubleSpinners() {
        parameterGridPane.getChildren().removeAll(redSpinner, greenSpinner, blueSpinner, redDoubleSpinner, greenDoubleSpinner, blueDoubleSpinner);
        parameterGridPane.add(redDoubleSpinner, 1, 0);
        parameterGridPane.add(greenDoubleSpinner, 1, 1);
        parameterGridPane.add(blueDoubleSpinner, 1, 2);
    }

    private Spinner<Integer> getSpinner() {
        Spinner<Integer> spinner = new Spinner<>(0, 255, 0, 1);
        spinner.setEditable(true);
        return spinner;
    }

    private Spinner<Double> getDoubleSpinner() {
        Spinner<Double> spinner = new Spinner<>(0.0, 99999.0, 1, 0.1);
        spinner.setEditable(true);
        return spinner;
    }

    @FXML
    public void saveButtonPressed(ActionEvent actionEvent) {
        try {
            if (controllerData.getParameter(EditorViewController.PARAM_IMAGE) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error during processing occured!");
                alert.setContentText("No image loaded!");
                alert.showAndWait();
                return;
            }

            WritableImage image = (WritableImage) controllerData.getParameter(EditorViewController.PARAM_IMAGE);
            ColorProcessor.PointOperation pointOperation = (ColorProcessor.PointOperation) taskGroup.getSelectedToggle().getUserData();
            switch (pointOperation) {
                case ADD:
                case SUBTRACT:
                    image = ColorProcessor.getInstance().pointOperation(image, pointOperation, redSpinner.getValue(), greenSpinner.getValue(), blueSpinner.getValue());
                    break;
                case MULTIPLY:
                case DIVIDE:
                    image = ColorProcessor.getInstance().pointOperation(image, pointOperation, redDoubleSpinner.getValue(), greenDoubleSpinner.getValue(), blueDoubleSpinner.getValue());
                    break;
                case BRIGHTEN:
                    image = ColorProcessor.getInstance().brightenImage(image, redSpinner.getValue());
                    break;
                case DARKEN:
                    image = ColorProcessor.getInstance().brightenImage(image, -redSpinner.getValue());
                    break;
                case GREYSCALE:
                    image = ColorProcessor.getInstance().turnToGreyscale(image);
                    break;
            }
            controllerData.setParameter(EditorViewController.PARAM_IMAGE, image);
            updateUI(image);

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error during processing occured!");
            alert.setContentText(e.getMessage());
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.getBuffer().toString())));
            alert.showAndWait();
        }
    }

    @FXML
    public void cancelButtonPressed(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
