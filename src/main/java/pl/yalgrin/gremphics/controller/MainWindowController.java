package pl.yalgrin.gremphics.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.yalgrin.gremphics.io.ImageIO;
import pl.yalgrin.gremphics.io.ImageSaveParam;
import pl.yalgrin.gremphics.processing.HistogramProcessor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindowController extends AbstractController {

    public static final String PARAM_FILE = "PARAM_FILE";

    @FXML
    private BorderPane contentPane;

    private AbstractController currentViewController;
    private AbstractController currentWindowController;

    private EditorViewController editorViewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setMainWindowController(this);
        try {
            loadContentPane("layout/shapes_view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContentPane(String layourUrl) throws IOException {
        loadContentPane(layourUrl, this.controllerData, null);
    }

    public void loadContentPane(String layourUrl, ControllerData<Object> controllerData) throws IOException {
        loadContentPane(layourUrl, controllerData, null);
    }

    private void loadContentPane(String layourUrl, PreViewChange runnable) throws IOException {
        loadContentPane(layourUrl, this.controllerData, runnable);
    }

    private void loadContentPane(String layoutUrl, ControllerData<Object> controllerData, PreViewChange runnable) throws IOException {
        if (currentViewController != null) {
            currentViewController.onWindowClose();
            currentViewController = null;
        }

        editorViewController = null;
        FXMLLoader loader = new FXMLLoader(MainWindowController.class.getClassLoader().getResource(layoutUrl));
        loader.setResources(ResourceBundle.getBundle("language.LangBundle", Locale.ENGLISH));
        Parent root = loader.load();
        currentViewController = loader.getController();
        currentViewController.setMainWindowController(this);
        currentViewController.initData(controllerData);
        if (runnable != null) {
            runnable.onViewChange(currentViewController);
        }
        contentPane.setCenter(root);
    }

    @Override
    public void onWindowClose() {
        currentViewController.onWindowClose();
        currentViewController = null;
    }

    @FXML
    public void loadFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open image file");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                WritableImage image = ImageIO.readImage(file);
                loadContentPane("layout/editor_view.fxml", c -> {
                    EditorViewController controller = (EditorViewController) c;
                    controller.setImage(image);
                    editorViewController = controller;
                });
            } catch (IOException ex) {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw, true);
                ex.printStackTrace(pw);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error during load occured!");
                alert.setContentText(ex.getMessage());
                alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.getBuffer().toString())));
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void saveFile() throws IOException {
        if (controllerData.getParameter(EditorViewController.PARAM_IMAGE) == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error during save occured!");
            alert.setContentText("No image loaded!");
            alert.showAndWait();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save image file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG image", "jpg", "jpeg"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            controllerData.setParameter(PARAM_FILE, file);
            openWindow("Select compression", "layout/jpeg_options_view.fxml");
        }
    }

    public void saveImage(Image image, File file, Map<ImageSaveParam, Object> params) {
        try {
            ImageIO.writeImage(file, image, params);
        } catch (IOException ex) {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            ex.printStackTrace(pw);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error during save occured!");
            alert.setContentText(ex.getMessage());
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.getBuffer().toString())));
            alert.showAndWait();
        }
    }

    private void openWindow(String title, String layoutUrl, PreViewChange runnable) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindowController.class.getClassLoader().getResource(layoutUrl));
            loader.setResources(ResourceBundle.getBundle("language.LangBundle", Locale.ENGLISH));
            Parent root = loader.load();
            currentWindowController = loader.getController();
            currentWindowController.setMainWindowController(this);
            currentWindowController.initData(controllerData);
            if (runnable != null) {
                runnable.onViewChange(currentWindowController);
            }
            currentWindowController.initializeAfterLoad();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openWindow(String title, String layoutUrl) {
        openWindow(title, layoutUrl, null);
    }

    public void exitApplication(ActionEvent actionEvent) {
    }

    public void goToShapeView(ActionEvent actionEvent) throws IOException {
        loadContentPane("layout/shapes_view.fxml", controllerData, null);
    }

    public void goToRgbCmykConv(ActionEvent actionEvent) {
        openWindow("RGB/CMYK Converter", "layout/rgb_cmyk_view.fxml");
    }

    public void goToRgbCube(ActionEvent actionEvent) throws IOException {
        openWindow("RGB Cube", "layout/rgb_cube_view.fxml");
    }

    public void goToHsvCone(ActionEvent actionEvent) {
        openWindow("HSV Cone", "layout/hsv_cone_view.fxml");
    }

    public void goToPointOperations(ActionEvent actionEvent) {
        if (controllerData.getParameter(EditorViewController.PARAM_IMAGE) == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error during processing occured!");
            alert.setContentText("No image loaded!");
            alert.showAndWait();
            return;
        }

        openWindow("Point operations", "layout/point_operation_view.fxml");
    }

    public EditorViewController getEditorViewController() {
        return editorViewController;
    }

    public void goToHistogram(ActionEvent actionEvent) {
        openWindow("Histogram", "layout/histogram_view.fxml", controller -> {
            HistogramController c = (HistogramController) controller;
            c.setImage(currentViewController.getImage());
        });
    }

    public void stretchHistogram(ActionEvent actionEvent) {
        updateUI(HistogramProcessor.getInstance().stretchImageHistogram(currentViewController.getImage()));
    }

    public void equalizeHistogram(ActionEvent actionEvent) {
        updateUI(HistogramProcessor.getInstance().equalizeHistogram(currentViewController.getImage()));
    }
}
