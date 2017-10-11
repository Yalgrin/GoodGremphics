package pl.yalgrin.gremphics.io;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.*;

public class ImageIO {
    public static Image readImage(File file) throws IOException {
        String extension = getFileExtension(file);
        if (extension.equals("ppm")) {
            return SwingFXUtils.toFXImage(readPPMImage(file), null);
        }
        return SwingFXUtils.toFXImage(javax.imageio.ImageIO.read(file), null);
    }

    private static BufferedImage readPPMImage(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String type = readUncommentedPart(fileInputStream);
            int maxBytesPerPixel;
            boolean readPlainText = false;
            if (type.equals("P3")) {
                readPlainText = true;
            } else if (type.equals("P6")) {
                readPlainText = false;
            } else {
                throw new IOException("Shit happened");
            }
            int dimX = Integer.parseInt(readUncommentedPart(fileInputStream));
            int dimY = Integer.parseInt(readUncommentedPart(fileInputStream));
            ImageDTO imageDTO = new ImageDTO(dimX, dimY);
            int maxValue = Integer.parseInt(readUncommentedPart(fileInputStream));
            int numOfBytes;
            if (maxValue >= (1 << 16)) {
                throw new IOException("TOO BIG VALUE");
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
            return imageDTOToBufImage(imageDTO);
        }
    }

    private static void readPlainTextPPM(ImageDTO dto, InputStream reader, int numOfBytes, int maxValue) throws IOException {
        //TODO: Optimize! ITS WAY TOO SLOW
        int val;
        for (int y = 0; y < dto.getHeight(); y++) {
            for (int x = 0; x < dto.getWidth(); x++) {
                val = Integer.parseInt(readUncommentedPart(reader));
                if (val > maxValue) {
                    throw new IOException("TOO BIG VALUE");
                }
                dto.getR()[x][y] = (int) (val * 255.0 / (maxValue + 1));
                val = Integer.parseInt(readUncommentedPart(reader));
                if (val > maxValue) {
                    throw new IOException("TOO BIG VALUE");
                }
                dto.getG()[x][y] = (int) (val * 255.0 / (maxValue + 1));
                val = Integer.parseInt(readUncommentedPart(reader));
                if (val > maxValue) {
                    throw new IOException("TOO BIG VALUE");
                }
                dto.getB()[x][y] = (int) (val * 255.0 / (maxValue + 1));
            }
        }
    }

    private static void readBinaryPPM(ImageDTO imageDTO, InputStream inputStream, int numOfBytes, int maxValue) throws IOException {
        int bytesPerRead = imageDTO.getWidth() * 3 * numOfBytes;
        byte[] buffer = new byte[bytesPerRead];
        int x = 0, y = 0;
        int bytesRead = 0;
        for (int i = 0; i < imageDTO.getHeight(); i++) {
            bytesRead = inputStream.read(buffer, 0, bytesPerRead);
            if (bytesRead < bytesPerRead) {
                System.out.println("poop");
                //throw new IOException("FILE IS SHIT");
            }
            int val = 0;
            for (int k = 0; k < bytesPerRead; k += 3 * numOfBytes) {
                for (int j = 0; j < numOfBytes; j++) {
                    val <<= 8;
                    val += (int) (buffer[k + j] & 0xFF);
                }
                imageDTO.getR()[x][y] = (int) (val * 256.0 / (maxValue + 1));
                val = 0;
                for (int j = 0; j < numOfBytes; j++) {
                    val <<= 8;
                    val += (int) (buffer[k + j + numOfBytes] & 0xFF);
                }
                imageDTO.getG()[x][y] = (int) (val * 256.0 / (maxValue + 1));
                val = 0;
                for (int j = 0; j < numOfBytes; j++) {
                    val <<= 8;
                    val += (int) (buffer[k + j + numOfBytes * 2] & 0xFF);
                }
                imageDTO.getB()[x][y] = (int) (val * 256.0 / (maxValue + 1));
                x++;
                if (x == imageDTO.getWidth()) {
                    x = 0;
                    y++;
                }
            }
        }
    }

    private static String readLineWithoutComments(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line.length() > 0 && line.charAt(0) == '#') {
            return readLineWithoutComments(reader);
        }
        return line;
    }

    public static String readUncommentedPart(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        char c;
        int i;
        boolean isComment = false;
        while (Character.isWhitespace(c = (char) (i = inputStream.read()))) ;
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

            c = (char) (i = inputStream.read());
            if (i == -1) {
                return sb.toString();
            }
        }
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
