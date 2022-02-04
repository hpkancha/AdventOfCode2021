import common.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class BingoBoard {
    int[][] bingoNums = new int[5][5];
    boolean[][] hits = new boolean[5][5];
}

public class Day4 {
    private static void markBingoBoards(int num, List<BingoBoard> bingoBoards) {
        for (BingoBoard b : bingoBoards) {
            for (int i = 0; i < b.bingoNums.length; i++) {
                for (int j = 0; j < b.bingoNums[0].length; j++) {
                    if (b.bingoNums[i][j] == num) {
                        b.hits[i][j] = true;
                    }
                }
            }
        }
    }

    private static List<BingoBoard> findWinningBoards(List<BingoBoard> bingoBoards) {
        return bingoBoards.stream().filter(b -> {
            for (int i = 0; i < b.hits.length; i++) {
                boolean rowHit = true;
                boolean colHit = true;
                for (int j = 0; j < b.hits[0].length; j++) {
                    rowHit = rowHit && b.hits[i][j];
                    colHit = colHit && b.hits[j][i];
                }
                if (rowHit || colHit) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    private static int getUnmarkedSum(BingoBoard bingoBoard) {
        int ret = 0;
        for (int i = 0; i < bingoBoard.bingoNums.length; i++) {
            for (int j = 0; j < bingoBoard.bingoNums[0].length; j++) {
                if (!bingoBoard.hits[i][j]) {
                    ret += bingoBoard.bingoNums[i][j];
                }
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        List<String> inputs = FileUtils.readLinesFromFile("Day4.txt");
        List<Integer> drawnNumbers = Arrays.stream(
                inputs.get(0).split(",")).map(
                Integer::parseInt).collect(Collectors.toList());

        inputs.remove(0);

        int rowNumBeingParsed = 0;
        BingoBoard b = null;
        List<BingoBoard> bingoBoards = new ArrayList<>();
        for (String input: inputs) {
            if (!input.isEmpty()) {
                if (rowNumBeingParsed == 0) {
                    b = new BingoBoard();
                    bingoBoards.add(b);
                }
                b.bingoNums[rowNumBeingParsed++] = Arrays.stream(
                        input.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
            } else {
                rowNumBeingParsed = 0;
            }
        }

        // Day4_1
        for (int num: drawnNumbers) {
            markBingoBoards(num, bingoBoards);
            List<BingoBoard> winningBoards = findWinningBoards(bingoBoards);
            if (winningBoards.size() > 0) {
                BingoBoard winningBoard = winningBoards.get(0);
                System.out.println(getUnmarkedSum(winningBoard) * num);
                break;
            }
        }

        // Day4_2
        for (int num: drawnNumbers) {
            markBingoBoards(num, bingoBoards);
            List<BingoBoard> winningBoards = findWinningBoards(bingoBoards);
            if (winningBoards.size() > 0) {
                if (bingoBoards.size() > 1) {
                    for (BingoBoard winningBoard: winningBoards) {
                        bingoBoards.remove(winningBoard);
                    }
                } else {
                    BingoBoard winningBoard = winningBoards.get(0);
                    System.out.println(getUnmarkedSum(winningBoard) * num);
                    break;
                }
            }
        }
    }
}
