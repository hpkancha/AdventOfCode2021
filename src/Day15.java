import common.FileUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day15 {
    private static int getMinRiskLevel(int[][] riskLevels) {
        int[][] minRiskLevels = new int[riskLevels.length][riskLevels[0].length];
        for (int i = 0; i < minRiskLevels.length; i++) {
            for (int j = 0; j < minRiskLevels[i].length; j++) {
                if (i == 0 && j == 0) {
                    minRiskLevels[i][j] = 0;
                } else if (i == 0 && j != 0) {
                    minRiskLevels[i][j] = riskLevels[i][j] + minRiskLevels[i][j - 1];
                } else if (i != 0 && j == 0) {
                    minRiskLevels[i][j] = riskLevels[i][j] + minRiskLevels[i - 1][j];
                } else {
                    minRiskLevels[i][j] = riskLevels[i][j] + Math.min(minRiskLevels[i - 1][j], minRiskLevels[i][j - 1]);
                }
            }
        }
        return minRiskLevels[riskLevels.length - 1][riskLevels[0].length - 1];
    }

    private static void updateIndicesAndMinRiskLevels(Coordinate currentIndex, Coordinate nextIndex,
                                                      int[][] riskLevels, int[][] minRiskLevels, Queue<Coordinate> indices) {
        int nextIndexMinRiskLevel = minRiskLevels[nextIndex.x][nextIndex.y];
        int nextIndexRiskLevel = riskLevels[nextIndex.x][nextIndex.y];
        int currIndexMinRiskLevel = minRiskLevels[currentIndex.x][currentIndex.y];
        if (nextIndexMinRiskLevel == 0 || nextIndexRiskLevel + currIndexMinRiskLevel < nextIndexMinRiskLevel) {
            minRiskLevels[nextIndex.x][nextIndex.y] = nextIndexRiskLevel + currIndexMinRiskLevel;
            indices.add(nextIndex);
        }
    }

    private static int getMinRiskLevelNew(int[][] riskLevels) {
        int[][] minRiskLevels = new int[riskLevels.length][riskLevels[0].length];
        Queue<Coordinate> indices = new LinkedList<>();
        indices.add(new Coordinate(0, 0));

        while (!indices.isEmpty()) {
            Coordinate c = indices.poll();
            int i = c.x;
            int j = c.y;

            // Move Down
            if (i < minRiskLevels.length - 1) {
                updateIndicesAndMinRiskLevels(c, new Coordinate(i + 1, j), riskLevels, minRiskLevels, indices);
            }
            // Move Right
            if (j < minRiskLevels[i].length - 1) {
                updateIndicesAndMinRiskLevels(c, new Coordinate(i, j + 1), riskLevels, minRiskLevels, indices);
            }
            // Move Up
            if (i > 0) {
                updateIndicesAndMinRiskLevels(c, new Coordinate(i - 1, j), riskLevels, minRiskLevels, indices);
            }
            // Move Left
            if (j > 0) {
                updateIndicesAndMinRiskLevels(c, new Coordinate(i, j - 1), riskLevels, minRiskLevels, indices);
            }
        }
        return minRiskLevels[riskLevels.length - 1][riskLevels[0].length - 1];
    }

    private static int[][] getRiskLevels(List<String> inputLines, int rowLength, int colLength) {
        int[][] riskLevels = new int[rowLength][colLength];
        int row = 0;
        for (String inputLine : inputLines) {
            int col = 0;
            for (char c : inputLine.toCharArray()) {
                riskLevels[row][col] = c - '0';
                col++;
            }
            row++;
        }
        return riskLevels;
    }

    private static int getRiskLevel(int leftOrUpTileRiskLevel) {
        return leftOrUpTileRiskLevel + 1 > 9 ? 1 : leftOrUpTileRiskLevel + 1;
    }

    private static void fillFullRiskLevels(int[][] fullRiskLevels) {
        int tileRowLength = fullRiskLevels.length / 5;
        int tileColLength = fullRiskLevels[0].length / 5;
        for (int tileRowPointer = 0; tileRowPointer < tileRowLength; tileRowPointer++) {
            for (int tileColPointer = 0; tileColPointer < tileColLength; tileColPointer++) {
                for (int i = tileRowPointer; i < fullRiskLevels.length; i = i + tileRowLength) {
                    for (int j = tileColPointer; j < fullRiskLevels[i].length; j = j + tileColLength) {
                        if (i - tileRowLength >= 0) {
                            fullRiskLevels[i][j] = getRiskLevel(fullRiskLevels[i - tileRowLength][j]);
                        } else if (j - tileColLength >= 0) {
                            fullRiskLevels[i][j] = getRiskLevel(fullRiskLevels[i][j - tileColLength]);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day15.txt");

        // Day 15_1
        int[][] riskLevels = getRiskLevels(inputLines, inputLines.size(), inputLines.get(0).length());
        System.out.println(getMinRiskLevelNew(riskLevels));

        // Day 15_2
        int[][] fullRiskLevels = getRiskLevels(inputLines, inputLines.size() * 5, inputLines.get(0).length() * 5);
        fillFullRiskLevels(fullRiskLevels);
        System.out.println(getMinRiskLevelNew(fullRiskLevels));
    }
}
