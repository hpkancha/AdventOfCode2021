import common.FileUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {
    private static void updateEnergies(int[][] energylevels, int row, int  col) {
        if (row < 0 || row > energylevels.length - 1 || col < 0 || col > energylevels[0].length - 1 || energylevels[row][col] == 0) {
            return;
        }

        if (energylevels[row][col] <= 9) {
            energylevels[row][col]++;
        }

        if (energylevels[row][col] > 9) {
            energylevels[row][col] = 0;
            updateEnergies(energylevels, row - 1, col - 1);
            updateEnergies(energylevels, row - 1, col);
            updateEnergies(energylevels, row - 1, col + 1);
            updateEnergies(energylevels, row, col - 1);
            updateEnergies(energylevels, row, col + 1);
            updateEnergies(energylevels, row + 1, col - 1);
            updateEnergies(energylevels, row + 1, col);
            updateEnergies(energylevels, row + 1, col + 1);
        }
    }

    private static void simulateDay(int[][] energyLevels) {
        for (int i = 0; i < energyLevels.length; i++) {
            for (int j = 0; j < energyLevels[i].length; j++) {
                energyLevels[i][j]++;
            }
        }

        for (int i = 0; i < energyLevels.length; i++) {
            for (int j = 0; j < energyLevels[i].length; j++) {
                if (energyLevels[i][j] > 9) {
                    updateEnergies(energyLevels, i, j);
                }
            }
        }
    }

    private static long getNumOfFlashes(int[][] energyLevels) {
        long numOfFlashes = 0;
        for (int i = 0; i < energyLevels.length; i++) {
            for (int j = 0; j < energyLevels[i].length; j++) {
                if (energyLevels[i][j] == 0) {
                    numOfFlashes++;
                }
            }
        }

        return numOfFlashes;
    }

    private static int[][] copyArray(int[][] twoDimArray) {
        int[][] result = new int[twoDimArray.length][twoDimArray[0].length];

        for (int i = 0; i < twoDimArray.length; i++) {
            for (int j = 0; j < twoDimArray[i].length; j++) {
                result[i][j] = twoDimArray[i][j];
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day11.txt");
        int[][] energyLevelsOrig = new int[inputLines.size()][inputLines.get(0).length()];
        int row = 0;
        for (String inputLine: inputLines) {
            int col = 0;
            for (char c: inputLine.toCharArray()) {
                energyLevelsOrig[row][col] = c - '0';
                col++;
            }
            row++;
        }

        // Day 11_1
        long totNumOfFlashes = 0;
        int[][] energyLevels1 = copyArray(energyLevelsOrig);
        for (int i = 1; i <= 100; i++) {
            simulateDay(energyLevels1);
            totNumOfFlashes += getNumOfFlashes(energyLevels1);
        }
        System.out.println(totNumOfFlashes);

        // Day 11_2
        int[][] energyLevels2 = copyArray(energyLevelsOrig);
        int stepNum = 1;
        while (true) {
            simulateDay(energyLevels2);
            long numFlashes = getNumOfFlashes(energyLevels2);
            if (numFlashes == 100) break;
            stepNum++;
        }

        System.out.println(stepNum);
    }
}
