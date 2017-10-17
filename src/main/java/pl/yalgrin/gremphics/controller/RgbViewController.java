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
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import pl.yalgrin.gremphics.io.ImageIO;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class RgbViewController extends AbstractController {

    public class Shape3DRectangle extends TriangleMesh {

        //https://stackoverflow.com/questions/19459012/how-to-create-custom-3d-model-in-javafx-8
        public Shape3DRectangle(float length) {
            float[] points = {
                    -length / 2, -length / 2, -length / 2, // P0 - left up front
                    length / 2, -length / 2, -length / 2, // P1 - right up front
                    -length / 2, length / 2, -length / 2, // P2 - left down front
                    length / 2, length / 2, -length / 2, // P3 - right down front
                    -length / 2, -length / 2, length / 2, // P4 - left up back
                    length / 2, -length / 2, length / 2, // P5 - right up back
                    -length / 2, length / 2, length / 2, // P6 - left down back
                    length / 2, length / 2, length / 2, // P7 - right down back
            };
            float[] texCoords = {
                    0.25f, 0, //T0
                    0.5f, 0, //T1
                    0, 0.25f, //T2
                    0.25f, 0.25f, //T3
                    0.5f, 0.25f, //T4
                    0.75f, 0.25f, //T5
                    1, 0.25f, //T6
                    0, 0.5f, //T7
                    0.25f, 0.5f, //T8
                    0.5f, 0.5f, //T9
                    0.75f, 0.5f, //T10
                    1, 0.5f, //T11
                    0.25f, 0.75f, //T12
                    0.5f, 0.75f //T13
            };
            int[] faces = {
                    5, 1, 4, 0, 0, 3,
                    5, 1, 0, 3, 1, 4,
                    0, 3, 4, 2, 6, 7,
                    0, 3, 6, 7, 2, 8,
                    1, 4, 0, 3, 2, 8,
                    1, 4, 2, 8, 3, 9,
                    5, 5, 1, 4, 3, 9,
                    5, 5, 3, 9, 7, 10,
                    4, 6, 5, 5, 7, 10,
                    4, 6, 7, 10, 6, 11,
                    3, 9, 2, 8, 6, 12,
                    3, 9, 6, 12, 7, 13,
                    4, 0, 4, 0, 0, 3,
                    4, 0, 4, 0, 0, 3,
            };

            getPoints().setAll(points);
            getTexCoords().setAll(texCoords);
            getFaces().setAll(faces);
        }
    }

    @FXML
    private AnchorPane anchorPane;

    private WritableImage texture;

    private double lastX, lastY;
    private Rotate rotateX, rotateY;

    private double anchorX, anchorY, anchorXAngle, anchorYAngle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        texture = new WritableImage(1024, 1024);
        PixelWriter pixelWriter = texture.getPixelWriter();
//        for (int x = 0; x < 1024; x++) {
//            for (int y = 0; y < 1024; y++) {
//                pixelWriter.setColor(x, y, Color.color(Math.random(), Math.random(), Math.random()));
//            }
//        }

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


        try {
            ImageIO.writeImage(new File("D:/tesciki.jpg"), texture, new HashMap<>());
        } catch (IOException e) {
            e.printStackTrace();
        }

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(texture);

        final MeshView rect = new MeshView(
                new Shape3DRectangle(256)
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
}
