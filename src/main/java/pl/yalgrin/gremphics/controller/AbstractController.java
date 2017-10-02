package pl.yalgrin.gremphics.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController implements Initializable {

    private MainWindowController mainWindowController;

    protected ResourceBundle resourceBundle;
    protected ControllerData<Object> controllerData = new ControllerData<>();

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
    }

    public final void initData(ControllerData<Object> controllerData) {
        this.controllerData = controllerData;
    }

    public void initializeAfterLoad() {

    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public void onWindowClose() {

    }
}
