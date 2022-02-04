import common.FileUtils;

import java.util.List;
import java.util.stream.Collectors;

class Command {
    String direction;
    int unit;

    public Command(String direction, int unit) {
        this.direction = direction;
        this.unit = unit;
    }
}

class Position {
    int horizontalPosition = 0;
    int depth = 0;
    int aim = 0;
}

public class Day2 {
    private static Position getFinalPosition(List<Command> commands) {
        Position finalPosition = new Position();

        commands.stream().forEach(command -> {
            switch(command.direction) {
                case "forward": {
                    finalPosition.horizontalPosition += command.unit;
                    break;
                }
                case "down": {
                    finalPosition.depth += command.unit;
                    break;
                }
                case "up": {
                    finalPosition.depth -= command.unit;
                    break;
                }
                default: {
                    System.out.println("Invalid command:" + command.direction);
                    break;
                }
            }
        });

        return finalPosition;
    }

    private static Position getFinalPosition2(List<Command> commands) {
        Position finalPosition = new Position();

        commands.stream().forEach(command -> {
            switch(command.direction) {
                case "forward": {
                    finalPosition.horizontalPosition += command.unit;
                    finalPosition.depth += finalPosition.aim * command.unit;
                    break;
                }
                case "down": {
                    finalPosition.aim += command.unit;
                    break;
                }
                case "up": {
                    finalPosition.aim -= command.unit;
                    break;
                }
                default: {
                    System.out.println("Invalid command:" + command.direction);
                    break;
                }
            }
        });

        return finalPosition;
    }

    public static void main(String[] args) {
        List<String> commandLines = FileUtils.readLinesFromFile("Day2.txt");
        List<Command> commands = commandLines.stream().map(line -> {
            String[] splitLne = line.split("\\s+");
            return new Command(splitLne[0], Integer.parseInt(splitLne[1]));
        }).collect(Collectors.toList());

        // Day 2_1
        Position finalPosition = getFinalPosition(commands);
        System.out.println(finalPosition.horizontalPosition * finalPosition.depth);

        // Day 2_2
        Position finalPosition2 = getFinalPosition2(commands);
        System.out.println(finalPosition2.horizontalPosition * finalPosition2.depth);
    }
}