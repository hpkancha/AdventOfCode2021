import common.FileUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

enum Pixel {
    LIGHT('#'),
    DARK('.');

    char c;

    Pixel(char c) {
        this.c = c;
    }

    public Pixel getPixel(char c) {
        return Arrays.stream(Pixel.values()).filter(p -> p.c == c).collect(Collectors.toList()).get(0);
    }
}

public class Day20 {
    private static char getEnhancedPixel(char[][] image, String imageEnhancementAlgorithm, int row, int col) {
        StringBuilder sb = new StringBuilder();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                sb.append(image[row + i][col + j]);
            }
        }

        String binaryIndex = sb.toString().replace('.', '0').replace('#', '1');
        int decimalIndex = Integer.parseInt(binaryIndex, 2);

        return imageEnhancementAlgorithm.charAt(decimalIndex);
    }

    private static char[][] enhanceImage(char[][] image, String imageEnhancementAlgorithm, char outerPixel) {
        char[][] expandedImage = expandImage(image, outerPixel);
        char[][] enhancedImage = new char[expandedImage.length][expandedImage[0].length];

        for (int i = 0; i < expandedImage.length; i++) {
            for (int j = 0; j < expandedImage[i].length; j++) {
                if (i == 0 || j == 0 || i == expandedImage.length - 1 || j == expandedImage[i].length - 1) {
                    enhancedImage[i][j] = outerPixel;
                } else {
                    enhancedImage[i][j] = getEnhancedPixel(expandedImage, imageEnhancementAlgorithm, i, j);
                }
            }
        }

        return trimImage(enhancedImage);
    }

    private static char[][] trimImage(char[][] image) {
        char[][] newImage = new char[image.length - 2][image[0].length - 2];

        for (int i = 0; i < newImage.length; i++) {
            for (int j = 0; j < newImage[i].length; j++) {
                    newImage[i][j] = image[i + 1][j + 1];
            }
        }

        return newImage;
    }

    private static char[][] expandImage(char[][] image, char outerPixel) {
        char[][] newImage = new char[image.length + 4][image[0].length + 4];

        for (int i = 0; i < newImage.length; i++) {
            for (int j = 0; j < newImage[i].length; j++) {
                if (i < 2 || j < 2 || i > newImage.length - 3 || j > newImage[i].length - 3) {
                    newImage[i][j] = outerPixel;
                } else {
                    newImage[i][j] = image[i - 2][j - 2];
                }
            }
        }

        return newImage;
    }

    private static char[][] parseImage(List<String> inputLines) {
        inputLines.remove(0);
        inputLines.remove(0);

        char[][] image = new char[inputLines.size()][];

        int rowNum = 0;
        for (String inputLine: inputLines) {
            image[rowNum] = inputLine.toCharArray();
            rowNum++;
        }

        return image;
    }

    private static int getLitPixelCount(char[][] image) {
        int litPixels = 0;
        for (char[] row : image) {
            for (char c: row) {
                if (c == '#') {
                    litPixels++;
                }
            }
        }

        return litPixels;
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day20.txt");
        String imageEnhancementAlgorithm = inputLines.get(0);
        char[][] image = parseImage(inputLines);

        // Day20_1
        for (int i = 1; i <= 2; i++) {
            char outerPixel = '.';
            if (i % 2 == 0 && imageEnhancementAlgorithm.charAt(0) == '#' && imageEnhancementAlgorithm.charAt(511) == '.') {
                outerPixel = '#';
            }
            image = enhanceImage(image, imageEnhancementAlgorithm, outerPixel);
        }
        System.out.println(getLitPixelCount(image));

        // Day20_2
        for (int i = 1; i <= 48; i++) {
            char outerPixel = '.';
            if (i % 2 == 0 && imageEnhancementAlgorithm.charAt(0) == '#' && imageEnhancementAlgorithm.charAt(511) == '.') {
                outerPixel = '#';
            }
            image = enhanceImage(image, imageEnhancementAlgorithm, outerPixel);
        }
        System.out.println(getLitPixelCount(image));
    }
}
