package pl.yalgrin.gremphics.processing;

import javafx.scene.image.WritableImage;

public class BinarizationProcessor extends ColorProcessor {

    private static BinarizationProcessor instance = new BinarizationProcessor();

    public static BinarizationProcessor getInstance() {
        return instance;
    }

    private BinarizationProcessor() {
        super();
    }

    public WritableImage binarize(WritableImage image, int threshold) {
        image = turnToGreyscale(image);

        byte[] lut = new byte[256];
        for (int i = threshold; i < 256; i++) {
            lut[i] = (byte) 255;
        }

        return lutOperation(image, new byte[][]{lut, lut, lut});
    }
}
