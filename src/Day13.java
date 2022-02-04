import common.FileUtils;

import java.util.ArrayList;
import java.util.List;

class FoldInstruction {
    String direction;
    int index;

    public FoldInstruction(String direction, int index) {
        this.direction = direction;
        this.index = index;
    }
}

public class Day13 {
    private static Coordinate getMaxXYCoordinate(List<Coordinate> coordinates) {
        int maxXCoordinate = 0;
        int maxYCoordinate = 0;

        for (Coordinate c: coordinates) {
            maxXCoordinate = Math.max(maxXCoordinate, c.x);
            maxYCoordinate = Math.max(maxYCoordinate, c.y);
        }

        return new Coordinate(maxXCoordinate, maxYCoordinate);
    }

    private static boolean[][] foldUp(boolean[][] dots, int index) {
        boolean[][] result = new boolean[index][dots[0].length];

        for (int i = 0; i < result.length; i++) {
            int overlappedRow = 2 * (index - i) + i;
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = dots[i][j];
                if (overlappedRow < dots.length) {
                    result[i][j] = result[i][j] || dots[overlappedRow][j];
                }
            }
        }

        return result;
    }

    private static boolean[][] foldLeft(boolean[][] dots, int index) {
        boolean[][] result = new boolean[dots.length][index];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = dots[i][j];
                int overlappedCol = 2 * (index - j) + j;
                if (overlappedCol < dots[i].length) {
                    result[i][j] = result[i][j] || dots[i][overlappedCol];
                }
            }
        }

        return result;
    }

    private static void printDots(boolean[][] dots) {
        for (int i = 0; i < dots.length; i++) {
            for (int j = 0; j < dots[i].length; j++) {
                if (dots[i][j]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    private static int countDots(boolean[][] dots) {
        int result = 0;
        for (int i = 0; i < dots.length; i++) {
            for (int j = 0; j < dots[i].length; j++) {
                if (dots[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    private static boolean[][] buildDotMap(List<Coordinate> coordinates) {
        Coordinate maxCoordinates = getMaxXYCoordinate(coordinates);
        boolean[][] dots = new boolean[maxCoordinates.y + 1][maxCoordinates.x + 1];
        for (Coordinate c: coordinates) {
            dots[c.y][c.x] = true;
        }
        return dots;
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day13.txt");
        List<Coordinate> coordinates = new ArrayList<>();
        List<FoldInstruction> foldInstructions = new ArrayList<>();

        for (String inputLine: inputLines) {
            if (!inputLine.isEmpty()) {
                if (!inputLine.startsWith("fold")) {
                    String[] points = inputLine.split(",");
                    coordinates.add(new Coordinate(Integer.parseInt(points[0]), Integer.parseInt(points[1])));
                } else {
                    String[] instructions = inputLine.split("=");
                    int index = Integer.parseInt(instructions[1]);
                    String direction = instructions[0].endsWith("x") ? "left" : "up";
                    foldInstructions.add(new FoldInstruction(direction, index));
                }
            }
        }

        // Day13_1
        boolean[][] dots = buildDotMap(coordinates);
        FoldInstruction f = foldInstructions.get(0);
        if ("up".equals(f.direction)) {
            dots = foldUp(dots, f.index);
        } else {
            dots = foldLeft(dots, f.index);
        }
        System.out.println(countDots(dots));

        // Day13_2
        boolean[][] dots2 = buildDotMap(coordinates);
        for (FoldInstruction f2: foldInstructions) {
            if ("up".equals(f2.direction)) {
                dots2 = foldUp(dots2, f2.index);
            } else {
                dots2 = foldLeft(dots2, f2.index);
            }
        }
        printDots(dots2);
    }
}
