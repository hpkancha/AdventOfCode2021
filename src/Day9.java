import common.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class LowPoint {
    int row;
    int col;
    int value;
}

public class Day9 {
    private static boolean isLowPoint(int row, int col, int[][] heightMap) {
        if (row != 0 && heightMap[row][col] >= heightMap[row - 1][col]) {
            return false;
        }
        if (col != heightMap[0].length - 1 && heightMap[row][col] >= heightMap[row][col + 1]) {
            return false;
        }
        if (row != heightMap.length - 1 && heightMap[row][col] >= heightMap[row + 1][col]) {
            return false;
        }
        if (col != 0 && heightMap[row][col] >= heightMap[row][col - 1]) {
            return false;
        }
        return true;
    }

    private static List<LowPoint> findLowPoints(int[][] heightMap) {
        List<LowPoint> ret = new ArrayList<>();
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[i].length; j++) {
                if (isLowPoint(i, j, heightMap)) {
                    LowPoint lp = new LowPoint();
                    lp.row = i;
                    lp.col = j;
                    lp.value = heightMap[i][j];
                    ret.add(lp);
                }
            }
        }
        return ret;
    }

    private static void populateBasin(int[][] heightMap, boolean[][] basinMap, int row, int col) {
        if (heightMap[row][col] == 9 || basinMap[row][col]) {
            return;
        }

        basinMap[row][col] = true;

        if (row != 0) {
            populateBasin(heightMap, basinMap, row - 1, col);
        }
        if (row != heightMap.length - 1) {
            populateBasin(heightMap, basinMap, row + 1, col);
        }
        if (col != 0) {
            populateBasin(heightMap, basinMap, row, col - 1);
        }
        if (col != heightMap[0].length - 1) {
            populateBasin(heightMap, basinMap, row, col + 1);
        }
    }

    private static int getBasinSize(LowPoint lowPoint, int[][] heightMap) {
        boolean[][] basinMap = new boolean[heightMap.length][heightMap[0].length];
        populateBasin(heightMap, basinMap, lowPoint.row, lowPoint.col);
        int basinSize = 0;
        for (int i = 0; i < basinMap.length; i++) {
            for (int j = 0; j < basinMap[i].length; j++) {
                if (basinMap[i][j]) {
                    basinSize++;
                }
            }
        }
        return basinSize;
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day9.txt");
        int[][] heightMap = new int[inputLines.size()][inputLines.get(0).length()];
        int row = 0;
        for (String inputLine: inputLines) {
            int col = 0;
            for (char c: inputLine.toCharArray()) {
                heightMap[row][col] = c - '0';
                col++;
            }
            row++;
        }

        // Day 9_1
        List<LowPoint> lowPoints = findLowPoints(heightMap);
        System.out.println(lowPoints.stream().map(lp -> lp.value).reduce(Integer::sum).get() + lowPoints.size());

        // Day9_2
        List<Integer> basinSizes = lowPoints.stream().map(lp -> getBasinSize(lp, heightMap)).collect(Collectors.toList());
        Collections.sort(basinSizes, Collections.reverseOrder());
        System.out.println(basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2));
    }
}
