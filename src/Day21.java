import common.FileUtils;

import java.util.*;

class DeterministicDice {
    int lastRoll;
    int totalRolls;

    public DeterministicDice(int lastRoll) {
        this.lastRoll = lastRoll;
    }

    public int roll() {
        totalRolls++;

        lastRoll = (lastRoll + 1) % 100;
        if (lastRoll == 0) {
            lastRoll = 100;
        }
        return lastRoll;
    }
}

class Universe {
    Player p1;
    Player p2;
    int winningPlayer;

    public Universe(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Universe clone() {
        Universe u = new Universe(p1.clone(), p2.clone());
        u.winningPlayer = winningPlayer;

        return u;
    }

    public void playTurn(int totalRoll, int playerTurn) {
        if (playerTurn == 1) {
            p1.updatePositionAndScore(totalRoll);
        } else {
            p2.updatePositionAndScore(totalRoll);
        }

        if (p1.totalScore >= 21) {
            winningPlayer = 1;
        } else if (p2.totalScore >= 21) {
            winningPlayer = 2;
        }
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Universe universe = (Universe) o;
//        return winningPlayer == universe.winningPlayer && Objects.equals(p1, universe.p1) && Objects.equals(p2, universe.p2);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(p1, p2, winningPlayer);
//    }
}

class DiracDice {
    Map<Universe, Long> universeToCountMap = new HashMap<>();
    Map<Integer, Integer> totalRollToUniverseCount = new HashMap<>();
    Long player1Wins = 0L;
    Long player2Wins = 0L;

    public DiracDice(Player p1, Player p2) {
        this.universeToCountMap.put(new Universe(p1, p2), 1L);

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                for (int k = 1; k <= 3; k++) {
                    int totalRoll = i + j + k;
                    int universeCount = totalRollToUniverseCount.getOrDefault(totalRoll, 0);
                    totalRollToUniverseCount.put(totalRoll, universeCount + 1);
                }
            }
        }
    }

    public boolean playTurn(int playerTurn) {
        boolean universesUpdated = false;
        Map<Universe, Long> newUniverseToCountMap = new HashMap<>();
        for (Universe u: universeToCountMap.keySet()) {
            Long universeCount = universeToCountMap.get(u);
            if (u.winningPlayer == 0) {
                for (int totalRoll: totalRollToUniverseCount.keySet()) {
                    Universe newUniverse = u.clone();
                    newUniverse.playTurn(totalRoll, playerTurn);
                    Long newUniverseCount = universeCount * totalRollToUniverseCount.get(totalRoll);
                    if (newUniverse.winningPlayer == 1) {
                        player1Wins += newUniverseCount;
                    } else if (newUniverse.winningPlayer == 2) {
                        player2Wins += newUniverseCount;
                    } else {
                        newUniverseToCountMap.put(newUniverse, newUniverseCount);
                    }
                }
                universesUpdated = true;
            }
        }

        universeToCountMap = newUniverseToCountMap;

        return universesUpdated;
    }
}

class Player {
    int currentPosition;
    int totalScore;

    public Player(int startPosition) {
        this.currentPosition = startPosition;
    }

    public Player clone() {
        Player p = new Player(currentPosition);
        p.totalScore = this.totalScore;
        return p;
    }

    public void playTurn(DeterministicDice dice) {
        int rollTotal = 0;
        for (int i = 1; i <= 3; i++) {
            rollTotal += dice.roll();
        }

        updatePositionAndScore(rollTotal);
    }

    public void updatePositionAndScore(int rollTotal) {
        currentPosition = (currentPosition + rollTotal) % 10;
        if (currentPosition == 0) {
            currentPosition = 10;
        }
        totalScore += currentPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return currentPosition == player.currentPosition && totalScore == player.totalScore;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPosition, totalScore);
    }
}

public class Day21 {
    // Returns number of turns played
    private static void play(Player p1, Player p2, DeterministicDice dice) {
        while (p1.totalScore < 1000 && p2.totalScore < 1000) {
            p1.playTurn(dice);
            if (p1.totalScore >= 1000) {
                break;
            }
            p2.playTurn(dice);
        }
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day21.txt");
        Player p1 = new Player(Integer.parseInt(inputLines.get(0).substring(inputLines.get(0).length() - 1)));
        Player p2 = new Player(Integer.parseInt(inputLines.get(1).substring(inputLines.get(1).length() - 1)));
        DeterministicDice dice = new DeterministicDice(100);

        // Day 21_1
//        play(p1, p2, dice);
//        if (p1.totalScore >= 1000) {
//            System.out.println(p2.totalScore * dice.totalRolls);
//        } else {
//            System.out.println(p1.totalScore * dice.totalRolls);
//        }

        // Day 21_2
        DiracDice diracDice = new DiracDice(p1, p2);
        boolean universesUpdated = true;
        int playerTurn = 1;
        while (universesUpdated) {
            universesUpdated = diracDice.playTurn(playerTurn);

            if (playerTurn == 1) {
                playerTurn = 2;
            } else {
                playerTurn = 1;
            }
        }

        System.out.println(Math.max(diracDice.player1Wins, diracDice.player2Wins));
    }
}
