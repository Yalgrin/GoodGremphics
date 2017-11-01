package pl.yalgrin.gremphics.processing;

import javafx.scene.image.*;

public class ColorProcessor {
    public enum PointOperation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, BRIGHTEN, DARKEN, GREYSCALE
    }

    public enum Color {
        BLUE(0), GREEN(1), RED(2);

        private int index;

        Color(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private static final byte[] IDENTITY_LUT, GREYSCALE_SUM_LUT;

    static {
        IDENTITY_LUT = new byte[256];
        for (int i = 0; i < 256; i++) {
            IDENTITY_LUT[i] = (byte) i;
        }

        GREYSCALE_SUM_LUT = new byte[766];
        for (int i = 0; i < 766; i++) {
            GREYSCALE_SUM_LUT[i] = (byte) (Math.round(i / 3.0));
        }
    }

    private static ColorProcessor instance = new ColorProcessor();

    public static ColorProcessor getInstance() {
        return instance;
    }

    protected ColorProcessor() {
    }

    protected WritableImage lutOperation(WritableImage image, byte[][] lutArray) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];

        PixelReader pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, WritablePixelFormat.getByteBgraPreInstance(), buffer, 0, width * 4);

        int r = Color.RED.getIndex(), g = Color.GREEN.getIndex(), b = Color.BLUE.getIndex();
        for (int i = 0; i < buffer.length; i += 4) {
            buffer[i + b] = lutArray[b][buffer[i + b] & 0xFF];
            buffer[i + g] = lutArray[g][buffer[i + g] & 0xFF];
            buffer[i + r] = lutArray[r][buffer[i + r] & 0xFF];
        }

        WritableImage newImage = new WritableImage(width, height);
        PixelWriter pixelWriter = newImage.getPixelWriter();
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getByteBgraPreInstance(), buffer, 0, width * 4);

        return newImage;
    }

    private WritableImage sumLutOperation(WritableImage image, byte[] lut) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];

        PixelReader pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, WritablePixelFormat.getByteBgraPreInstance(), buffer, 0, width * 4);

        int r = Color.RED.getIndex(), g = Color.GREEN.getIndex(), b = Color.BLUE.getIndex();
        int sum;
        for (int i = 0; i < buffer.length; i += 4) {
            sum = (buffer[i + b] & 0xFF) + (buffer[i + g] & 0xFF) + (buffer[i + r] & 0xFF);
            buffer[i + b] = buffer[i + g] = buffer[i + r] = lut[sum];
        }

        WritableImage newImage = new WritableImage(width, height);
        PixelWriter pixelWriter = newImage.getPixelWriter();
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getByteBgraPreInstance(), buffer, 0, width * 4);

        return newImage;
    }

    public WritableImage pointOperation(WritableImage image, PointOperation pointOperation, double red, double green, double blue) {
        int r = Color.RED.getIndex(), g = Color.GREEN.getIndex(), b = Color.BLUE.getIndex();
        byte[][] lutArray = new byte[3][];
        lutArray[b] = getPointOperationLut(pointOperation, blue);
        lutArray[g] = getPointOperationLut(pointOperation, green);
        lutArray[r] = getPointOperationLut(pointOperation, red);
        return lutOperation(image, lutArray);
    }

    private byte[] getPointOperationLut(PointOperation pointOperation, double value) {
        byte[] lut = new byte[256];
        switch (pointOperation) {
            case ADD:
                if (value == 0) {
                    return IDENTITY_LUT;
                }
                for (int i = 0; i < 256; i++) {
                    lut[i] = (byte) (Math.max(0, Math.min(Math.round(i + value), 255)));
                }
                break;
            case SUBTRACT:
                if (value == 0) {
                    return IDENTITY_LUT;
                }
                for (int i = 0; i < 256; i++) {
                    lut[i] = (byte) (Math.max(0, Math.min(Math.round(i - value), 255)));
                }
                break;
            case MULTIPLY:
                if (value == 1) {
                    return IDENTITY_LUT;
                }
                for (int i = 0; i < 256; i++) {
                    lut[i] = (byte) (Math.max(0, Math.min(Math.round(i * value), 255)));
                }
                break;
            case DIVIDE:
                if (value == 1) {
                    return IDENTITY_LUT;
                }
                if (value == 0) {
                    throw new IllegalArgumentException("Can't divide by zero!");
                }
                for (int i = 0; i < 256; i++) {
                    lut[i] = (byte) (Math.max(0, Math.min(Math.round(i / value), 255)));
                }
                break;
            default:
                return null;
        }
        return lut;
    }

    public WritableImage brightenImage(WritableImage image, double value) {
        return pointOperation(image, PointOperation.ADD, value, value, value);
    }

    public WritableImage turnToGreyscale(WritableImage image) {
        return sumLutOperation(image, GREYSCALE_SUM_LUT);
    }
}
