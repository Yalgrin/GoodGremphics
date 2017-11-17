package pl.yalgrin.gremphics.processing;

public class BinaryImageDTO {
    private boolean[][] val;
    private int width, height;

    public BinaryImageDTO(int width, int height) {
        this.width = width;
        this.height = height;

        val = new boolean[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean[][] getValues() {
        return val;
    }
}
