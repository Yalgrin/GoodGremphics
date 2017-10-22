package pl.yalgrin.gremphics.shape;


import javafx.collections.ObservableFloatArray;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;

public class ShapeCone extends TriangleMesh {

    //https://stackoverflow.com/questions/19459012/how-to-create-custom-3d-model-in-javafx-8
    public ShapeCone(float length) {
//        float[] points = {
//                -length / 2, -length / 2, -length / 2, // P0 - left up front
//                length / 2, -length / 2, -length / 2, // P1 - right up front
//                -length / 2, length / 2, -length / 2, // P2 - left down front
//                length / 2, length / 2, -length / 2, // P3 - right down front
//                -length / 2, -length / 2, length / 2, // P4 - left up back
//                length / 2, -length / 2, length / 2, // P5 - right up back
//                -length / 2, length / 2, length / 2, // P6 - left down back
//                length / 2, length / 2, length / 2, // P7 - right down back
//        };
//        float[] texCoords = {
//                0.25f, 0, //T0
//                0.5f, 0, //T1
//                0, 0.25f, //T2
//                0.25f, 0.25f, //T3
//                0.5f, 0.25f, //T4
//                0.75f, 0.25f, //T5
//                1, 0.25f, //T6
//                0, 0.5f, //T7
//                0.25f, 0.5f, //T8
//                0.5f, 0.5f, //T9
//                0.75f, 0.5f, //T10
//                1, 0.5f, //T11
//                0.25f, 0.75f, //T12
//                0.5f, 0.75f //T13
//        };
//        int[] faces = {
//                5, 1, 4, 0, 0, 3,
//                5, 1, 0, 3, 1, 4,
//                0, 3, 4, 2, 6, 7,
//                0, 3, 6, 7, 2, 8,
//                1, 4, 0, 3, 2, 8,
//                1, 4, 2, 8, 3, 9,
//                5, 5, 1, 4, 3, 9,
//                5, 5, 3, 9, 7, 10,
//                4, 6, 5, 5, 7, 10,
//                4, 6, 7, 10, 6, 11,
//                3, 9, 2, 8, 6, 12,
//                3, 9, 6, 12, 7, 13,
//        };
//
//        getPoints().setAll(points);
//        getTexCoords().setAll(texCoords);
//        getFaces().setAll(faces);

        ObservableFloatArray points = getPoints();
        points.addAll(0, -length / 2, 0);

        int numOfPoints = 360;
        double rad;
        for (int i = 0; i < numOfPoints; i++) {
            rad = (Math.PI * 2 * i / numOfPoints);
            points.addAll((float) (Math.sin(rad) * length / 2), length / 2, (float) (Math.cos(rad) * length / 2));
        }

        points.addAll(0, length / 2, 0);

        ObservableFaceArray faces = getFaces();
        for (int i = 1; i < numOfPoints; i++) {
            faces.addAll(0, numOfPoints + i, i, i - 1, i + 1, i);
        }
        faces.addAll(0, 2 * numOfPoints, numOfPoints, numOfPoints - 1, 1, numOfPoints);

        for (int i = 1; i < numOfPoints; i++) {
            faces.addAll(numOfPoints + 1, 2 * numOfPoints + i, i + 1, i, i, i - 1);
        }
        faces.addAll(numOfPoints + 1, 3 * numOfPoints, numOfPoints, numOfPoints - 1, 1, numOfPoints);

        ObservableFloatArray texCoords = getTexCoords();
        for (int i = 0; i < numOfPoints; i++) {
            texCoords.addAll((float) i / numOfPoints, 0.5f);
        }
        texCoords.addAll(1f, 0.5f);
        for (int i = 0; i < numOfPoints; i++) {
            texCoords.addAll((2f * i + 1f) / (2f * numOfPoints), 0.0f);
        }
        for (int i = 0; i < numOfPoints; i++) {
            texCoords.addAll((2f * i + 1f) / (2f * numOfPoints), 1.0f);
        }

    }
}
