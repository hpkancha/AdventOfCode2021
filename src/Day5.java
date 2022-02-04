import common.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class LineSegment {
    Coordinate start;
    Coordinate end;

    public LineSegment(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }
}

class VentDiagram {
    int[][] overlaps = new int[1000][1000];
}

public class Day5 {
    private static Coordinate getCoordinateFromString(String coordinateString) {
        String[] coordinates = coordinateString.split(",");
        return new Coordinate(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
    }

    private static VentDiagram getVentDiagram(List<LineSegment> lineSegments) {
        VentDiagram ventDiagram = new VentDiagram();
        lineSegments.forEach(ls -> {
            if (ls.start.x == ls.end.x) {
                int y = ls.start.y;
                ventDiagram.overlaps[y][ls.start.x]++;
                do {
                    if (ls.start.y > ls.end.y) {
                        y--;
                    } else {
                        y++;
                    }
                    ventDiagram.overlaps[y][ls.start.x]++;
                } while (y != ls.end.y);
            }
            if (ls.start.y == ls.end.y) {
                int x = ls.start.x;
                ventDiagram.overlaps[ls.start.y][x]++;
                do {
                    if (ls.start.x > ls.end.x) {
                        x--;
                    } else {
                        x++;
                    }
                    ventDiagram.overlaps[ls.start.y][x]++;
                } while (x != ls.end.x);
            }
        });

        return ventDiagram;
    }

    private static VentDiagram getVentDiagram2(List<LineSegment> lineSegments) {
        VentDiagram ventDiagram = new VentDiagram();
        lineSegments.forEach(ls -> {
            // Horizontal Line
            if (ls.start.x == ls.end.x) {
                int y = ls.start.y;
                ventDiagram.overlaps[y][ls.start.x]++;
                do {
                    if (ls.start.y > ls.end.y) {
                        y--;
                    } else {
                        y++;
                    }
                    ventDiagram.overlaps[y][ls.start.x]++;
                } while (y != ls.end.y);
            }
            // Vertical Line
            if (ls.start.y == ls.end.y) {
                int x = ls.start.x;
                ventDiagram.overlaps[ls.start.y][x]++;
                do {
                    if (ls.start.x > ls.end.x) {
                        x--;
                    } else {
                        x++;
                    }
                    ventDiagram.overlaps[ls.start.y][x]++;
                } while (x != ls.end.x);
            }
            // Diagonal Line
            if (Math.abs(ls.start.x - ls.end.x) == Math.abs(ls.start.y - ls.end.y)) {
                int x = ls.start.x;
                int y = ls.start.y;
                ventDiagram.overlaps[y][x]++;
                do {
                    if (x < ls.end.x) {
                        x++;
                    } else {
                        x--;
                    }

                    if (y < ls.end.y) {
                        y++;
                    } else {
                        y--;
                    }
                    ventDiagram.overlaps[y][x]++;
                } while (x != ls.end.x && y != ls.end.y);
            }
        });

        return ventDiagram;
    }

    public static void main(String[] args) {
        List<String> lineStrings = FileUtils.readLinesFromFile("Day5.txt");
        List<LineSegment> lineSegments = lineStrings.stream().map(lineString -> {
            String[] coordinates = lineString.split(" -> ");
            return new LineSegment(
                    getCoordinateFromString(coordinates[0]),
                    getCoordinateFromString(coordinates[1]));
        }).collect(Collectors.toList());

        // Day5_1
        VentDiagram ventDiagram = getVentDiagram(lineSegments);
        System.out.println(Arrays.stream(ventDiagram.overlaps).flatMapToInt(
                row -> Arrays.stream(row)).filter(cell -> cell >= 2).toArray().length);

        // Day5_2
        VentDiagram ventDiagram2 = getVentDiagram2(lineSegments);
        System.out.println(Arrays.stream(ventDiagram2.overlaps).flatMapToInt(
                row -> Arrays.stream(row)).filter(cell -> cell >= 2).toArray().length);
    }
}
