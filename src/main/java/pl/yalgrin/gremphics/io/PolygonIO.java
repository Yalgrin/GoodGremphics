package pl.yalgrin.gremphics.io;

import pl.yalgrin.gremphics.control.PolygonCanvas;
import pl.yalgrin.gremphics.control.PolygonPoint;
import pl.yalgrin.gremphics.control.PolygonShape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolygonIO {
    public static void writeShapes(File file, List<PolygonShape> shapeList) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(intToByteArray(shapeList.size()), 0, 4);
            for (PolygonShape shape : shapeList) {
                stream.write(intToByteArray(shape.getPoints().size()), 0, 4);
                for (PolygonPoint point : shape.getPoints()) {
                    stream.write(intToByteArray((int) point.getX()), 0, 4);
                    stream.write(intToByteArray((int) point.getY()), 0, 4);
                }
            }
        }
    }

    public static void readShapes(File file, PolygonCanvas canvas) throws IOException {
        byte[] fourBytes = new byte[4];
        List<PolygonShape> shapeList = new ArrayList<>();
        try (FileInputStream stream = new FileInputStream(file)) {
            int count = stream.read(fourBytes, 0, 4);
            if (count < 4) {
                throw new IOException("File corrupted!");
            }
            int shapeCount = intToByteArray(fourBytes);
            for (int i = 0; i < shapeCount; i++) {
                PolygonShape shape = new PolygonShape(canvas);
                count = stream.read(fourBytes, 0, 4);
                if (count < 4) {
                    throw new IOException("File corrupted!");
                }
                int pointCount = intToByteArray(fourBytes);
                for (int j = 0; j < pointCount; j++) {
                    count = stream.read(fourBytes, 0, 4);
                    if (count < 4) {
                        throw new IOException("File corrupted!");
                    }
                    int x = intToByteArray(fourBytes);
                    count = stream.read(fourBytes, 0, 4);
                    if (count < 4) {
                        throw new IOException("File corrupted!");
                    }
                    int y = intToByteArray(fourBytes);
                    shape.addPoint(x, y);
                }
                shapeList.add(shape);
            }
        }
        canvas.setShapes(shapeList);
    }

    private static byte[] intToByteArray(int val) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (val & 0xFF);
        bytes[1] = (byte) ((val >> 8) & 0xFF);
        bytes[2] = (byte) ((val >> 16) & 0xFF);
        bytes[3] = (byte) ((val >> 24) & 0xFF);
        return bytes;
    }

    private static int intToByteArray(byte[] bytes) {
        return ((bytes[3] & 0xFF) << 24) | ((bytes[2] & 0xFF) << 16) | ((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF);
    }
}
