package pl.yalgrin.gremphics.processing;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class MorphologyProcessor {
    private BinarizationProcessor binarizationProcessor = BinarizationProcessor.getInstance();

    private static final MorphologyProcessor instance = new MorphologyProcessor();

    public static MorphologyProcessor getInstance() {
        return instance;
    }

    private MorphologyProcessor() {

    }

    private BinaryImageDTO imageToDto(WritableImage image) {
        BinaryImageDTO dto = new BinaryImageDTO((int) image.getWidth(), (int) image.getHeight());
        boolean[][] booleanValues = dto.getValues();
        PixelReader pixelReader = image.getPixelReader();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((pixelReader.getArgb(x, y) & 0xFFFFFF) > 0) {
                    booleanValues[x][y] = true;
                }
            }
        }
        return dto;
    }

    private WritableImage dtoToImage(BinaryImageDTO dto) {
        WritableImage image = new WritableImage(dto.getWidth(), dto.getHeight());
        PixelWriter pixelWriter = image.getPixelWriter();
        boolean[][] booleanValues = dto.getValues();
        for (int y = 0; y < dto.getHeight(); y++) {
            for (int x = 0; x < dto.getWidth(); x++) {
                if (booleanValues[x][y]) {
                    pixelWriter.setArgb(x, y, 0xFFFFFFFF);
                } else {
                    pixelWriter.setArgb(x, y, 0xFF000000);
                }
            }
        }
        return image;
    }

    public WritableImage dilation(WritableImage image, MorphologyMatrix matrix) {
        return dtoToImage(dilation(imageToDto(image), matrix));
    }

    private BinaryImageDTO dilation(BinaryImageDTO dto, MorphologyMatrix matrix) {
        BinaryImageDTO resultDto = new BinaryImageDTO(dto.getWidth(), dto.getHeight());
        boolean[][] origValues = dto.getValues();
        boolean[][] resultValues = resultDto.getValues();
        boolean[][] matrixValues = matrix.getMatrix();
        int offsetX = matrix.getWidthOffset();
        int offsetY = matrix.getHeightOffset();
        for (int y = 0; y < dto.getHeight(); y++) {
            pixelLoop:
            for (int x = 0; x < dto.getWidth(); x++) {
                for (int my = 0; my < matrix.getHeight(); my++) {
                    for (int mx = 0; mx < matrix.getWidth(); mx++) {
                        if (!matrixValues[mx][my]) {
                            continue;
                        }

                        int ix = x + mx + offsetX;
                        if (ix < 0 || ix >= dto.getWidth()) {
                            continue;
                        }
                        int iy = y + my + offsetY;
                        if (iy < 0 || iy >= dto.getHeight()) {
                            continue;
                        }

                        if (origValues[ix][iy]) {
                            resultValues[x][y] = true;
                            continue pixelLoop;
                        }
                    }
                }
            }
        }
        return resultDto;
    }

    public WritableImage erosion(WritableImage image, MorphologyMatrix matrix) {
        return dtoToImage(erosion(imageToDto(image), matrix));
    }

    private BinaryImageDTO erosion(BinaryImageDTO dto, MorphologyMatrix matrix) {
        BinaryImageDTO resultDto = new BinaryImageDTO(dto.getWidth(), dto.getHeight());
        boolean[][] origValues = dto.getValues();
        boolean[][] resultValues = resultDto.getValues();
        boolean[][] matrixValues = matrix.getMatrix();
        int offsetX = matrix.getWidthOffset();
        int offsetY = matrix.getHeightOffset();
        for (int y = 0; y < dto.getHeight(); y++) {
            pixelLoop:
            for (int x = 0; x < dto.getWidth(); x++) {
                resultValues[x][y] = true;
                for (int my = 0; my < matrix.getHeight(); my++) {
                    for (int mx = 0; mx < matrix.getWidth(); mx++) {
                        if (!matrixValues[mx][my]) {
                            continue;
                        }

                        int ix = x + mx + offsetX;
                        if (ix < 0 || ix >= dto.getWidth()) {
                            continue;
                        }
                        int iy = y + my + offsetY;
                        if (iy < 0 || iy >= dto.getHeight()) {
                            continue;
                        }

                        if (!origValues[ix][iy]) {
                            resultValues[x][y] = false;
                            continue pixelLoop;
                        }
                    }
                }
            }
        }
        return resultDto;
    }

    public WritableImage opening(WritableImage image, MorphologyMatrix matrix) {
        return dtoToImage(dilation(erosion(imageToDto(image), matrix), matrix));
    }

    public WritableImage closing(WritableImage image, MorphologyMatrix matrix) {
        return dtoToImage(erosion(dilation(imageToDto(image), matrix), matrix));
    }

    private BinaryImageDTO copyNegated(BinaryImageDTO dto) {
        boolean[][] values = dto.getValues();

        BinaryImageDTO resultDto = new BinaryImageDTO(dto.getWidth(), dto.getHeight());
        boolean[][] resultValues = resultDto.getValues();

        for (int y = 0; y < dto.getHeight(); y++) {
            for (int x = 0; x < dto.getWidth(); x++) {
                resultValues[x][y] = !values[x][y];
            }
        }

        return resultDto;
    }

    public WritableImage hitOrMiss(WritableImage image, MorphologyMatrix... matrices) {
        BinaryImageDTO imageDTO = imageToDto(image);

        imageDTO = hitOrMiss(imageDTO, matrices);

        return dtoToImage(imageDTO);
    }

    private BinaryImageDTO hitOrMiss(BinaryImageDTO imageDTO, MorphologyMatrix... matrices) {
        int changedPixels = 0;
        int current = 0;
        BinaryImageDTO erosionResult, dilationResult;
        do {
            erosionResult = erosion(imageDTO, matrices[current]);
            dilationResult = erosion(copyNegated(imageDTO), matrices[current + 1]);

            changedPixels = 0;
            BinaryImageDTO resultDTO = new BinaryImageDTO(imageDTO.getWidth(), imageDTO.getHeight());
            boolean[][] resultValues = resultDTO.getValues();
            boolean[][] erosionValues = erosionResult.getValues();
            boolean[][] dilationValues = dilationResult.getValues();
            boolean[][] origValues = imageDTO.getValues();
            for (int y = 0; y < imageDTO.getHeight(); y++) {
                for (int x = 0; x < imageDTO.getWidth(); x++) {
                    resultValues[x][y] = erosionValues[x][y] && dilationValues[x][y];
                    if (origValues[x][y] != resultValues[x][y]) {
                        changedPixels++;
                    }
                }
            }

            imageDTO = resultDTO;
            System.out.println("Changed pixels: " + changedPixels);

            current = (current + 2) % matrices.length;
        } while (changedPixels != 0);
        return imageDTO;
    }
}
