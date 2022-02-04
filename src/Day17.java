import common.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {
    private static List<Coordinate> findAllVelocities(int xStart, int xEnd, int yStart, int yEnd) {
        List<Coordinate> result = new ArrayList<>();
        for (int x = 0; x <= xEnd; x++) {
            for (int y = yEnd; y <= -(yEnd - 1); y++) {
                Coordinate c = new Coordinate(x, y);
                if (reachTargetArea(c, xStart, xEnd, yStart, yEnd)) {
                    result.add(c);
                }
            }
        }

        return result;
    }

    private static boolean reachTargetArea(Coordinate velocity, int xStart, int xEnd, int yStart, int yEnd) {
        int xVelocity = velocity.x;
        int yVelocity = velocity.y;

        Coordinate currPosition = new Coordinate(0, 0);

        while (currPosition.x <= xEnd && currPosition.y >= yEnd) {
            currPosition.x += xVelocity;
            currPosition.y += yVelocity;
            if (currPosition.x >= xStart && currPosition.x <= xEnd && currPosition.y <= yStart && currPosition.y >= yEnd) {
                return true;
            }
            if (xVelocity > 0) {
                xVelocity--;
            }
            yVelocity--;
        }

        return false;
    }

    public static void main(String[] args) {
        String input = FileUtils.readLinesFromFile("Day17.txt").get(0);
        Pattern inputPattern = Pattern.compile(".*x=(\\d+)\\.\\.(\\d+).*(-\\d+)\\.\\.(-\\d+)");
        Matcher inputMatcher = inputPattern.matcher(input);
        inputMatcher.find();
        int xStart = Integer.parseInt(inputMatcher.group(1));
        int xEnd = Integer.parseInt(inputMatcher.group(2));
        int yEnd = Integer.parseInt(inputMatcher.group(3));
        int yStart = Integer.parseInt(inputMatcher.group(4));


        System.out.println(findAllVelocities(xStart, xEnd, yStart, yEnd).size());
    }
}
