import common.FileUtils;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;

import static common.Utils.convertBinaryToLong;

public class Day16 {
    private static Map<Character, String> hexToBinaryMap = new HashMap<Character, String>() {{
        put('0', "0000");
        put('1', "0001");
        put('2', "0010");
        put('3', "0011");
        put('4', "0100");
        put('5', "0101");
        put('6', "0110");
        put('7', "0111");
        put('8', "1000");
        put('9', "1001");
        put('A', "1010");
        put('B', "1011");
        put('C', "1100");
        put('D', "1101");
        put('E', "1110");
        put('F', "1111");
    }};

    private static int totalVersionNum = 0;

    private static String convertHexToBinary(String hex) {
        StringBuilder sb = new StringBuilder();
        for (Character c: hex.toCharArray()) {
            sb.append(hexToBinaryMap.get(c));
        }

        return sb.toString();
    }

    private static Pair<Long, Integer> parseLiteral(String packet) {
        StringBuilder sb = new StringBuilder();
        char groupStartChar;
        int endIndex = 6;
        do {
            groupStartChar = packet.charAt(endIndex);
            sb.append(packet, endIndex + 1, endIndex + 5);
            endIndex += 5;
        } while (groupStartChar != '0');
        return new Pair<>( convertBinaryToLong(sb.toString()), endIndex);
    }

    private static Pair<List<Long>, Integer> parseOperator(String packet) {
        long lengthTypeId = convertBinaryToLong(packet.substring(6, 7));
        int startIndex;

        List<Long> numbers = new ArrayList<>();
        if (lengthTypeId == 0L) {
            // Read next 15 bits
            long subPacketsTotalLength = convertBinaryToLong(packet.substring(7, 22));

            startIndex = 22;
            while (startIndex < 22 + subPacketsTotalLength) {
                Pair<Long, Integer> result = parsePacket(packet.substring(startIndex));
                startIndex += result.getValue();
                numbers.add(result.getKey());
            }
        } else {
            // Read next 11 bits
            long numSubPackets = convertBinaryToLong(packet.substring(7, 18));
            startIndex = 18;
            while (numSubPackets > 0) {
                Pair<Long, Integer> result = parsePacket(packet.substring(startIndex));
                startIndex += result.getValue();
                numSubPackets--;
                numbers.add(result.getKey());
            }
        }
        return new Pair<>(numbers, startIndex);
    }

    private static long operateNumbers(List<Long> numbers, int typeId) {
        switch (typeId) {
            case 0:
                return numbers.stream().reduce(Long::sum).get();
            case 1:
                return numbers.stream().reduce(1L, (a, b) -> a * b);
            case 2:
                return Collections.min(numbers);
            case 3:
                return Collections.max(numbers);
            case 5:
                return numbers.get(0) > numbers.get(1) ? 1 : 0;
            case 6:
                return numbers.get(0) < numbers.get(1) ? 1 : 0;
            case 7:
                return numbers.get(0).equals(numbers.get(1)) ? 1 : 0;
            default:
                return 0;
        }
    }

    private static Pair<Long, Integer> parsePacket(String packet) {
        long versionNum = convertBinaryToLong(packet.substring(0, 3));
        totalVersionNum += versionNum;

        long typeId = convertBinaryToLong(packet.substring(3, 6));

        // Literal
        if (typeId == 4) {
            return parseLiteral(packet);
        } else {
            // Operator
            Pair<List<Long>, Integer> result = parseOperator(packet);
            long operationResult = operateNumbers(result.getKey(), (int) typeId);
            return new Pair<>(operationResult, result.getValue());
        }
    }

    public static void main(String[] args) {
        String input = FileUtils.readLinesFromFile("Day16.txt").get(0);
        Pair<Long, Integer> result = parsePacket(convertHexToBinary(input));
        System.out.println(totalVersionNum);
        System.out.println(result.getKey());
    }
}
