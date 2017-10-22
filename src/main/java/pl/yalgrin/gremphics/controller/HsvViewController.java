package pl.yalgrin.gremphics.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import pl.yalgrin.gremphics.io.ImageIO;
import pl.yalgrin.gremphics.io.ImageSaveParam;
import pl.yalgrin.gremphics.threedee.ShapeCone;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HsvViewController extends AbstractController {

    @FXML
    private AnchorPane anchorPane;

    private WritableImage texture;

    private double lastX, lastY;
    private Rotate rotateX, rotateY;

    private double anchorX, anchorY, anchorXAngle, anchorYAngle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(generateTexture());

        final MeshView rect = new MeshView(
                new ShapeCone(256)
        );
        rect.setMaterial(material);
        rect.setDrawMode(DrawMode.FILL);
        rect.setTranslateX(250);
        rect.setTranslateY(250);
        rect.setCullFace(CullFace.NONE);

        rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateY = new Rotate(0, Rotate.Y_AXIS);
        rect.getTransforms().addAll(rotateX, rotateY);

        final Group root = new Group(rect);
        root.getChildren().add(new AmbientLight());
        final SubScene scene = new SubScene(root, 500, 500, true, SceneAntialiasing.BALANCED);

        anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                anchorXAngle = rotateX.getAngle();
                anchorYAngle = rotateY.getAngle();
            }
        });

        anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                rotateX.setAngle(anchorXAngle - (anchorY - event.getSceneY()));
                rotateY.setAngle(anchorYAngle + anchorX - event.getSceneX());
            }
        });


        addCamera(scene);

        anchorPane.getChildren().add(scene);
    }

    private PerspectiveCamera addCamera(SubScene scene) {
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
        scene.setCamera(perspectiveCamera);
        return perspectiveCamera;
    }

    private WritableImage generateTexture() {
        WritableImage texture = new WritableImage(360, 720);
        PixelWriter pixelWriter = texture.getPixelWriter();
        for (int x = 0; x < 360; x++) {
            for (int y = 0; y < 360; y++) {
                pixelWriter.setColor(x, y, Color.hsb(x, 1.0, y / 360.0));
            }
        }
        for (int x = 0; x < 360; x++) {
            for (int y = 360; y < 720; y++) {
                pixelWriter.setColor(x, y, Color.hsb(x, (720 - y) / 360.0, 1.0));
            }
        }

        try {
            HashMap<ImageSaveParam, Object> params = new HashMap<>();
            params.put(ImageSaveParam.JPEG_COMPRESSION, 1f);
            ImageIO.writeImage(new File("D:/tesciki2.jpg"), texture, params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return texture;
    }
}
