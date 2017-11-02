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

        return lutOperation(image, lut);
    }

    public WritableImage percentBlackSelection(WritableImage image, double percentage) {
        image = turnToGreyscale(image);
        Histogram histogram = HistogramProcessor.getInstance().getHistogram(image);

        int desired = (int) (percentage * (image.getWidth() * image.getHeight()));
        int threshold = 0;
        for (int i = 0; i < histogram.getCumulativeDistribution().length; i++) {
            if (histogram.getCumulativeDistribution()[i] >= desired) {
                threshold = i;
                break;
            }
        }

        System.out.println("Found threshold: " + threshold);

        return binarize(image, threshold);
    }
}
