import common.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {
    private static Map<String, String> pairInsertionRules = new HashMap<>();

    private static String applyInsertionRules(String polymer) {
        StringBuilder result = new StringBuilder("");
        int prevMatchEnd = -1;
        for (int i = 0; i < polymer.length() - 1; i++) {
            String toInsertString = polymer.substring(i, i + 2);
            String insertionRule = pairInsertionRules.get(toInsertString);
            if (insertionRule != null) {
                if (i != prevMatchEnd) {
                    result.append(toInsertString.substring(0, 1));
                }
                result.append(insertionRule).append(toInsertString.substring(1));
                prevMatchEnd = i + 1;
            }
        }
        return result.toString();
    }

    private static Map<String, Long> applyInsertionRules(Map<String, Long> substringCountMap) {
        Map<String, Long> result = new HashMap<>();
        for (String substring: substringCountMap.keySet()) {
            if (pairInsertionRules.containsKey(substring)) {
                long substringCount = substringCountMap.get(substring);
                String newString1 = substring.substring(0, 1) + pairInsertionRules.get(substring);
                String newString2 = pairInsertionRules.get(substring) + substring.substring(1);

                result.put(newString1, result.getOrDefault(newString1, 0L) + substringCount);
                result.put(newString2, result.getOrDefault(newString2, 0L) + substringCount);
            }
        }
        return result;
    }

    private static Map<String, Long> buildSubstringCountMap(String polymer) {
        Map<String, Long> result = new HashMap<>();
        for (int i = 0; i < polymer.length() - 1; i++) {
            String substring = polymer.substring(i, i + 2);
            long prevCount = result.getOrDefault(substring, 0L);
            result.put(substring, prevCount + 1);
        }
        return result;
    }

    private static Map<Character, Long> buildCharCountMap(String polymer) {
        Map<Character, Long> result = new HashMap<>();
        for (Character c: polymer.toCharArray()) {
            if (!result.containsKey(c)) {
                result.put(c, 0L);
            }
            result.put(c, result.get(c) + 1);
        }
        return result;
    }

    private static Map<Character, Long> buildCharCountMap(Map<String, Long> substringCountMap, String polymer) {
        Map<Character, Long> result = new HashMap<>();
        for (String substring: substringCountMap.keySet()) {
            long substringCount = substringCountMap.get(substring);
            for (Character c: substring.toCharArray()) {
                result.put(c, result.getOrDefault(c, 0L) + substringCount);
            }
        }

        Character firstChar = polymer.charAt(0);
        Character lastChar = polymer.charAt(polymer.length() - 1);
        for (Character c: result.keySet()) {
            if (c.equals(firstChar) || c.equals(lastChar)) {
                result.put(c, (result.get(c) + 1) / 2);
            } else {
                result.put(c, result.get(c) / 2);
            }
        }
        return result;
    }

    private static void printVariance(Map<Character, Long> charCountMap) {
        long leastCommonCount = Long.MAX_VALUE;
        long mostCommonCount = Long.MIN_VALUE;
        for (Map.Entry<Character, Long> entry: charCountMap.entrySet()) {
            leastCommonCount = Math.min(entry.getValue(), leastCommonCount);
            mostCommonCount = Math.max(entry.getValue(), mostCommonCount);
        }
        System.out.println(mostCommonCount - leastCommonCount);
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day14.txt");
        String polymer = inputLines.get(0);

        // Remove first 2 lines
        inputLines.remove(0);
        inputLines.remove(0);

        for (String inputLine: inputLines) {
            String[] insertionRule = inputLine.split("->");
            pairInsertionRules.put(insertionRule[0].trim(), insertionRule[1].trim());
        }

        // Day 14_1
        Map<String, Long> substringCountMap = buildSubstringCountMap(polymer);
        for (int i = 1; i <= 10; i++) {
            substringCountMap = applyInsertionRules(substringCountMap);
        }
        Map<Character, Long> charCountMap = buildCharCountMap(substringCountMap, polymer);
        printVariance(charCountMap);

        // Day 14_2
        Map<String, Long> substringCountMap2 = buildSubstringCountMap(polymer);
        for (int i = 1; i <= 40; i++) {
            substringCountMap2 = applyInsertionRules(substringCountMap2);
        }
        Map<Character, Long> charCountMap2 = buildCharCountMap(substringCountMap2, polymer);
        printVariance(charCountMap2);
    }
}
