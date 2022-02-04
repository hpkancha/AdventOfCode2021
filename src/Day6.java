import common.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

class LanternFish {
    int timeUntilSpawn;

    public LanternFish(int timeUntilSpawn) {
        this.timeUntilSpawn = timeUntilSpawn;
    }
}
public class Day6 {
    private static List<LanternFish> simulateDayOld(List<LanternFish> lanternFishes) {
        return lanternFishes.parallelStream().map(lf -> {
           if (lf.timeUntilSpawn == 0) {
               lf.timeUntilSpawn = 6;
               return Arrays.asList(lf, new LanternFish(8));
           } else {
               lf.timeUntilSpawn--;
               return Arrays.asList(lf);
           }
       }).flatMap(lf -> lf.stream()).collect(Collectors.toList());
    }

    private static Map<Integer, Long> simulateDay(Map<Integer, Long> lanternFishMap) {
        Map<Integer, Long> retMap = new HashMap<>();
        for (Integer timeUntilSpawn: lanternFishMap.keySet()) {
            if (timeUntilSpawn == 0) {
                retMap.put(8, lanternFishMap.get(timeUntilSpawn));
                retMap.put(6, retMap.getOrDefault(timeUntilSpawn, 0L) + lanternFishMap.get(timeUntilSpawn));
            } else {
                retMap.put(timeUntilSpawn - 1, retMap.getOrDefault(timeUntilSpawn - 1, 0L) + lanternFishMap.get(timeUntilSpawn));
            }
        }
        return retMap;
    }

    private static Map<Integer, Long> getInitialLanternFishMap(List<LanternFish> lanternFishes) {
        Map<Integer, Long> lanternFishMap = new HashMap<>();
        for (LanternFish lf: lanternFishes) {
            lanternFishMap.put(lf.timeUntilSpawn, lanternFishMap.getOrDefault(lf.timeUntilSpawn, 0L) + 1);
        }
        return lanternFishMap;
    }

    public static void main(String[] args) {
        List<String> initalState = FileUtils.readLinesFromFile("Day6.txt");
        List<LanternFish> lanternFishes = Arrays.stream(initalState.get(0).split(","))
                .map(t -> new LanternFish(Integer.parseInt(t)))
                .collect(Collectors.toList());
        Map<Integer, Long> lanternFishMap = getInitialLanternFishMap(lanternFishes);

        //Day6_1
        Long startTime = new Date().getTime();
        Map<Integer, Long> lanternFishMap1 = lanternFishMap;
        for (int i = 1; i <= 80; i++) {
            lanternFishMap1 = simulateDay(lanternFishMap1);
        }
        System.out.println("Total Fishes: " + lanternFishMap1.values().stream().reduce(0L, Long::sum));
        System.out.println("Runtime:" + (new Date().getTime() - startTime));

        // Day6_2
        Long startTime2 = new Date().getTime();
        Map<Integer, Long> lanternFishMap2 = lanternFishMap;
        for (int i = 1; i <= 256; i++) {
            lanternFishMap2 = simulateDay(lanternFishMap2);
        }
        System.out.println("Total Fishes: " + lanternFishMap2.values().stream().reduce(0L, Long::sum));
        System.out.println("Runtime:" + (new Date().getTime() - startTime2));
    }
}
