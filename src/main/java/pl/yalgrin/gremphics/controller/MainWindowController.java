package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.yalgrin.gremphics.io.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainWindowController extends AbstractController {

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
            BufferedImage image = ImageIO.readImage(file);
            loadContentPane("layout/editor_view.fxml", c -> {
                EditorViewController controller = (EditorViewController) c;
                controller.setImage(image);
            });
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
