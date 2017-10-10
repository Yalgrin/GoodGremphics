package pl.yalgrin.gremphics.io;

public class ImageDTO {
    private int[][] r, g, b;
    private int width, height;

    public ImageDTO(int width, int height) {
        this.width = width;
        this.height = height;

        r = new int[width][height];
        g = new int[width][height];
        b = new int[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getB() {
        return b;
    }

    public int[][] getG() {
        return g;
    }

    public int[][] getR() {
        return r;
    }
}
