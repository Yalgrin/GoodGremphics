package pl.yalgrin.gremphics.io;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import pl.yalgrin.gremphics.exception.PPMException;

import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

public class ImageIO {
    private static final int READ_ERROR_VALUE = -150;
    private static final int MAX_BUFFER_SIZE = 1000000;
    private static byte[] streamBuffer;
    private static int bufferCounter = 1, bufferElements;

    public static Image readImage(File file) throws IOException {
        bufferElements = bufferCounter = 1;
        String extension = getFileExtension(file);
        if (extension.equals("ppm")) {
            return SwingFXUtils.toFXImage(readPPMImage(file), null);
        }
        BufferedImage image = javax.imageio.ImageIO.read(file);
        if (image == null) {
            throw new IOException("File is not an image or is corrupted.");
        }
        return SwingFXUtils.toFXImage(image, null);
    }

    public static void writeImage(File file, Image image, Map<ImageSaveParam, Object> params) throws IOException {
        String extension = getFileExtension(file);
        if (extension.equals("jpeg") || extension.equals("jpg")) {
            JPEGImageWriteParam param = new JPEGImageWriteParam(null);
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality((Float) (params.get(ImageSaveParam.JPEG_COMPRESSION) != null ? params.get(ImageSaveParam.JPEG_COMPRESSION) : ImageSaveParam.JPEG_COMPRESSION.getDefaultValue()));

            final ImageWriter writer = javax.imageio.ImageIO.getImageWritersByMIMEType("image/jpeg").next();
            writer.setOutput(new FileImageOutputStream(file));

            final BufferedImage rawVersion = SwingFXUtils.fromFXImage(image, null);

            final int w = rawVersion.getWidth();
            final int h = rawVersion.getHeight();

            BufferedImage imageToSave = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            final Graphics2D g2 = imageToSave.createGraphics();
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, w, h);
            g2.drawImage(rawVersion, 0, 0, null);
            writer.write(null, new IIOImage(imageToSave, null, null), param);
        } else {
            throw new IOException("Unrecognized format");
        }
    }

    private static BufferedImage readPPMImage(File file) throws IOException {
        long timeInMillis = System.currentTimeMillis();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String type = readUncommentedPart(fileInputStream);
            boolean readPlainText;
            if (type.equals("P3")) {
                readPlainText = true;
            } else if (type.equals("P6")) {
                readPlainText = false;
            } else {
                throw new PPMException("Invalid header.");
            }
            int dimX = Integer.parseInt(readUncommentedPart(fileInputStream));
            int dimY = Integer.parseInt(readUncommentedPart(fileInputStream));
            ImageDTO imageDTO = new ImageDTO(dimX, dimY);
            int maxValue = Integer.parseInt(readUncommentedPart(fileInputStream));
            int numOfBytes;
            if (maxValue >= (1 << 16)) {
                throw new PPMException("Value per color is too big");
            } else if (maxValue >= (1 << 8)) {
                numOfBytes = 2;
            } else {
                numOfBytes = 1;
            }
            if (readPlainText) {
                readPlainTextPPM(imageDTO, fileInputStream, numOfBytes, maxValue);
            } else {
                readBinaryPPM(imageDTO, fileInputStream, numOfBytes, maxValue);
            }
            System.out.println("Wczytano w: " + (System.currentTimeMillis() - timeInMillis));
            return imageDTOToBufImage(imageDTO);
        }
    }

    private static void readPlainTextPPM(ImageDTO dto, InputStream reader, int numOfBytes, int maxValue) throws IOException {
        int val;
        for (int y = 0; y < dto.getHeight(); y++) {
            for (int x = 0; x < dto.getWidth(); x++) {
                val = Integer.parseInt(readUncommentedPart(reader, true, dto.getWidth() * dto.getHeight()));
                if (val > maxValue) {
                    val = maxValue;
                }
                dto.getR()[x][y] = (int) (val * 255.0 / maxValue);
                val = Integer.parseInt(readUncommentedPart(reader, true, dto.getWidth() * dto.getHeight()));
                if (val > maxValue) {
                    val = maxValue;
                }
                dto.getG()[x][y] = (int) (val * 255.0 / maxValue);
                val = Integer.parseInt(readUncommentedPart(reader, true, dto.getWidth() * dto.getHeight()));
                if (val > maxValue) {
                    val = maxValue;
                }
                dto.getB()[x][y] = (int) (val * 255.0 / maxValue);
            }
        }
//        String str = readUncommentedPart(reader, true, 100);
//        if (!str.isEmpty()) {
//            throw new PPMException("File is corrupted!");
//        }
    }

    private static void readBinaryPPM(ImageDTO imageDTO, InputStream inputStream, int numOfBytes, int maxValue) throws IOException {
        int inVal, val;
        for (int y = 0; y < imageDTO.getHeight(); y++) {
            for (int x = 0; x < imageDTO.getWidth(); x++) {
                val = 0;
                for (int j = 0; j < numOfBytes; j++) {
                    val <<= 8;
                    val += (int) ((inVal = read(inputStream, true, imageDTO.getWidth() * imageDTO.getHeight() * numOfBytes)) & 0xFF);
                    if (inVal == READ_ERROR_VALUE) {
                        throw new PPMException("File is corrupted!");
                    }
                }
                if (val > maxValue) {
                    val = maxValue;
                }
                imageDTO.getR()[x][y] = (int) (val * 255.0 / maxValue);
                val = 0;
                for (int j = 0; j < numOfBytes; j++) {
                    val <<= 8;
                    val += (int) ((inVal = read(inputStream, true, imageDTO.getWidth() * imageDTO.getHeight() * numOfBytes)) & 0xFF);
                    if (inVal == READ_ERROR_VALUE) {
                        throw new PPMException("File is corrupted!");
                    }
                }
                if (val > maxValue) {
                    val = maxValue;
                }
                imageDTO.getG()[x][y] = (int) (val * 255.0 / maxValue);
                val = 0;
                for (int j = 0; j < numOfBytes; j++) {
                    val <<= 8;
                    val += (int) ((inVal = read(inputStream, true, imageDTO.getWidth() * imageDTO.getHeight() * numOfBytes)) & 0xFF);
                    if (inVal == READ_ERROR_VALUE) {
                        throw new PPMException("File is corrupted!");
                    }
                }
                if (val > maxValue) {
                    val = maxValue;
                }
                imageDTO.getB()[x][y] = (int) (val * 255.0 / maxValue);
            }
        }
//        String str = readUncommentedPart(inputStream, true, 100);
//        if (!str.isEmpty()) {
//            throw new PPMException("File is corrupted!");
//        }
    }

    private static String readLineWithoutComments(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line.length() > 0 && line.charAt(0) == '#') {
            return readLineWithoutComments(reader);
        }
        return line;
    }

    public static String readUncommentedPart(InputStream inputStream) throws IOException {
        return readUncommentedPart(inputStream, false, 0);
    }

    public static String readUncommentedPart(InputStream inputStream, boolean buffering, int bufferSize) throws IOException {
        StringBuilder sb = new StringBuilder();
        char c;
        int i;
        boolean isComment = false;
        while (Character.isWhitespace(c = (char) (i = read(inputStream, buffering, bufferSize))) && i != READ_ERROR_VALUE)
            ;
        if (i == READ_ERROR_VALUE) {
            return "";
        }
        while (true) {
            if (c == '#') {
                isComment = true;
            }

            if (isComment) {
                if (c == '\n') {
                    isComment = false;
                    if (sb.length() > 0) {
                        return sb.toString();
                    }
                }
            } else {
                if (!Character.isWhitespace(c)) {
                    sb.append(c);
                } else {
                    if (sb.length() > 0) {
                        return sb.toString();
                    }
                }
            }

            c = (char) (i = read(inputStream, buffering, bufferSize));
            if (i == READ_ERROR_VALUE) {
                return sb.toString();
            }
        }
    }

    private static int read(InputStream inputStream, boolean buffering, int bufferSize) throws IOException {
        if (buffering) {
            return readBuffered(inputStream, bufferSize);
        }
        return readUnbuffered(inputStream);
    }

    private static int readBuffered(InputStream inputStream, int bufferSize) throws IOException {
        bufferSize = Math.min(bufferSize, MAX_BUFFER_SIZE);
        if (bufferCounter >= bufferElements) {
            if (streamBuffer == null || streamBuffer.length < bufferSize) {
                streamBuffer = new byte[bufferSize];
            }
            bufferElements = inputStream.read(streamBuffer, 0, bufferSize);
            if (bufferElements == -1) {
                return READ_ERROR_VALUE;
            }
            bufferCounter = 0;
        }
        return streamBuffer[bufferCounter++];
    }

    private static int readUnbuffered(InputStream inputStream) throws IOException {
        return inputStream.read();
    }

    private static BufferedImage imageDTOToBufImage(ImageDTO dto) {
        BufferedImage bufferedImage = new BufferedImage(dto.getWidth(), dto.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < dto.getWidth(); x++) {
            for (int y = 0; y < dto.getHeight(); y++) {
                bufferedImage.setRGB(x, y, (dto.getR()[x][y] << 16) + (dto.getG()[x][y] << 8) + dto.getB()[x][y]);
            }
        }
        return bufferedImage;
    }

    private static String getFileExtension(File file) {
        String filePath = file.getPath();
        String[] pathParts = filePath.split("/");
        filePath = pathParts[pathParts.length - 1];
        pathParts = filePath.split("\\.");
        return pathParts[pathParts.length - 1].toLowerCase();
    }
}
