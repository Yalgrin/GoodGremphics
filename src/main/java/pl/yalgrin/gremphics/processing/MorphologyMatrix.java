package pl.yalgrin.gremphics.processing;

public class MorphologyMatrix {
    private boolean[][] matrix;
    private int width, height;

    public MorphologyMatrix() {
        this(5, 5);
    }

    public MorphologyMatrix(int width, int height) {
        matrix = new boolean[width][height];

        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean[][] getMatrix() {
        return matrix;
    }

    public int getWidthOffset() {
        return -(width / 2);
    }

    public int getHeightOffset() {
        return -(height / 2);
    }
}
