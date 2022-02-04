import common.FileUtils;

import java.util.List;

public class Day1 {
    private static int getSlidingWindowIncreases(List<Integer> depths, int windowSize) {
        int numIncreases = 0;
        int prevWindowSum = Integer.MAX_VALUE;
        for (int i = 0; i <= depths.size() - windowSize; i++) {
            int currWindowSum = 0;
            for (int j = 0; j < windowSize; j++) {
                currWindowSum += depths.get(i + j);
            }
            if (currWindowSum > prevWindowSum) {
                numIncreases++;
            }
            prevWindowSum = currWindowSum;
        }
        return numIncreases;
    }

    public static void main(String args[]) {
        List<Integer> inputNums = FileUtils.readNumsFromFile("Day1.txt");

        // Day 1_1
        System.out.println(getSlidingWindowIncreases(inputNums, 1));

        // Day 1_2
        System.out.println(getSlidingWindowIncreases(inputNums, 3));
    }
}
