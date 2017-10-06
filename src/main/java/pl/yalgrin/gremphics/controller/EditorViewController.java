package pl.yalgrin.gremphics.controller;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseButton;
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

    private Double dragStartX, dragStartY;
    private long dragStartTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        lastSelectedButton = selectButton;
        selectButton.setDisable(true);
        selectButton.setOnAction(e -> {
            selectedShapeType = null;
            canvasHolder.setDraggingEnabled(true);
            lastSelectedButton.setDisable(false);
            lastSelectedButton = selectButton;
            selectButton.setDisable(true);
        });

        canvasHolder = new CanvasHolder();
        canvasHolder.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY || dragStartX == null || dragStartY == null || selectedShapeType == null) {
                return;
            }

            Shape shape = null;
            switch (selectedShapeType) {
                case LINE:
                    Line line = new Line();
                    line.setEndX(e.getX());
                    line.setEndY(e.getY());
                    line.setStartX(dragStartX);
                    line.setStartY(dragStartY);
                    shape = line;
                    break;
                case OVAL:
                    Circle circle = new Circle();
                    circle.centerXProperty().set(dragStartX);
                    circle.centerYProperty().set(dragStartY);
                    circle.radiusProperty().set(Math.sqrt(Math.pow(e.getX() - dragStartX, 2) + Math.pow(e.getY() - dragStartY, 2)));
                    shape = circle;
                    break;
                case RECTANGLE:
                    Rectangle rect = new Rectangle();
                    rect.endXProperty().set((int) e.getX());
                    rect.endYProperty().set((int) e.getY());
                    rect.startXProperty().set(dragStartX.intValue());
                    rect.startYProperty().set(dragStartY.intValue());
                    System.out.println("START: " + dragStartX.intValue() + ", " + dragStartY.intValue());
                    System.out.println("START: " + e.getX() + ", " + e.getY());
                    shape = rect;
                    break;
            }

            if (shape != null) {
                canvasHolder.startDrag(shape);
            }

            System.out.println(e.getX() + ", " + e.getY());
        });
        canvasHolder.setOnMousePressed(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            System.out.println("PRESS:" + e.getX() + ", " + e.getY());
            dragStartX = e.getX();
            dragStartY = e.getY();
            dragStartTime = System.currentTimeMillis();
        });
        canvasHolder.setOnMouseReleased(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            System.out.println("RELEASE:" + e.getX() + ", " + e.getY());
            if (System.currentTimeMillis() - dragStartTime < 100 &&
                    Math.abs(e.getX() - dragStartX) < 5 && Math.abs(e.getY() - dragStartY) < 5) {
                canvasHolder.cancelDrag();

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
                        line.setEndX(e.getX() + 25);
                        line.setEndY(e.getY() + 25);
                        line.setStartX(e.getX() - 25);
                        line.setStartY(e.getY() - 25);
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
                        rect.endXProperty().set((int) e.getX() + 25);
                        rect.endYProperty().set((int) e.getY() + 25);
                        rect.startXProperty().set((int) e.getX() - 25);
                        rect.startYProperty().set((int) e.getY() - 25);
                        shape = rect;
                        break;
                }

                if (shape != null) {
                    canvasHolder.addShape(shape);
                }
            } else {
                canvasHolder.endDrag();
            }
            dragStartX = dragStartY = null;
        });
        centerPane.getChildren().add(canvasHolder);

        for (Shapes shapes : Shapes.values()) {
            Button button = new Button(shapes.toString());
            button.setOnAction(e -> {
                selectedShapeType = shapes;
                canvasHolder.setDraggingEnabled(false);
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
