package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import pl.yalgrin.gremphics.processing.Histogram;
import pl.yalgrin.gremphics.processing.HistogramProcessor;

import java.net.URL;
import java.util.ResourceBundle;

public class HistogramController extends AbstractController {
    @FXML
    private AnchorPane rootPane;

    private BarChart<String, Number> histogramChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        histogramChart = new BarChart<String, Number>(xAxis, yAxis);
        histogramChart.setBarGap(0);
        histogramChart.setCategoryGap(0);
        AnchorPane.setLeftAnchor(histogramChart, 0.0);
        AnchorPane.setRightAnchor(histogramChart, 0.0);
        AnchorPane.setBottomAnchor(histogramChart, 0.0);
        AnchorPane.setTopAnchor(histogramChart, 0.0);
        rootPane.getChildren().addAll(histogramChart);
    }

    public void setImage(WritableImage image) {
        Histogram histogram = HistogramProcessor.getInstance().getHistogram(image);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Histogram");
        for (int i = 0; i < histogram.getHistogram().length; i++) {
            series1.getData().add(new XYChart.Data<>(Integer.toString(i), histogram.getHistogram()[i]));
        }

        histogramChart.getData().addAll(series1);
    }
}
