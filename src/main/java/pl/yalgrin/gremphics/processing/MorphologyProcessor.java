package pl.yalgrin.gremphics.processing;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class MorphologyProcessor {
    private static final MorphologyProcessor instance = new MorphologyProcessor();

    private MorphologyMatrix[] thinningMatrices = new MorphologyMatrix[16];
    private MorphologyMatrix[] thickeningMatrices = new MorphologyMatrix[16];

    private MorphologyProcessor() {
        thinningMatrices[0] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 1}});
        thinningMatrices[1] = new MorphologyMatrix(new int[][]{{1, 1, 1}, {0, 0, 0}, {0, 0, 0}});
        thinningMatrices[4] = new MorphologyMatrix(new int[][]{{1, 0, 0}, {1, 1, 0}, {1, 0, 0}});
        thinningMatrices[5] = new MorphologyMatrix(new int[][]{{0, 0, 1}, {0, 0, 1}, {0, 0, 1}});
        thinningMatrices[8] = new MorphologyMatrix(new int[][]{{1, 1, 1}, {0, 1, 0}, {0, 0, 0}});
        thinningMatrices[9] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 0, 0}, {1, 1, 1}});
        thinningMatrices[12] = new MorphologyMatrix(new int[][]{{0, 0, 1}, {0, 1, 1}, {0, 0, 1}});
        thinningMatrices[13] = new MorphologyMatrix(new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 0, 0}});

        thinningMatrices[2] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {1, 1, 0}, {0, 1, 0}});
        thinningMatrices[3] = new MorphologyMatrix(new int[][]{{0, 1, 1}, {0, 0, 1}, {0, 0, 0}});
        thinningMatrices[6] = new MorphologyMatrix(new int[][]{{0, 1, 0}, {1, 1, 0}, {0, 0, 0}});
        thinningMatrices[7] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 0, 1}, {0, 1, 1}});
        thinningMatrices[10] = new MorphologyMatrix(new int[][]{{0, 1, 0}, {0, 1, 1}, {0, 0, 0}});
        thinningMatrices[11] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {1, 0, 0}, {1, 1, 0}});
        thinningMatrices[14] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 1, 1}, {0, 1, 0}});
        thinningMatrices[15] = new MorphologyMatrix(new int[][]{{1, 1, 0}, {1, 0, 0}, {0, 0, 0}});

        thickeningMatrices[0] = new MorphologyMatrix(new int[][]{{1, 1, 0}, {1, 0, 0}, {1, 0, 0}});
        thickeningMatrices[1] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        thickeningMatrices[4] = new MorphologyMatrix(new int[][]{{1, 1, 1}, {0, 0, 1}, {0, 0, 0}});
        thickeningMatrices[5] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 0, 0}});
        thickeningMatrices[8] = new MorphologyMatrix(new int[][]{{0, 0, 1}, {0, 0, 1}, {0, 1, 1}});
        thickeningMatrices[9] = new MorphologyMatrix(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        thickeningMatrices[12] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {1, 0, 0}, {1, 1, 1}});
        thickeningMatrices[13] = new MorphologyMatrix(new int[][]{{0, 0, 1}, {0, 1, 0}, {0, 0, 0}});

        thickeningMatrices[2] = new MorphologyMatrix(new int[][]{{0, 1, 1}, {0, 0, 1}, {0, 0, 1}});
        thickeningMatrices[3] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 0, 0}});
        thickeningMatrices[6] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 0, 1}, {1, 1, 1}});
        thickeningMatrices[7] = new MorphologyMatrix(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        thickeningMatrices[10] = new MorphologyMatrix(new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 0}});
        thickeningMatrices[11] = new MorphologyMatrix(new int[][]{{0, 0, 1}, {0, 1, 0}, {0, 0, 0}});
        thickeningMatrices[14] = new MorphologyMatrix(new int[][]{{1, 1, 1}, {1, 0, 0}, {0, 0, 0}});
        thickeningMatrices[15] = new MorphologyMatrix(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 1}});
    }

    public static MorphologyProcessor getInstance() {
        return instance;
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
        boolean foundValidValues;
        for (int y = 0; y < dto.getHeight(); y++) {
            pixelLoop:
            for (int x = 0; x < dto.getWidth(); x++) {
                foundValidValues = false;
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

                        foundValidValues = true;
                        if (!origValues[ix][iy]) {
                            resultValues[x][y] = false;
                            continue pixelLoop;
                        }
                    }
                }
                if (!foundValidValues) {
                    resultValues[x][y] = false;
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

    private BinaryImageDTO sum(BinaryImageDTO... dtos) {
        BinaryImageDTO resultDto = new BinaryImageDTO(dtos[0].getWidth(), dtos[0].getHeight());
        boolean[][] resultValues = resultDto.getValues();

        for (int y = 0; y < dtos[0].getHeight(); y++) {
            for (int x = 0; x < dtos[0].getWidth(); x++) {
                for (BinaryImageDTO dto : dtos) {
                    if (dto.getValues()[x][y]) {
                        resultValues[x][y] = true;
                        break;
                    }
                }
            }
        }

        return resultDto;
    }

    private BinaryImageDTO subtract(BinaryImageDTO dto1, BinaryImageDTO dto2) {
        BinaryImageDTO resultDto = new BinaryImageDTO(dto1.getWidth(), dto1.getHeight());
        boolean[][] resultValues = resultDto.getValues();
        boolean[][] dto1Values = dto1.getValues();
        boolean[][] dto2Values = dto2.getValues();

        for (int y = 0; y < dto1.getHeight(); y++) {
            for (int x = 0; x < dto1.getWidth(); x++) {
                if (!dto1Values[x][y]) {
                    continue;
                }
                resultValues[x][y] = !dto2Values[x][y];
            }
        }

        return resultDto;
    }

    private BinaryImageDTO multiply(BinaryImageDTO... dtos) {
        BinaryImageDTO resultDto = new BinaryImageDTO(dtos[0].getWidth(), dtos[0].getHeight());
        boolean[][] resultValues = resultDto.getValues();

        for (int y = 0; y < dtos[0].getHeight(); y++) {
            for (int x = 0; x < dtos[0].getWidth(); x++) {
                resultValues[x][y] = true;
                for (BinaryImageDTO dto : dtos) {
                    if (!dto.getValues()[x][y]) {
                        resultValues[x][y] = false;
                        break;
                    }
                }
            }
        }

        return resultDto;
    }

    private int countChangedPixels(BinaryImageDTO dto1, BinaryImageDTO dto2) {
        int changedPixels = 0;
        boolean[][] origValues = dto1.getValues();
        boolean[][] resultValues = dto2.getValues();
        for (int y = 0; y < dto1.getHeight(); y++) {
            for (int x = 0; x < dto1.getWidth(); x++) {
                if (origValues[x][y] != resultValues[x][y]) {
                    changedPixels++;
                }
            }
        }
        return changedPixels;
    }

    public WritableImage thinning(WritableImage image) {
        BinaryImageDTO imageDTO = imageToDto(image);

        BinaryImageDTO resultDTO = imageDTO;
        int countChanged;
        do {
            resultDTO = subtract(resultDTO, hitOrMiss(resultDTO, thinningMatrices[0], thinningMatrices[1], thinningMatrices[2], thinningMatrices[3]));
            resultDTO = subtract(resultDTO, hitOrMiss(resultDTO, thinningMatrices[4], thinningMatrices[5], thinningMatrices[6], thinningMatrices[7]));
            resultDTO = subtract(resultDTO, hitOrMiss(resultDTO, thinningMatrices[8], thinningMatrices[9], thinningMatrices[10], thinningMatrices[11]));
            resultDTO = subtract(resultDTO, hitOrMiss(resultDTO, thinningMatrices[12], thinningMatrices[13], thinningMatrices[14], thinningMatrices[15]));
            countChanged = countChangedPixels(imageDTO, resultDTO);
            System.out.println("CHANGED PIXELS: " + countChanged);
            imageDTO = resultDTO;
        } while (countChanged > 0);

        return dtoToImage(imageDTO);
    }

    public WritableImage thickening(WritableImage image) {
        BinaryImageDTO imageDTO = imageToDto(image);

        BinaryImageDTO resultDTO = imageDTO;
        int countChanged;
        do {
            resultDTO = sum(resultDTO, hitOrMiss(resultDTO, thickeningMatrices[0], thickeningMatrices[1], thickeningMatrices[2], thickeningMatrices[3]));
            resultDTO = sum(resultDTO, hitOrMiss(resultDTO, thickeningMatrices[4], thickeningMatrices[5], thickeningMatrices[6], thickeningMatrices[7]));
            resultDTO = sum(resultDTO, hitOrMiss(resultDTO, thickeningMatrices[8], thickeningMatrices[9], thickeningMatrices[10], thickeningMatrices[11]));
            resultDTO = sum(resultDTO, hitOrMiss(resultDTO, thickeningMatrices[12], thickeningMatrices[13], thickeningMatrices[14], thickeningMatrices[15]));
            countChanged = countChangedPixels(imageDTO, resultDTO);
            System.out.println("CHANGED PIXELS: " + countChanged);
            imageDTO = resultDTO;
        } while (countChanged > 0);

        return dtoToImage(imageDTO);
    }

    private BinaryImageDTO hitOrMiss(BinaryImageDTO imageDTO, MorphologyMatrix... matrices) {
        BinaryImageDTO[] results = new BinaryImageDTO[matrices.length / 2];
        BinaryImageDTO firstResult, secondResult;
        for (int i = 0; i < matrices.length; i += 2) {
            firstResult = erosion(imageDTO, matrices[i]);
            secondResult = erosion(copyNegated(imageDTO), matrices[i + 1]);
            results[i / 2] = multiply(firstResult, secondResult);
        }
        return sum(results);
    }
}
