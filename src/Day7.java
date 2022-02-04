import common.FileUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day7 {
    private static int getMedian(List<Integer> crabPositions) {
        Collections.sort(crabPositions);
        return crabPositions.get(crabPositions.size() / 2);
    }

    private static int getMean1(List<Integer> crabPositions) {
        return ((int) Math.floor(crabPositions.stream().reduce(Integer::sum).get() * 1.0 / crabPositions.size()));
    }

    private static int getMean2(List<Integer> crabPositions) {
        return ((int) Math.ceil(crabPositions.stream().reduce(Integer::sum).get() * 1.0 / crabPositions.size()));
    }

    public static void main(String[] args) {
        List<String> crabPositionsString = FileUtils.readLinesFromFile("Day7.txt");
        List<Integer> crabPositions = Arrays.stream(crabPositionsString.get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // Day7_1
        int medianPosition = getMedian(crabPositions);
        int totalNeededFuel1 = 0;
        for (int p: crabPositions) {
            totalNeededFuel1 += Math.abs(p - medianPosition);
        }
        System.out.println(totalNeededFuel1);

        //Day7_2
        int meanPosition1 = getMean1(crabPositions);
        int totalNeededFuel2_1 = 0;
        for (int p: crabPositions) {
            int distance = Math.abs(p - meanPosition1);
            totalNeededFuel2_1 += distance * (distance + 1) / 2;
        }
        System.out.println(totalNeededFuel2_1);

        int meanPosition2 = getMean2(crabPositions);
        int totalNeededFuel2_2 = 0;
        for (int p: crabPositions) {
            int distance = Math.abs(p - meanPosition2);
            totalNeededFuel2_2 += distance * (distance + 1) / 2;
        }
        System.out.println(totalNeededFuel2_2);
    }
}
