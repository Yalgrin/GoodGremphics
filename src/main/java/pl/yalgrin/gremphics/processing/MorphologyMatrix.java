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

    public MorphologyMatrix(int[][] values) {
        this(values.length, values[0].length);

        for (int y = 0; y < values.length; y++) {
            for (int x = 0; x < values[0].length; x++) {
                if (values[x][y] == 1) {
                    matrix[x][y] = true;
                }
            }
        }
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
