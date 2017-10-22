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
import pl.yalgrin.gremphics.threedee.ShapeCube;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class RgbViewController extends AbstractController {

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
                new ShapeCube(256)
        );
        rect.setMaterial(material);
        rect.setDrawMode(DrawMode.FILL);
        rect.setTranslateX(250);
        rect.setTranslateY(250);
        rect.setCullFace(CullFace.BACK);

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
        WritableImage texture = new WritableImage(1024, 1024);
        PixelWriter pixelWriter = texture.getPixelWriter();
        for (int x = 256; x < 512; x++) {
            for (int y = 256; y < 512; y++) {
                pixelWriter.setColor(x, y, Color.rgb(511 - y, x - 256, 0));
            }
        }
        for (int x = 0; x < 256; x++) {
            for (int y = 256; y < 512; y++) {
                pixelWriter.setColor(x, y, Color.rgb(511 - y, 0, 255 - x));
            }
        }
        for (int x = 256; x < 512; x++) {
            for (int y = 0; y < 256; y++) {
                pixelWriter.setColor(x, y, Color.rgb(255, x - 256, 255 - y));
            }
        }
        for (int x = 256; x < 512; x++) {
            for (int y = 512; y < 768; y++) {
                pixelWriter.setColor(x, y, Color.rgb(0, x - 256, y - 512));
            }
        }
        for (int x = 512; x < 768; x++) {
            for (int y = 256; y < 512; y++) {
                pixelWriter.setColor(x, y, Color.rgb(511 - y, 255, x - 512));
            }
        }
        for (int x = 768; x < 1024; x++) {
            for (int y = 256; y < 512; y++) {
                pixelWriter.setColor(x, y, Color.rgb(511 - y, 1023 - x, 255));
            }
        }
        for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                pixelWriter.setColor(x, y, Color.rgb(255, 0, Math.min(511 - x - y, 255)));
            }
        }
        for (int x = 512; x < 768; x++) {
            for (int y = 0; y < 256; y++) {
                pixelWriter.setColor(x, y, Color.rgb(255, 255, Math.min(x - y - 256, 255)));
            }
        }
        for (int x = 0; x < 256; x++) {
            for (int y = 512; y < 768; y++) {
                pixelWriter.setColor(x, y, Color.rgb(0, 0, Math.min(y - x - 256, 255)));
            }
        }
        for (int x = 512; x < 768; x++) {
            for (int y = 512; y < 768; y++) {
                pixelWriter.setColor(x, y, Color.rgb(0, 255, Math.min(y + x - 1024, 255)));
            }
        }
        for (int x = 768; x < 1024; x++) {
            for (int y = 0; y < 256; y++) {
                pixelWriter.setColor(x, y, Color.rgb(255, 1023 - x, 255));
            }
        }
        for (int x = 768; x < 1024; x++) {
            for (int y = 512; y < 768; y++) {
                pixelWriter.setColor(x, y, Color.rgb(0, 1023 - x, 255));
            }
        }
        for (int x = 256; x < 512; x++) {
            for (int y = 768; y < 896; y++) {
                pixelWriter.setColor(x, y, Color.rgb(0, x - 256, 255));
            }
        }
        for (int x = 256; x < 512; x++) {
            for (int y = 896; y < 1024; y++) {
                pixelWriter.setColor(x, y, Color.rgb(255, x - 256, 255));
            }
        }

        try {
            HashMap<ImageSaveParam, Object> params = new HashMap<>();
            params.put(ImageSaveParam.JPEG_COMPRESSION, 1f);
            ImageIO.writeImage(new File("D:/tesciki.jpg"), texture, params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return texture;
    }
}
