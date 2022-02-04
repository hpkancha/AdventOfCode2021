import common.FileUtils;

import java.util.List;

public class Day25 {
    private static char[][] parseInput(List<String> inputLines) {
        char[][] result = new char[inputLines.size()][];

        for (int i = 0; i < inputLines.size(); i++) {
            result[i] = inputLines.get(i).toCharArray();
        }

        return result;
    }

    private static boolean equalArrays(char[][] s1, char[][] s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        for (int i = 0; i < s1.length; i++) {
            for (int j = 0; j < s1[i].length; j++) {
                if (s1[i][j] != s2[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private static char[][] moveEast(char[][] state) {
        char[][] result = new char[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                result[i][j] = state[i][j];
                if (state[i][j] == '>') {
                    if (j != state[i].length - 1 && state[i][j + 1] == '.') {
                        result[i][j] = '.';
                        result[i][j + 1] = '>';
                        j++;
                    } else if (j == state[i].length - 1 && state[i][0] == '.') {
                        result[i][j] = '.';
                        result[i][0] = '>';
                    }
                }
            }
        }
        return result;
    }

    private static char[][] moveSouth(char[][] state) {
        char[][] result = new char[state.length][state[0].length];

        for (int i = 0; i < state[0].length; i++) {
            for (int j = 0; j < state.length; j++) {
                result[j][i] = state[j][i];
                if (state[j][i] == 'v') {
                    if (j != state.length - 1 && state[j + 1][i] == '.') {
                        result[j][i] = '.';
                        result[j + 1][i] = 'v';
                        j++;
                    } else if (j == state.length - 1 && state[0][i] == '.') {
                        result[j][i] = '.';
                        result[0][i] = 'v';
                    }
                }
            }
        }
        return result;
    }

    private static char[][] runStep(char[][] state) {
        return moveSouth(moveEast(state));
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day25.txt");
        char[][] initialState = parseInput(inputLines);
        char[][] prevState = initialState;

        // Day25_1
        int numSteps = 0;
        while (true) {
            numSteps++;
            char[][] newState = runStep(prevState);
            if (equalArrays(prevState, newState)) {
                break;
            }
            prevState = newState;
        }

        System.out.println(numSteps);
    }
}
