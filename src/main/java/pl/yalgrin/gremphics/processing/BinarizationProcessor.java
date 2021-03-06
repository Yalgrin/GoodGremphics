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

    public WritableImage meanIterativeSelection(WritableImage image) {
        image = turnToGreyscale(image);
        Histogram histogram = HistogramProcessor.getInstance().getHistogram(image);
        int[] hist = histogram.getHistogram();

        int threshold, newThreshold = 128;
        long totalWhite, totalBlack, countWhite, countBlack;
        double meanWhite, meanBlack;

        do {
            threshold = newThreshold;

            totalWhite = totalBlack = countWhite = countBlack = 0;
            for (int i = 0; i < hist.length; i++) {
                if (i < threshold) {
                    totalBlack += hist[i] * i;
                    countBlack += hist[i];
                } else {
                    totalWhite += hist[i] * i;
                    countWhite += hist[i];
                }
            }

            meanWhite = ((double) totalWhite / countWhite);
            meanBlack = ((double) totalBlack / countBlack);

            newThreshold = (int) Math.abs((meanWhite + meanBlack) / 2);

            System.out.println("Calculated threshold: " + newThreshold);
        }
        while (Math.abs((int) meanWhite - (int) meanBlack) != 0 && threshold != newThreshold);

        System.out.println("Found threshold: " + newThreshold);

        return binarize(image, newThreshold);
    }

    //http://computervision.wikia.com/wiki/Entropy_thresholding
    public WritableImage entropySelection(WritableImage image) {
        image = turnToGreyscale(image);
        Histogram histogram = HistogramProcessor.getInstance().getHistogram(image);

        int[] hist = histogram.getHistogram();
        double[] prob = new double[256];
        double[] entropy = new double[256];

        for (int i = 0; i < hist.length; i++) {
            prob[i] = hist[i] / (image.getWidth() * image.getHeight());
        }

        double sumProbBlack, sumProbWhite, sumProbBlackLog, sumProbWhiteLog;
        for (int t = 0; t < 256; t++) {
            sumProbBlack = sumProbWhite = sumProbBlackLog = sumProbWhiteLog = 0;

            for (int i = 0; i < t; i++) {
                sumProbBlack += prob[i];
                if (prob[i] != 0) {
                    sumProbBlackLog += (prob[i] * Math.log(prob[i]));
                }
            }

            for (int i = t; i < 256; i++) {
                sumProbWhite += prob[i];
                if (prob[i] != 0) {
                    sumProbWhiteLog += (prob[i] * Math.log(prob[i]));
                }
            }

            entropy[t] = Math.log(sumProbBlack) + Math.log(sumProbWhite) - (sumProbBlackLog / sumProbBlack) - (sumProbWhiteLog / sumProbWhite);
            if (Double.isNaN(entropy[t]) || entropy[t] < 0) {
                entropy[t] = 0;
            }
        }

        int threshold = 0;
        for (int t = 0; t < 256; t++) {
            if (entropy[t] > entropy[threshold]) {
                threshold = t;
            }
        }

        System.out.println("Found threshold: " + threshold);

        return binarize(image, threshold);
    }
}
