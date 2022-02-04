import common.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

class Coordinate3D {
    int x;
    int y;
    int z;

    public Coordinate3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate3D that = (Coordinate3D) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public Coordinate3D add(Coordinate3D c) {
        return new Coordinate3D(x + c.x, y + c.y, z + c.z);
    }

    public int getManhattanDistance(Coordinate3D c) {
        return Math.abs(x - c.x) + Math.abs(y - c.y) + Math.abs(z - c.z);
    }

    public Coordinate3D rotate(int orientation) {
        switch (orientation) {
            case 1:
                return new Coordinate3D(x, y, z);
            case 2:
                return new Coordinate3D(x, -y, -z);
            case 3:
                return new Coordinate3D(x, z, -y);
            case 4:
                return new Coordinate3D(x, -z, y);
            case 5:
                return new Coordinate3D(-x, y, -z);
            case 6:
                return new Coordinate3D(-x, -y, z);
            case 7:
                return new Coordinate3D(-x, z, y);
            case 8:
                return new Coordinate3D(-x, -z, -y);
            case 9:
                return new Coordinate3D(y, x, -z);
            case 10:
                return new Coordinate3D(y, -x, z);
            case 11:
                return new Coordinate3D(y, z, x);
            case 12:
                return new Coordinate3D(y, -z, -x);
            case 13:
                return new Coordinate3D(-y, x, z);
            case 14:
                return new Coordinate3D(-y, -x, -z);
            case 15:
                return new Coordinate3D(-y, z, -x);
            case 16:
                return new Coordinate3D(-y, -z, x);
            case 17:
                return new Coordinate3D(z, y, -x);
            case 18:
                return new Coordinate3D(z, -y, x);
            case 19:
                return new Coordinate3D(z, x, y);
            case 20:
                return new Coordinate3D(z, -x, -y);
            case 21:
                return new Coordinate3D(-z, x, -y);
            case 22:
                return new Coordinate3D(-z, -x, y);
            case 23:
                return new Coordinate3D(-z, y, x);
            case 24:
                return new Coordinate3D(-z, -y, -x);
            default:
                System.out.println("Invalid Rotation Position");
                return null;
        }
    }
}

class Scanner {
    Set<Coordinate3D> beaconPositions;
    Coordinate3D relativePosition;
    int orientation;
    Set<Coordinate3D> relativeBeaconPositions;

    public Scanner() {
        this.beaconPositions = new HashSet<>();
        this.orientation = 1;
    }

    public Set<Coordinate3D> getBeaconPositionsByOrientation() {
        return beaconPositions.stream().map(b -> b.rotate(orientation)).collect(Collectors.toSet());
    }

    public void updateRelativeBeaconPositions() {
        relativeBeaconPositions = beaconPositions.stream().map(b -> b.rotate(orientation).add(relativePosition)).collect(Collectors.toSet());
    }

    public boolean isOverlap(Scanner s) {
        Set<Coordinate3D> intersection = new HashSet<>();
        intersection.addAll(this.relativeBeaconPositions);
        intersection.retainAll(s.relativeBeaconPositions);
        return intersection.size() >= 12;
    }

    public boolean updateIfOverlap(Scanner s) {
        for (int orientation = 1; orientation <= 24; orientation++) {
            s.orientation = orientation;
            for (Coordinate3D b1: this.relativeBeaconPositions) {
                for (Coordinate3D b2 : s.getBeaconPositionsByOrientation()) {
                    s.relativePosition = new Coordinate3D(b1.x - b2.x, b1.y - b2.y, b1.z - b2.z);
                    s.updateRelativeBeaconPositions();
                    if (this.isOverlap(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scanner scanner = (Scanner) o;
        return Objects.equals(relativePosition, scanner.relativePosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativePosition);
    }
}

public class Day19 {
    private static List<Scanner> parseInput(List<String> inputLines) {
        List<Scanner> result = new ArrayList<>();

        Scanner s = null;
        for (String inputLine: inputLines) {
            if (inputLine.contains("scanner")) {
                s = new Scanner();
                result.add(s);
            } else if (!inputLine.isEmpty()) {
                String[] coordinates = inputLine.split(",");
                s.beaconPositions.add(new Coordinate3D(
                        Integer.parseInt(coordinates[0]),
                        Integer.parseInt(coordinates[1]),
                        Integer.parseInt(coordinates[2])
                ));
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day19.txt");
        List<Scanner> scanners = parseInput(inputLines);

        Long startTime = System.currentTimeMillis();

        // Day19_1
        Scanner scannerZero = scanners.get(0);
        scannerZero.relativePosition = new Coordinate3D(0, 0, 0);
        scannerZero.updateRelativeBeaconPositions();
        Queue<Scanner> matchedScanners = new LinkedList<>();
        List<Scanner> unMatchedScanners = new ArrayList<>(scanners);
        matchedScanners.add(scannerZero);
        unMatchedScanners.removeAll(matchedScanners);

        while (!matchedScanners.isEmpty()) {
            Scanner s1 = matchedScanners.poll();
            for (Scanner s2: unMatchedScanners) {
                if (s1.updateIfOverlap(s2)) {
                    matchedScanners.offer(s2);
                }
            }
            unMatchedScanners.removeAll(matchedScanners);
        }

        Set<Coordinate3D> allBeacons = new HashSet<>();
        for (Scanner s: scanners) {
            allBeacons.addAll(s.relativeBeaconPositions);
        }
        System.out.println(allBeacons.size());

        // Day19_2
        int maxManhattanDistance = Integer.MIN_VALUE;
        for (Scanner s1: scanners) {
            for (Scanner s2: scanners) {
                if (!s1.equals(s2)) {
                    maxManhattanDistance = Math.max(maxManhattanDistance, s1.relativePosition.getManhattanDistance(s2.relativePosition));
                }
            }
        }
        System.out.println(maxManhattanDistance);

        System.out.println("Execution Time: " + (System.currentTimeMillis() - startTime));
    }
}
