package pl.yalgrin.gremphics.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import pl.yalgrin.gremphics.shape.InterestPoint;
import pl.yalgrin.gremphics.shape.Shape;
import pl.yalgrin.gremphics.shape.Shapes;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorViewController extends AbstractController {

    @FXML
    private AnchorPane centerPane;

    @FXML
    private FlowPane buttonPane;

    private Shapes currentShape;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        for (Shapes shapes : Shapes.values()) {
            Button button = new Button(shapes.toString());
            button.setOnAction(e -> {
                currentShape = shapes;
            });
            FlowPane.setMargin(button, new Insets(5));
            buttonPane.getChildren().add(button);
        }

        Canvas canvas = new Canvas(500, 500);
        canvas.setOnMouseClicked(e -> {
            if (currentShape == null) {
                return;
            }

            Shape shape = currentShape.instantiate();
            shape.setPivotPoint(new Point((int) e.getX(), (int) e.getY()));
            shape.draw(canvas.getGraphicsContext2D());

            Point pivotPoint = shape.getPivotPoint();
            GraphicsContext context = canvas.getGraphicsContext2D();
            context.setFill(Color.BLACK);
            context.fillRect(pivotPoint.getX() - 3, pivotPoint.getY() - 3, 7, 7);
            context.setFill(Color.GREEN);
            context.fillRect(pivotPoint.getX() - 2, pivotPoint.getY() - 2, 5, 5);

            for (InterestPoint interestPoint : shape.getResizePoints()) {
                context.setFill(Color.BLACK);
                context.fillRect(interestPoint.getPoint().getX() - 3, interestPoint.getPoint().getY() - 3, 7, 7);
                context.setFill(Color.WHITE);
                context.fillRect(interestPoint.getPoint().getX() - 2, interestPoint.getPoint().getY() - 2, 5, 5);
            }
        });
        centerPane.getChildren().add(canvas);
    }
}
