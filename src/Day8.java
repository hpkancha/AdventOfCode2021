import common.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day8 {
    static Map<String, Integer> segmentToDigitMap = new HashMap<String, Integer>() {{
        put("abcefg", 0);
        put("cf", 1);
        put("acdeg", 2);
        put("acdfg", 3);
        put("bcdf", 4);
        put("abdfg", 5);
        put("abdefg", 6);
        put("acf", 7);
        put("abcdefg", 8);
        put("abcdfg", 9);
    }};

    private static String sortString(String s) {
        char[] charArray = s.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    private static List<Character> convertStringToCharList(String s) {
        return s.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
    }

    private static String convertCharListToString(List<Character> chars) {
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private static String getCommonChars(List<String> patterns) {
        Map<Character, Integer> characterCountMap = new HashMap<>();
        for (String pattern: patterns) {
            for (Character c: convertStringToCharList(pattern)) {
                if (!characterCountMap.containsKey(c)) {
                    characterCountMap.put(c, 0);
                }
                characterCountMap.put(c, characterCountMap.get(c) + 1);
            }
        }
        List<Character> commonChars = characterCountMap.keySet().stream().filter(
                c -> characterCountMap.get(c) == patterns.size()).collect(Collectors.toList());
        return convertCharListToString(commonChars);
    }

    private static boolean containsAllChars(String s1, String s2) {
        boolean contains = true;
        List<Character> s1Chars = convertStringToCharList(s1);
        for (Character c: convertStringToCharList(s2)) {
            if (!s1Chars.contains(c)) {
                contains = false;
            }
        }
        return contains;
    }

    private static void updateSegmentMapping(String pattern, String segment, Map<String, String> segmentMappings) {
        List<Character> patternChars = convertStringToCharList(pattern);
        List<Character> segmentChars = convertStringToCharList(segment);
        for (Map.Entry<String, String> mapping : segmentMappings.entrySet()) {
            List<Character> mappingPatternChars = convertStringToCharList(mapping.getKey());
            List<Character> mappingSegmentChars = convertStringToCharList(mapping.getValue());

            if (containsAllChars(pattern, mapping.getKey())) {
                patternChars.removeAll(mappingPatternChars);
                segmentChars.removeAll(mappingSegmentChars);
            }
        }
        segmentMappings.put(convertCharListToString(patternChars), convertCharListToString(segmentChars));
    }

    private static void optimizeMappings(Map<String, String> segmentMappings) {
        Map<String, String> reversedSegmentMappings = new HashMap<>();
        for (Map.Entry<String, String> mapping: segmentMappings.entrySet()) {
            reversedSegmentMappings.put(mapping.getValue(), mapping.getKey());
        }

        String cf_mapping = reversedSegmentMappings.get("cf");
        segmentMappings.remove(cf_mapping);
        updateSegmentMapping(cf_mapping, "cf", segmentMappings);

        String eg_mapping = reversedSegmentMappings.get("eg");
        String dg_mapping = reversedSegmentMappings.get("dg");
        segmentMappings.put(getCommonChars(Arrays.asList(eg_mapping, dg_mapping)), "g");
        segmentMappings.remove(eg_mapping);
        segmentMappings.remove(dg_mapping);
        updateSegmentMapping(eg_mapping, "eg", segmentMappings);
        updateSegmentMapping(dg_mapping, "dg", segmentMappings);

        String bd_mapping = reversedSegmentMappings.get("bd");
        segmentMappings.remove(bd_mapping);
        updateSegmentMapping(bd_mapping, "bd", segmentMappings);
    }

    private static Map<String, String> getMappings(List<String> patterns) {
        Map<Integer, List<String>> lengthToPatternsMap = new HashMap<>();
        for (String pattern: patterns) {
            int length = pattern.length();
            if (!lengthToPatternsMap.containsKey(length)) {
                lengthToPatternsMap.put(length, new ArrayList<>());
            }
            lengthToPatternsMap.get(length).add(sortString(pattern));
        }

        Map<String, String> segmentMappings = new HashMap<>();
        String one_pattern = lengthToPatternsMap.get(2).get(0);
        segmentMappings.put(one_pattern, "cf");
        String seven_pattern = lengthToPatternsMap.get(3).get(0);
        updateSegmentMapping(seven_pattern, "acf", segmentMappings);
        String four_pattern = lengthToPatternsMap.get(4).get(0);
        updateSegmentMapping(four_pattern, "bcdf", segmentMappings);
        String eight_pattern = lengthToPatternsMap.get(7).get(0);
        updateSegmentMapping(eight_pattern, "abcdefg", segmentMappings);

        String common_five_len_pattern = getCommonChars(lengthToPatternsMap.get(5));
        updateSegmentMapping(common_five_len_pattern, "adg", segmentMappings);

        List<String> six_length_patterns = lengthToPatternsMap.get(6);
        String six_pattern = null;
        for (String s: six_length_patterns) {
            if (!containsAllChars(s, one_pattern)) {
                six_pattern = s;
            }
        }
        updateSegmentMapping(six_pattern, "abdefg", segmentMappings);
        optimizeMappings(segmentMappings);

        return segmentMappings;
    }

    private static String mapToSegment(String pattern, Map<String, String> segementMappings) {
        return sortString(
                convertStringToCharList(pattern).stream()
                        .map(c -> segementMappings.get(c.toString()))
                        .collect(Collectors.joining()));
    }

    private static int getNumFromDigits(List<String> digitStrings, Map<String, String> segementMappings) {
        String ret = "";
        for (String digitString: digitStrings) {
            ret += segmentToDigitMap.get(mapToSegment(digitString, segementMappings));
        }
        return Integer.parseInt(ret);
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day8.txt");
        Map<List<String>, List<String>> patternsToDigitsMap = new HashMap<>();
        for (String inputLine: inputLines) {
            String[] inputLineSplit = inputLine.split("\\|");
            patternsToDigitsMap.put(
                    Arrays.asList(inputLineSplit[0].trim().split("\\s+")),
                    Arrays.asList(inputLineSplit[1].trim().split("\\s+"))
            );
        }

        // Day8_1
        List<Integer> uniqueSegmentLengths = Arrays.asList(2, 3, 4, 7);
        int result = 0;
        for (String digitString: patternsToDigitsMap.values().stream().flatMap(x -> x.stream()).collect(Collectors.toList())) {
            if (uniqueSegmentLengths.contains(digitString.length())) {
                result++;
            }
        }
        System.out.println(result);

        // Day8_2
        int sumOfOutputs = 0;
        for (Map.Entry<List<String>, List<String>> patternToDigitsMapping: patternsToDigitsMap.entrySet()) {
            Map<String, String> segmentMappings = getMappings(patternToDigitsMapping.getKey());
            int digits = getNumFromDigits(patternToDigitsMapping.getValue(), segmentMappings);
            sumOfOutputs += digits;
        }
        System.out.println(sumOfOutputs);
    }
}
