package pl.yalgrin.gremphics.io;

import java.awt.image.BufferedImage;
import java.io.*;

public class ImageIO {
    public static BufferedImage readImage(File file) throws IOException {
        String extension = getFileExtension(file);
        if (extension.equals("ppm")) {
            return readPPMImage(file);
        }
        return javax.imageio.ImageIO.read(file);
    }

    private static BufferedImage readPPMImage(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "US-ASCII"))) {
            String type = readLineWithoutComments(reader);
            int maxBytesPerPixel;
            if (type.equals("P3")) {
                maxBytesPerPixel = 1;
            } else if (type.equals("P6")) {
                maxBytesPerPixel = 2;
            } else {
                throw new IOException("Shit happened");
            }
            String dimensions = readLineWithoutComments(reader);
            String[] dims = dimensions.split("\\s+");
            int[] dimValues = new int[]{Integer.parseInt(dims[0]), Integer.parseInt(dims[1])};
            ImageDTO imageDTO = new ImageDTO(dimValues[0], dimValues[1]);
            int maxValue = Integer.parseInt(readLineWithoutComments(reader));
            if (maxValue > (2 << (maxBytesPerPixel * 8)) - 1) {
                throw new IOException("TOO BIG");
            }
            int bytesPer = 0;
            while (maxValue > 0) {
                maxValue >>= 8;
                bytesPer++;
            }
            int bytesPerRead = dimValues[0] * 3 * bytesPer;
            char[] buffer = new char[bytesPerRead];
            int x = 0, y = 0;
            int bytesRead = 0;
            for (int i = 0; i < dimValues[1]; i++) {
                bytesRead = reader.read(buffer, 0, bytesPerRead);
                if (bytesRead < bytesPerRead) {
                    throw new IOException("FILE IS SHIT");
                }
                int val = 0;
                for (int k = 0; k < bytesPerRead; k += 3 * bytesPer) {
                    for (int j = 0; j < bytesPer; j++) {
                        val <<= 8;
                        val += buffer[k + j];
                    }
                    imageDTO.getR()[x][y] = val;
                    val = 0;
                    for (int j = 0; j < bytesPer; j++) {
                        val <<= 8;
                        val += buffer[k + j + bytesPer];
                    }
                    imageDTO.getG()[x][y] = val;
                    val = 0;
                    for (int j = 0; j < bytesPer; j++) {
                        val <<= 8;
                        val += buffer[k + j + bytesPer * 2];
                    }
                    imageDTO.getB()[x][y] = val;
                    x++;
                    if (x == dimValues[0]) {
                        x = 0;
                        y++;
                    }
                }
            }
            return imageDTOToBufImage(imageDTO);
        }
    }

    private static String readLineWithoutComments(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line.length() > 0 && line.charAt(0) == '#') {
            return readLineWithoutComments(reader);
        }
        return line;
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
