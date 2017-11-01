package pl.yalgrin.gremphics.processing;

public class Histogram {
    private int[] histogram = new int[256];
    private int[] cumulativeDistribution;

    public int[] getHistogram() {
        return histogram;
    }

    public void setHistogram(int[] histogram) {
        this.histogram = histogram;
    }

    public int[] getCumulativeDistribution() {
        if (cumulativeDistribution == null) {
            cumulativeDistribution = new int[256];
            cumulativeDistribution[0] = histogram[0];
            for (int i = 1; i < histogram.length; i++) {
                cumulativeDistribution[i] = cumulativeDistribution[i - 1] + histogram[i];
            }
        }
        return cumulativeDistribution;
    }
}
