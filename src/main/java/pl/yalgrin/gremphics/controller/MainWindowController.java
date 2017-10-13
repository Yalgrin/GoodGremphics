package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.yalgrin.gremphics.io.ImageIO;
import pl.yalgrin.gremphics.io.ImageSaveParam;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        try {
            loadContentPane("layout/shapes_view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadContentPane(String layourUrl) throws IOException {
        loadContentPane(layourUrl, this.controllerData, null);
    }

    public void loadContentPane(String layourUrl, ControllerData<Object> controllerData) throws IOException {
        loadContentPane(layourUrl, controllerData, null);
    }

    public void loadContentPane(String layourUrl, PreViewChange runnable) throws IOException {
        loadContentPane(layourUrl, this.controllerData, runnable);
    }

    public void loadContentPane(String layoutUrl, ControllerData<Object> controllerData, PreViewChange runnable) throws IOException {
        if (currentViewController != null) {
            currentViewController.onWindowClose();
            currentViewController = null;
        }

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
                Image image = ImageIO.readImage(file);
                loadContentPane("layout/editor_view.fxml", c -> {
                    EditorViewController controller = (EditorViewController) c;
                    controller.setImage(image);
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

    public void openWindow(String title, String layoutUrl) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindowController.class.getClassLoader().getResource(layoutUrl));
            loader.setResources(ResourceBundle.getBundle("language.LangBundle", Locale.ENGLISH));
            Parent root = loader.load();
            currentWindowController = loader.getController();
            currentWindowController.setMainWindowController(this);
            currentWindowController.initData(controllerData);
            currentWindowController.initializeAfterLoad();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
