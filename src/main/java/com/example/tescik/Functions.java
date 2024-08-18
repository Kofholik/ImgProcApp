package com.example.tescik;

import javafx.scene.image.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Functions {
    //Funckja odejmujaca 2 obrazy od siebie
    public static Image subtractImages(Image originalImage, Image subtractedImage) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage resultImage = new WritableImage(width, height);
        PixelReader originalReader = originalImage.getPixelReader();
        PixelReader subtractedReader = subtractedImage.getPixelReader();
        PixelWriter writer = resultImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalArgb = originalReader.getArgb(x, y);
                int subtractedArgb = subtractedReader.getArgb(x, y);

                int originalGray = (int) (0.2126 * ((originalArgb >> 16) & 0xFF) + 0.7152 * ((originalArgb >> 8) & 0xFF) + 0.0722 * (originalArgb & 0xFF));
                int subtractedGray = (int) (0.2126 * ((subtractedArgb >> 16) & 0xFF) + 0.7152 * ((subtractedArgb >> 8) & 0xFF) + 0.0722 * (subtractedArgb & 0xFF));

                int difference = originalGray - subtractedGray;
                difference = Math.max(0, difference);
                writer.setArgb(x, y, (0xFF << 24) | (difference << 16) | (difference << 8) | difference);
            }
        }

        return resultImage;
    }

    // Funkcja filtrująca obraz przy pomocy mediany
    public static Image medianFilter(Image ImageInput) {
        int width = (int) ImageInput.getWidth();
        int height = (int) ImageInput.getHeight();

        WritableImage ImageOutput = new WritableImage(width, height);
        PixelReader pixelReader = ImageInput.getPixelReader();
        PixelWriter pixelWriter = ImageOutput.getPixelWriter();

        final int maskSize = 3; // Rozmiar maski filtra

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixels = new int[maskSize * maskSize];
                int pixelIndex = 0;
                for (int dy = -maskSize / 2; dy <= maskSize / 2; dy++) {
                    for (int dx = -maskSize / 2; dx <= maskSize / 2; dx++) {
                        int pixelX = x + dx;
                        int pixelY = y + dy;
                        if (pixelX >= 0 && pixelX < width && pixelY >= 0 && pixelY < height) {
                            pixels[pixelIndex++] = pixelReader.getArgb(pixelX, pixelY);
                        }
                    }
                }

                Arrays.sort(pixels);
                int medianValue = pixels[maskSize * maskSize / 2];
                pixelWriter.setArgb(x, y, medianValue);
            }
        }

        return ImageOutput;
    }

    //Funkcja filtrująca obraz przy pomocy metody minimalnej
    public static Image minFilter(Image ImageInput) {
        int width = (int) ImageInput.getWidth();
        int height = (int) ImageInput.getHeight();

        WritableImage ImageOutput = new WritableImage(width, height);
        PixelReader pixelReader = ImageInput.getPixelReader();
        PixelWriter pixelWriter = ImageOutput.getPixelWriter();

        final int maskSize = 3;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixels = new int[maskSize * maskSize];
                int pixelIndex = 0;
                for (int dy = -maskSize / 2; dy <= maskSize / 2; dy++) {
                    for (int dx = -maskSize / 2; dx <= maskSize / 2; dx++) {
                        int pixelX = x + dx;
                        int pixelY = y + dy;
                        if (pixelX >= 0 && pixelX < width && pixelY >= 0 && pixelY < height) {
                            pixels[pixelIndex++] = pixelReader.getArgb(pixelX, pixelY);
                        }
                    }
                }
                int minValue = Integer.MAX_VALUE;
                for (int pixelValue : pixels) {
                    minValue = Math.min(minValue, pixelValue);
                }
                pixelWriter.setArgb(x, y, minValue);
            }
        }

        return ImageOutput;
    }

    //Funkcja filtrująca obraz przy pomocy metody maksymalnej
    public static Image maxFilter(Image ImageInput) {
        int width = (int) ImageInput.getWidth();
        int height = (int) ImageInput.getHeight();
        WritableImage ImageOutput = new WritableImage(width, height);

        PixelReader pixelReader = ImageInput.getPixelReader();
        PixelWriter pixelWriter = ImageOutput.getPixelWriter();

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int[] redNeighborhood = new int[9];
                int[] greenNeighborhood = new int[9];
                int[] blueNeighborhood = new int[9];
                int index = 0;

                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int argb = pixelReader.getArgb(x + dx, y + dy);
                        int red = (argb >> 16) & 0xFF;
                        int green = (argb >> 8) & 0xFF;
                        int blue = argb & 0xFF;

                        redNeighborhood[index] = red;
                        greenNeighborhood[index] = green;
                        blueNeighborhood[index] = blue;
                        index++;
                    }
                }

                int maxRed = Arrays.stream(redNeighborhood).max().getAsInt();
                int maxGreen = Arrays.stream(greenNeighborhood).max().getAsInt();
                int maxBlue = Arrays.stream(blueNeighborhood).max().getAsInt();

                int maxArgb = (0xFF << 24) | (maxRed << 16) | (maxGreen << 8) | maxBlue;
                pixelWriter.setArgb(x, y, maxArgb);
            }
        }

        return ImageOutput;
    }

    // Funckja konwertująca image na mat
    public static Mat imageToMat(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        Mat mat = new Mat(height, width, CvType.CV_8UC4);

        PixelReader pixelReader = image.getPixelReader();
        byte[] buffer = new byte[width * height * 4];
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        pixelReader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);

        mat.put(0, 0, buffer);
        Mat matBGR = new Mat();
        org.opencv.imgproc.Imgproc.cvtColor(mat, matBGR, org.opencv.imgproc.Imgproc.COLOR_BGRA2BGR);

        return matBGR;
    }

    // Funkcja która binaryzuje obraz za pomocą pętli
    public static Image binarizeImage(Image ImageInput, int value) {
        int width = (int) ImageInput.getWidth();
        int height = (int) ImageInput.getHeight();

        WritableImage outputImage = new WritableImage(width, height);
        PixelReader pixelReader = ImageInput.getPixelReader();
        PixelWriter pixelWriter = outputImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixelReader.getArgb(x, y);
                int gray = (int) (0.2126 * ((argb >> 16) & 0xFF) + 0.7152 * ((argb >> 8) & 0xFF) + 0.0722 * (argb & 0xFF));
                int binaryColor = (gray >= value) ? 0xFFFFFFFF : 0xFF000000;
                pixelWriter.setArgb(x, y, binaryColor);
            }
        }

        return outputImage;

    }
    //Funkcja dla krawedzi poziomej
    public static Image HoriEdgeDetection(Image ImageInput) {
        int width = (int) ImageInput.getWidth();
        int height = (int) ImageInput.getHeight();

        WritableImage ImageOutput = new WritableImage(width, height);
        PixelReader pixelReader = ImageInput.getPixelReader();
        PixelWriter pixelWriter = ImageOutput.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
                int argbCurrent = pixelReader.getArgb(x, y);
                int argbRight = pixelReader.getArgb(x + 1, y);

                int grayCurrent = (int) (0.2126 * ((argbCurrent >> 16) & 0xFF) + 0.7152 * ((argbCurrent >> 8) & 0xFF) + 0.0722 * (argbCurrent & 0xFF));
                int grayRight = (int) (0.2126 * ((argbRight >> 16) & 0xFF) + 0.7152 * ((argbRight >> 8) & 0xFF) + 0.0722 * (argbRight & 0xFF));

                int difference = Math.abs(grayCurrent - grayRight);

                int edgeColor = (0xFF << 24) | (difference << 16) | (difference << 8) | difference;
                pixelWriter.setArgb(x, y, edgeColor);
            }
        }

        return ImageOutput;
    }

    //Funkcja dla krawedzi pionowej
    public static Image vertEdgeDetection(Image ImageInput) {
        int width = (int) ImageInput.getWidth();
        int height = (int) ImageInput.getHeight();

        WritableImage ImageOutput = new WritableImage(width, height);
        PixelReader pixelReader = ImageInput.getPixelReader();
        PixelWriter pixelWriter = ImageOutput.getPixelWriter();

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                int argbCurrent = pixelReader.getArgb(x, y);
                int argbBelow = pixelReader.getArgb(x, y + 1);

                int grayCurrent = (int) (0.2126 * ((argbCurrent >> 16) & 0xFF) + 0.7152 * ((argbCurrent >> 8) & 0xFF) + 0.0722 * (argbCurrent & 0xFF));
                int grayBelow = (int) (0.2126 * ((argbBelow >> 16) & 0xFF) + 0.7152 * ((argbBelow >> 8) & 0xFF) + 0.0722 * (argbBelow & 0xFF));

                int difference = Math.abs(grayCurrent - grayBelow);

                int edgeColor = (0xFF << 24) | (difference << 16) | (difference << 8) | difference;
                pixelWriter.setArgb(x, y, edgeColor);
            }
        }

        return ImageOutput;
    }

    // Funkcja dla krawędzi ukośnej
    public static Image diagEdgeDetection(Image ImageInput) {
        int width = (int) ImageInput.getWidth();
        int height = (int) ImageInput.getHeight();

        WritableImage ImageOutput = new WritableImage(width, height);
        PixelReader pixelReader = ImageInput.getPixelReader();
        PixelWriter pixelWriter = ImageOutput.getPixelWriter();

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                int argbCurrent = pixelReader.getArgb(x, y);
                int argbDiagonal = pixelReader.getArgb(x + 1, y + 1);

                int grayCurrent = (int) (0.2126 * ((argbCurrent >> 16) & 0xFF) + 0.7152 * ((argbCurrent >> 8) & 0xFF) + 0.0722 * (argbCurrent & 0xFF));
                int grayDiagonal = (int) (0.2126 * ((argbDiagonal >> 16) & 0xFF) + 0.7152 * ((argbDiagonal >> 8) & 0xFF) + 0.0722 * (argbDiagonal & 0xFF));

                int difference = Math.abs(grayCurrent - grayDiagonal);

                int edgeColor = (0xFF << 24) | (difference << 16) | (difference << 8) | difference;
                pixelWriter.setArgb(x, y, edgeColor);
            }
        }

        return ImageOutput;
    }
    //Funkcja łącząca obrazy ze wszystkich krawędzi
    public static Image AllEdgeDetection(Image ImageInput) {
        Image horizontalEdges = HoriEdgeDetection(ImageInput);
        Image verticalEdges = vertEdgeDetection(ImageInput);
        Image diagonalEdges = diagEdgeDetection(ImageInput);

        int width = (int) ImageInput.getWidth();
        int height = (int) ImageInput.getHeight();

        WritableImage ImageOutput = new WritableImage(width, height);
        PixelReader horiReader = horizontalEdges.getPixelReader();
        PixelReader vertReader = verticalEdges.getPixelReader();
        PixelReader diagReader = diagonalEdges.getPixelReader();
        PixelWriter pixelWriter = ImageOutput.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int horiArgb = horiReader.getArgb(x, y);
                int vertArgb = vertReader.getArgb(x, y);
                int diagArgb = diagReader.getArgb(x, y);

                int horiGray = (horiArgb >> 16) & 0xFF;
                int vertGray = (vertArgb >> 16) & 0xFF;
                int diagGray = (diagArgb >> 16) & 0xFF;

                int combinedGray = Math.min(255, horiGray + vertGray + diagGray);

                int edgeColor = (0xFF << 24) | (combinedGray << 16) | (combinedGray << 8) | combinedGray;
                pixelWriter.setArgb(x, y, edgeColor);
            }
        }

        return ImageOutput;
    }
    //Funkcja filtru górnoprzepustowego
    public static Image applyHighPassFilter(Image imageInput) {
        int width = (int) imageInput.getWidth();
        int height = (int) imageInput.getHeight();

        WritableImage resultImage = new WritableImage(width, height);
        PixelReader pixelReader = imageInput.getPixelReader();
        PixelWriter pixelWriter = resultImage.getPixelWriter();

        final int maskSize = 3; // Rozmiar maski filtra (np. 3x3)
        final int[][] kernel = {{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}}; // Maska filtra

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // Przygotuj macierz wartości pikseli wokół piksela (maska filtra)
                int sumR = 0;
                int sumG = 0;
                int sumB = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int pixelX = x + dx;
                        int pixelY = y + dy;
                        int argb = pixelReader.getArgb(pixelX, pixelY);
                        int kernelValue = kernel[dy + 1][dx + 1];
                        sumR += ((argb >> 16) & 0xFF) * kernelValue;
                        sumG += ((argb >> 8) & 0xFF) * kernelValue;
                        sumB += (argb & 0xFF) * kernelValue;
                    }
                }

                // Oblicz nową wartość piksela na podstawie sumy wartości z maski filtra
                int newArgb = (0xFF << 24) |
                        (clamp(sumR) << 16) |
                        (clamp(sumG) << 8) |
                        clamp(sumB);
                pixelWriter.setArgb(x, y, newArgb);
            }
        }

        return resultImage;
    }
    //Funkcja filtru dolnoprzepustowego
    public static Image applyLowPassFilter(Image imageInput) {
        int width = (int) imageInput.getWidth();
        int height = (int) imageInput.getHeight();

        WritableImage resultImage = new WritableImage(width, height);
        PixelReader pixelReader = imageInput.getPixelReader();
        PixelWriter pixelWriter = resultImage.getPixelWriter();

        final int maskSize = 3; // Rozmiar maski filtra (np. 3x3)
        final double[][] kernel = {{1.0/9, 1.0/9, 1.0/9}, {1.0/9, 1.0/9, 1.0/9}, {1.0/9, 1.0/9, 1.0/9}}; // Maska filtra

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // Przygotuj macierz wartości pikseli wokół piksela (maska filtra)
                double sumR = 0;
                double sumG = 0;
                double sumB = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int pixelX = x + dx;
                        int pixelY = y + dy;
                        int argb = pixelReader.getArgb(pixelX, pixelY);
                        double kernelValue = kernel[dy + 1][dx + 1];
                        sumR += ((argb >> 16) & 0xFF) * kernelValue;
                        sumG += ((argb >> 8) & 0xFF) * kernelValue;
                        sumB += (argb & 0xFF) * kernelValue;
                    }
                }

                // Oblicz nową wartość piksela na podstawie sumy wartości z maski filtra
                int newArgb = (0xFF << 24) |
                        (clamp((int) sumR) << 16) |
                        (clamp((int) sumG) << 8) |
                        clamp((int) sumB);
                pixelWriter.setArgb(x, y, newArgb);
            }
        }

        return resultImage;
    }
    //Funkcja pomocnicza dla filtrów liniowych
    private static int clamp(int value) {
        return Math.max(0, Math.min(value, 255));
    }


}
