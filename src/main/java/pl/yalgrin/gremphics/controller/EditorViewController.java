package pl.yalgrin.gremphics.controller;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Shape;
import pl.yalgrin.gremphics.shape.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EditorViewController extends AbstractController {

    @FXML
    private AnchorPane centerPane;

    @FXML
    private FlowPane buttonPane;

    @FXML
    private FlowPane propertyPane;

    @FXML
    private Button selectButton;

    private Button lastSelectedButton;
    private Shape selectedShape;
    private Shapes selectedShapeType;
    private CanvasHolder canvasHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        lastSelectedButton = selectButton;
        selectButton.setDisable(true);
        selectButton.setOnAction(e -> {
            selectedShapeType = null;
            lastSelectedButton.setDisable(false);
            lastSelectedButton = selectButton;
            selectButton.setDisable(true);
        });

        canvasHolder = new CanvasHolder();
        canvasHolder.setOnMouseClicked(e -> {
            if (selectedShapeType == null) {
                unbindProperties(selectedShape);
                selectedShape = canvasHolder.getShapeAt((int) e.getX(), (int) e.getY());
                if (selectedShape != null) {
                    setupProperties((IShape) selectedShape);
                }
                return;
            }

            Shape shape = null;
            switch (selectedShapeType) {
                case LINE:
                    Line line = new Line();
                    line.setStartX(e.getX() - 25);
                    line.setStartY(e.getY() - 25);
                    line.setEndX(e.getX() + 25);
                    line.setEndY(e.getY() + 25);
                    shape = line;
                    break;
                case OVAL:
                    Circle circle = new Circle();
                    circle.centerXProperty().set((int) e.getX());
                    circle.centerYProperty().set((int) e.getY());
                    circle.radiusProperty().set(25);
                    shape = circle;
                    break;
                case RECTANGLE:
                    Rectangle rect = new Rectangle();
                    rect.startXProperty().set((int) e.getX() - 25);
                    rect.startYProperty().set((int) e.getY() - 25);
                    rect.endXProperty().set((int) e.getX() + 25);
                    rect.endYProperty().set((int) e.getY() + 25);
                    shape = rect;
                    break;
            }

            if (shape != null) {
                canvasHolder.addShape(shape);
            }
        });
        centerPane.getChildren().add(canvasHolder);

        for (Shapes shapes : Shapes.values()) {
            Button button = new Button(shapes.toString());
            button.setOnAction(e -> {
                selectedShapeType = shapes;
                lastSelectedButton.setDisable(false);
                lastSelectedButton = button;
                button.setDisable(true);
            });
            buttonPane.getChildren().add(button);
        }
    }

    private void unbindProperties(Shape shape) {
        if (shape == null) {
            return;
        }

        propertyPane.getChildren().clear();
    }

    private void setupProperties(IShape shape) {
        System.out.println(shape.getBoundProperties());
        for (NamedProperty namedProperty : shape.getBoundProperties()) {
            HBox hBox = new HBox();
            Label label = new Label(namedProperty.getName());
            Spinner<Integer> spinner = new Spinner<>(0, 1000, 1);
            spinner.setEditable(true);
            IntegerProperty.integerProperty(spinner.getValueFactory().valueProperty()).bindBidirectional(namedProperty.propertyProperty());
            hBox.getChildren().add(label);
            hBox.getChildren().add(spinner);
            propertyPane.getChildren().add(hBox);
        }
        Button button = new Button("Delete");
        button.setOnAction(e -> {
            canvasHolder.removeShape((Shape) shape);
            unbindProperties((Shape) shape);
        });
        propertyPane.getChildren().add(button);
    }
}
