import common.FileUtils;
import javafx.util.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Cuboid {
    String instruction;
    int x1;
    int x2;
    int y1;
    int y2;
    int z1;
    int z2;

    public Cuboid(String instruction, int x1, int x2, int y1, int y2, int z1, int z2) {
        this.instruction = instruction;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }

    public Long getVolume() {
        long length = x2 - x1 + 1;
        long width = y2 - y1 + 1;
        long height = z2 - z1 + 1;
        return length * width * height;
    }

    public boolean isInitialization() {
        return x1 >= -50 && x1 <= 50 &&
                x2 >= -50 && x2 <= 50 &&
                y1 >= -50 && y1 <= 50 &&
                y2 >= -50 && y2 <= 50 &&
                z1 >= -50 && z1 <= 50 &&
                z2 >= -50 && z2 <= 50;
    }
}

public class Day22 {
    static Pattern inputPattern = Pattern.compile("([-\\d]+)");

    private static Cuboid parseInputLine(String inputLine) {
        Matcher inputMatcher = inputPattern.matcher(inputLine);
        Queue<Integer> stepNums = new LinkedList<>();
        while (inputMatcher.find()) {
            stepNums.offer(Integer.parseInt(inputMatcher.group()));
        }

        String instruction = inputLine.startsWith("on") ? "on" : "off";
        return new Cuboid(instruction, stepNums.poll(), stepNums.poll(),
                stepNums.poll(), stepNums.poll(), stepNums.poll(), stepNums.poll());
    }

    private static Cuboid getIntersection(Cuboid c1, Cuboid c2) {
        Cuboid intersection = new Cuboid("on", Math.max(c1.x1, c2.x1), Math.min(c1.x2, c2.x2),
                Math.max(c1.y1, c2.y1), Math.min(c1.y2, c2.y2),
                Math.max(c1.z1, c2.z1), Math.min(c1.z2, c2.z2));
        if (intersection.x1 > intersection.x2 || intersection.y1 > intersection.y2 || intersection.z1 > intersection.z2) {
            return null;
        }
        return intersection;
    }

    private static Long getTotalOnCubes(List<Cuboid> cuboids) {
        List<Pair<Integer, Cuboid>> cuboidOperations = new ArrayList<>();
        for (Cuboid c: cuboids) {
            List<Pair<Integer, Cuboid>> newCuboidOperations = new ArrayList<>();

            if ("on".equals(c.instruction)) {
                newCuboidOperations.add(new Pair<>(1, c));
            }

            for (Pair<Integer, Cuboid> cuboidOperation: cuboidOperations) {
                Cuboid intersection = getIntersection(cuboidOperation.getValue(), c);
                if (intersection != null) {
                    newCuboidOperations.add(new Pair<>(-cuboidOperation.getKey(), intersection));
                }
            }

            cuboidOperations.addAll(newCuboidOperations);
        }

        return cuboidOperations.stream().mapToLong(c -> c.getKey() * c.getValue().getVolume()).sum();
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day22.txt");
        List<Cuboid> cuboids = inputLines.stream().map(Day22::parseInputLine).collect(Collectors.toList());

        // Day22_1
        System.out.println(getTotalOnCubes(cuboids.stream().filter(Cuboid::isInitialization).collect(Collectors.toList())));

        // Day22_2
        System.out.println(getTotalOnCubes(cuboids));
    }
}
