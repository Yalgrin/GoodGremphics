package pl.yalgrin.gremphics.processing;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

public class HistogramProcessor extends ColorProcessor {

    private static HistogramProcessor instance = new HistogramProcessor();

    public static HistogramProcessor getInstance() {
        return instance;
    }

    private HistogramProcessor() {
        super();
    }

    public Histogram getHistogram(WritableImage image) {
        image = ColorProcessor.getInstance().turnToGreyscale(image);

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];

        PixelReader pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, WritablePixelFormat.getByteBgraPreInstance(), buffer, 0, width * 4);

        Histogram histogram = new Histogram();
        int[] histArray = histogram.getHistogram();

        for (int i = 0; i < buffer.length; i += 4) {
            histArray[buffer[i] & 0xFF]++;
        }

        return histogram;
    }

    public WritableImage stretchImageHistogram(WritableImage image) {
        image = ColorProcessor.getInstance().turnToGreyscale(image);
        Histogram histogram = getHistogram(image);
        int min = 0, max = 255;
        for (int i = 0; i < histogram.getHistogram().length; i++) {
            if (histogram.getHistogram()[i] > 0) {
                min = i;
                break;
            }
        }
        for (int i = histogram.getHistogram().length - 1; i >= 0; i--) {
            if (histogram.getHistogram()[i] > 0) {
                max = i;
                break;
            }
        }

        if (min == max) {
            return image;
        }

        byte[] lut = new byte[256];
        for (int i = 0; i < lut.length; i++) {
            if (i < min) {
                lut[i] = 0;
            } else if (i > max) {
                lut[i] = (byte) 255;
            } else {
                lut[i] = (byte) ((255.0 / (max - min)) * (i - min));
            }
        }
        return lutOperation(image, lut);
    }

    public WritableImage equalizeHistogram(WritableImage image) {
        image = ColorProcessor.getInstance().turnToGreyscale(image);
        Histogram histogram = getHistogram(image);
        int[] cumulativeDistribution = histogram.getCumulativeDistribution();
        int minCDstr = 0;
        for (int i = 0; i < cumulativeDistribution.length; i++) {
            if (cumulativeDistribution[i] > 0) {
                minCDstr = cumulativeDistribution[i];
                break;
            }
        }

        int pixels = (int) (image.getWidth() * image.getHeight());
        byte[] lut = new byte[256];
        for (int i = 0; i < cumulativeDistribution.length; i++) {
            lut[i] = (byte) ((int) (((double) cumulativeDistribution[i] - minCDstr) / ((double) pixels - minCDstr) * 255.0));
        }

        return lutOperation(image, lut);
    }
}
