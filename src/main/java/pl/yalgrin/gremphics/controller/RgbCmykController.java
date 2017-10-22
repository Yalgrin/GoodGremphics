package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class RgbCmykController extends AbstractController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Canvas colorCanvas;

    @FXML
    private Canvas colorPickerCanvas;

    @FXML
    private Canvas huePickerCanvas;

    private Spinner<Integer> redSpinner, greenSpinner, blueSpinner, blackSpinner, cyanSpinner, yellowSpinner, magentaSpinner;

    private boolean editingRGB = true, editingCMYK = false;
    private int lastNonZeroHue = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        redSpinner = getRGBSpinner();
        greenSpinner = getRGBSpinner();
        blueSpinner = getRGBSpinner();

        gridPane.add(redSpinner, 1, 1);
        gridPane.add(greenSpinner, 1, 2);
        gridPane.add(blueSpinner, 1, 3);

        blackSpinner = getCMYKSpinner();
        cyanSpinner = getCMYKSpinner();
        yellowSpinner = getCMYKSpinner();
        magentaSpinner = getCMYKSpinner();

        gridPane.add(blackSpinner, 3, 1);
        gridPane.add(cyanSpinner, 3, 2);
        gridPane.add(magentaSpinner, 3, 3);
        gridPane.add(yellowSpinner, 3, 4);

        redSpinner.getValueFactory().setValue(192);
        greenSpinner.getValueFactory().setValue(64);
        blueSpinner.getValueFactory().setValue(64);
        lastNonZeroHue = getCurrentHue();

        setupListenersForColorPicker();
        recalculateCMYK();
        drawColor();
        drawHueBar();
    }

    private void setupListenersForColorPicker() {
        huePickerCanvas.setOnMousePressed(event -> {
            int selectedHue = (int) (event.getY() / huePickerCanvas.getHeight() * 360);
            lastNonZeroHue = selectedHue;
            editingRGB = true;
            Color newColor = Color.hsb(selectedHue, getCurrentSaturation(), getCurrentValue());
            setRGBValue(redSpinner, newColor.getRed());
            setRGBValue(greenSpinner, newColor.getGreen());
            setRGBValue(blueSpinner, newColor.getBlue());
        });

        colorPickerCanvas.setOnMousePressed(event -> {
            double selectedSaturation = ((colorPickerCanvas.getWidth() - event.getX()) / colorPickerCanvas.getWidth());
            double selectedValue = ((colorPickerCanvas.getHeight() - event.getY()) / colorPickerCanvas.getHeight());
            editingRGB = true;
            Color newColor = Color.hsb(lastNonZeroHue, selectedSaturation, selectedValue);
            setRGBValue(redSpinner, newColor.getRed());
            setRGBValue(greenSpinner, newColor.getGreen());
            setRGBValue(blueSpinner, newColor.getBlue());
        });
    }

    private void drawColor() {
        GraphicsContext graphicsContext = colorCanvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.rgb(redSpinner.getValue(), greenSpinner.getValue(), blueSpinner.getValue()));
        graphicsContext.fillRect(0, 0, colorCanvas.getWidth(), colorCanvas.getHeight());

        drawColorPickerCanvas();
    }

    private void drawHueBar() {
        GraphicsContext graphicsContext = huePickerCanvas.getGraphicsContext2D();
        for (int y = 0; y < huePickerCanvas.getHeight(); y++) {
            graphicsContext.setFill(Color.hsb(y * 360 / 256, 1, 1));
            graphicsContext.fillRect(0, y, huePickerCanvas.getWidth(), 1);
        }
    }

    private void drawColorPickerCanvas() {
        GraphicsContext graphicsContext = colorPickerCanvas.getGraphicsContext2D();
        PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        for (int x = 0; x < colorPickerCanvas.getWidth(); x++) {
            for (int y = 0; y < colorPickerCanvas.getHeight(); y++) {
                pixelWriter.setColor(x, y, Color.hsb(lastNonZeroHue, (colorPickerCanvas.getWidth() - x) / colorPickerCanvas.getWidth(),
                        (colorPickerCanvas.getHeight() - y) / colorPickerCanvas.getHeight()));
            }
        }
    }

    private void recalculateRGB() {
        setRGBValue(redSpinner, 1 - Math.min(1, getCMYKValue(cyanSpinner) * (1 - getCMYKValue(blackSpinner)) + getCMYKValue(blackSpinner)));
        setRGBValue(greenSpinner, 1 - Math.min(1, getCMYKValue(magentaSpinner) * (1 - getCMYKValue(blackSpinner)) + getCMYKValue(blackSpinner)));
        setRGBValue(blueSpinner, 1 - Math.min(1, getCMYKValue(yellowSpinner) * (1 - getCMYKValue(blackSpinner)) + getCMYKValue(blackSpinner)));
    }

    private void recalculateCMYK() {
        setCMYKValue(blackSpinner, Math.min(1 - getRGBValue(redSpinner), Math.min(1 - getRGBValue(greenSpinner), 1 - getRGBValue(blueSpinner))));
        setCMYKValue(cyanSpinner, (1 - getRGBValue(redSpinner) - getCMYKValue(blackSpinner)) / (1 - getCMYKValue(blackSpinner)));
        setCMYKValue(magentaSpinner, (1 - getRGBValue(greenSpinner) - getCMYKValue(blackSpinner)) / (1 - getCMYKValue(blackSpinner)));
        setCMYKValue(yellowSpinner, (1 - getRGBValue(blueSpinner) - getCMYKValue(blackSpinner)) / (1 - getCMYKValue(blackSpinner)));
    }

    private Spinner<Integer> getRGBSpinner() {
        Spinner<Integer> spinner = new Spinner<>(0, 255, 128, 1);
        spinner.setEditable(true);
        spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                editingRGB = true;
            }
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!editingRGB) {
                return;
            }
            recalculateCMYK();
            drawColor();
        });
        return spinner;
    }

    private Spinner<Integer> getCMYKSpinner() {
        Spinner<Integer> spinner = new Spinner<>(0, 100, 50, 1);
        spinner.setEditable(true);
        spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                editingRGB = false;
            }
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (editingRGB) {
                return;
            }
            recalculateRGB();
            drawColor();
        });
        return spinner;
    }

    private double getRGBValue(Spinner<Integer> spinner) {
        return spinner.getValue() / 255.0;
    }

    private double getCMYKValue(Spinner<Integer> spinner) {
        return spinner.getValue() / 100.0;
    }

    private void setRGBValue(Spinner<Integer> spinner, double value) {
        spinner.getValueFactory().setValue((int) Math.round(255 * value));
    }

    private void setCMYKValue(Spinner<Integer> spinner, double value) {
        spinner.getValueFactory().setValue((int) Math.round(100 * value));
    }

    //http://www.rapidtables.com/convert/color/rgb-to-hsv.htm
    private int getCurrentHue() {
        double cMax = Math.max(Math.max(getRGBValue(redSpinner), getRGBValue(greenSpinner)), getRGBValue(blueSpinner));
        double cMin = Math.min(Math.min(getRGBValue(redSpinner), getRGBValue(greenSpinner)), getRGBValue(blueSpinner));
        double delta = cMax - cMin;
        if (delta == 0) {
            return 0;
        } else if (cMax == getRGBValue(redSpinner)) {
            return (int) Math.round(60.0 * (((getRGBValue(greenSpinner) - getRGBValue(blueSpinner)) / delta) % 6));
        } else if (cMax == getRGBValue(greenSpinner)) {
            return (int) Math.round(60.0 * (((getRGBValue(blueSpinner) - getRGBValue(redSpinner)) / delta) + 2));
        } else {
            return (int) Math.round(60.0 * (((getRGBValue(redSpinner) - getRGBValue(greenSpinner)) / delta) + 4));
        }
    }

    private double getCurrentSaturation() {
        double cMax = Math.max(Math.max(getRGBValue(redSpinner), getRGBValue(greenSpinner)), getRGBValue(blueSpinner));
        double cMin = Math.min(Math.min(getRGBValue(redSpinner), getRGBValue(greenSpinner)), getRGBValue(blueSpinner));
        double delta = cMax - cMin;
        if (cMax == 0) {
            return 0;
        } else {
            return delta / cMax;
        }
    }

    private double getCurrentValue() {
        return Math.max(Math.max(getRGBValue(redSpinner), getRGBValue(greenSpinner)), getRGBValue(blueSpinner));
    }
}
