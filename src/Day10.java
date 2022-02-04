import common.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day10 {
    private static Map<Character, Character> chunkPairs = new HashMap<Character, Character>() {{
        put('(', ')');
        put('[', ']');
        put('{', '}');
        put('<', '>');
    }};

    private static Character getCorruptedChar(String line) {
        char[] chunkSymbols = line.toCharArray();
        Stack<Character> parsedSymbols = new Stack<>();
        for (char chunkSymbol: chunkSymbols) {
            if (chunkPairs.containsKey(chunkSymbol)) {
                parsedSymbols.push(chunkSymbol);
            } else {
                char expectedOpenSymbol = parsedSymbols.pop();
                if (chunkPairs.get(expectedOpenSymbol) != chunkSymbol) {
                    return chunkSymbol;
                }
            }
        }
        return null;
    }

    private static long getAutoCompleteScore(String line) {
        char[] chunkSymbols = line.toCharArray();
        Stack<Character> parsedSymbols = new Stack<>();
        for (char chunkSymbol: chunkSymbols) {
            if (chunkPairs.containsKey(chunkSymbol)) {
                parsedSymbols.push(chunkSymbol);
            } else {
                parsedSymbols.pop();
            }
        }

        Map<Character, Integer> symbolToscoreMap = new HashMap<Character, Integer>() {{
            put(')', 1);
            put(']', 2);
            put('}', 3);
            put('>', 4);
        }};
        long totalScore = 0;
        while (!parsedSymbols.empty()) {
            totalScore *= 5;
            char incompleteSymbol = chunkPairs.get(parsedSymbols.pop());
            totalScore += symbolToscoreMap.get(incompleteSymbol);
        }
        return totalScore;
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day10.txt");

        // Day10_1
        List<Character> illegalCharacters = inputLines.stream()
                .map(l -> getCorruptedChar(l))
                .filter(c -> c != null)
                .collect(Collectors.toList());
        int syntaxErrorScore = illegalCharacters.stream().mapToInt(c -> {
            if (c == ')') {
                return 3;
            } else if (c == ']') {
                return 57;
            } else if (c == '}') {
                return 1197;
            } else {
                return 25137;
            }
        }).reduce(Integer::sum).getAsInt();
        System.out.println(syntaxErrorScore);

        // Day10_2
        List<String> incompleteLines = inputLines.stream()
                .filter(l -> getCorruptedChar(l) == null)
                .collect(Collectors.toList());
        List<Long> autoCompleteScores = incompleteLines.stream()
                .map(l -> getAutoCompleteScore(l))
                .filter(s-> s != 0)
                .collect(Collectors.toList());
        Collections.sort(autoCompleteScores);
        System.out.println(autoCompleteScores.get((autoCompleteScores.size() - 1)/2));
    }
}
