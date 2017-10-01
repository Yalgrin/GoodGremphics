package pl.yalgrin.gremphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GoodGremphicsApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(GoodGremphicsApplication.class.getClassLoader().getResource("layout/main_window_layout.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Good Gremphics");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}