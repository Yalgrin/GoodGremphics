package pl.yalgrin.gremphics.threedee;


import javafx.collections.ObservableFloatArray;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;

public class ShapeCone extends TriangleMesh {

    public ShapeCone(float length) {
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
