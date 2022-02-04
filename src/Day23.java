import common.FileUtils;
import javafx.util.Pair;

import java.util.*;

class Amphipod {
    private static Map<Character, Integer> amphipodEnergyMap = new HashMap<Character, Integer>() {{
        put('A', 1);
        put('B', 10);
        put('C', 100);
        put('D', 1000);
    }};

    private static Map<Character, Integer> amphipodRoomNumMap = new HashMap<Character, Integer>() {{
        put('A', 1);
        put('B', 2);
        put('C', 3);
        put('D', 4);
    }};

    char name;
    int energy;
    int desiredRoomNum;

    public static Amphipod create(char name) {
        Amphipod a = new Amphipod();
        a.name = name;
        a.energy= amphipodEnergyMap.get(name);
        a.desiredRoomNum = amphipodRoomNumMap.get(name);
        return a;
    }

    public static boolean canStopAt(int position) {
        return position != 2 && position != 4 && position != 6 && position != 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amphipod amphipod = (Amphipod) o;
        return name == amphipod.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

class Room {
    Stack<Amphipod> amphipods = new Stack<>();
    int roomNum;

    public Room(int roomNum) {
        this.roomNum = roomNum;
    }

    public boolean isFinalState(int roomSize) {
        if (amphipods.size() < roomSize) {
            return false;
        }

        for (Amphipod a: amphipods) {
            if (a.desiredRoomNum != roomNum) {
                return false;
            }
        }

        return true;
    }

    public boolean allowsAmphipod(Amphipod a) {
        if (a.desiredRoomNum != roomNum) {
            return false;
        }
        for (Amphipod currentAmphipod: amphipods) {
            if (!currentAmphipod.equals(a)) {
                return false;
            }
        }

        return true;
    }

    public int occupancy() {
        return amphipods.size();
    }

    public int getPositionInHallway() {
        return 2 * roomNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNum == room.roomNum && Objects.equals(amphipods, room.amphipods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amphipods, roomNum);
    }

    public Room clone() {
        Room r = new Room(roomNum);
        r.amphipods = (Stack) amphipods.clone();
        return r;
    }
}

class BurrowState {
    Amphipod[] hallway = new Amphipod[11];
    Room[] rooms = new Room[4];
    int roomSize;

    public BurrowState(int roomSize) {
        this.roomSize = roomSize;

        // Create empty rooms with positions
        for (int roomIndex = 0; roomIndex < 4; roomIndex++) {
            rooms[roomIndex] = new Room(roomIndex + 1);
        }
    }

    // Checks if amphipod path from given position to the desired room is clear and room has space
    public boolean canMoveToRoom(Room r, int positionInHallway) {
        int startIndex = Math.min(positionInHallway, r.getPositionInHallway());
        int endIndex = Math.max(positionInHallway, r.getPositionInHallway());
        for (int position = startIndex; position <= endIndex; position++) {
            if (position != positionInHallway && hallway[position] != null) {
                return false;
            }
        }
        if (r.occupancy() >= roomSize) {
            return false;
        }
        return true;
    }

    // Checks if amphipod path from given room to position is clear
    public boolean canMoveFromRoom(Room r, int positionInHallway) {
        if (r.occupancy() == 0) {
            return false;
        }

        int startIndex = Math.min(positionInHallway, r.getPositionInHallway());
        int endIndex = Math.max(positionInHallway, r.getPositionInHallway());
        for (int position = startIndex; position <= endIndex; position++) {
            if (position != positionInHallway && hallway[position] != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BurrowState that = (BurrowState) o;
        return roomSize == that.roomSize && Arrays.equals(hallway, that.hallway) && Arrays.equals(rooms, that.rooms);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(roomSize);
        result = 31 * result + Arrays.hashCode(hallway);
        result = 31 * result + Arrays.hashCode(rooms);
        return result;
    }

    public BurrowState clone() {
        BurrowState b = new BurrowState(this.roomSize);
        for (int position = 0; position < hallway.length; position++) {
            Amphipod amphipodAtPos = hallway[position];
            if (amphipodAtPos != null) {
                b.hallway[position] = Amphipod.create(amphipodAtPos.name);
            }
        }
        for (int roomIndex = 0; roomIndex < rooms.length; roomIndex++) {
            Room currentRoom = rooms[roomIndex];
            b.rooms[roomIndex] = currentRoom.clone();
        }

        return b;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int lineNum = 1; lineNum <= roomSize + 3; lineNum++) {
            for (int j = 0; j < 13; j++) {
                if (lineNum == 1 || lineNum == roomSize + 3) {
                    sb.append('#');
                } else if (lineNum == 2) {
                    if (j == 0 || j == 12) {
                        sb.append("#");
                    } else {
                        Amphipod a = hallway[j - 1];
                        if (a != null) {
                            sb.append(a.name);
                        } else {
                            sb.append('.');
                        }
                    }
                } else {
                    if (lineNum == 3 && (j <= 1 || j >= 11)) {
                        sb.append('#');
                    } else if (j == 2 || j == 10) {
                        sb.append('#');
                    } else if (!Amphipod.canStopAt(j - 1)) {
                        Room r = rooms[j/2 - 1];
                        if (roomSize + 3 - r.occupancy() <= lineNum) {
                            int index = roomSize + 2 - lineNum;
                            Amphipod a = r.amphipods.get(index);
                            sb.append(a.name);
                        } else {
                            sb.append('.');
                        }
                    } else {
                        sb.append('#');
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

public class Day23 {
    private static BurrowState parseInput(List<String> inputLines) {
        BurrowState result = new BurrowState(inputLines.size() - 3);

        String hallwayLine = inputLines.get(1);
        for (int position = 0; position < 11; position++) {
            char amphipodName = hallwayLine.charAt(position + 1);
            if (amphipodName != '.') {
                result.hallway[position] = Amphipod.create(amphipodName);
            }
        }

        for (int lineNum = inputLines.size() - 2; lineNum >= 2 ; lineNum--) {
            String roomLine = inputLines.get(lineNum);
            for (int roomIndex = 0; roomIndex < 4; roomIndex++) {
                char amphipodName = roomLine.charAt((2 * roomIndex) + 3);
                if (amphipodName != '.') {
                    result.rooms[roomIndex].amphipods.push(Amphipod.create(amphipodName));
                }
            }
        }

        return result;
    }

    private static BurrowState getFinalBurrowState(int roomSize) {
        BurrowState result = new BurrowState(roomSize);

        for (int roomIndex = 0; roomIndex < 4; roomIndex++) {
            char amphipodName = 'A';
            amphipodName += roomIndex;
            for (int i = 0; i < roomSize; i++) {
                result.rooms[roomIndex].amphipods.push(Amphipod.create(amphipodName));
            }
        }

        return result;
    }

    /*
        Returns a list of valid Burrow states starting from given state, and the corresponding costs to get there
     */
    private static List<Pair<BurrowState, Long>> getNextValidBurrowStates(BurrowState b) {
        List<Pair<BurrowState, Long>> result = getValidMovesFromHallwayToRooms(b);

        // If there are valid moves from hallway to room, prioritize that
        if (!result.isEmpty()) {
            return result;
        }

        return getValidMovesFromRoomsToHallway(b);
    }

    private static List<Pair<BurrowState, Long>> getValidMovesFromHallwayToRooms(BurrowState b) {
        List<Pair<BurrowState, Long>> result = new ArrayList<>();

        for (int position = 0; position < b.hallway.length; position++) {
            Amphipod amphipodAtPos = b.hallway[position];
            if (amphipodAtPos != null) {
                Room desiredRoom = b.rooms[amphipodAtPos.desiredRoomNum - 1];
                if (desiredRoom.allowsAmphipod(amphipodAtPos) && b.canMoveToRoom(desiredRoom, position)) {
                    BurrowState newState = b.clone();
                    Amphipod amphipodToMove = newState.hallway[position];
                    Room roomToMoveTo = newState.rooms[desiredRoom.roomNum - 1];
                    // Clear hallway position and add to room
                    newState.hallway[position] = null;
                    roomToMoveTo.amphipods.push(amphipodToMove);

                    // Number of moves inside room
                    int numOfMovesInsideRoom = newState.roomSize - desiredRoom.occupancy();
                    // Number of moves from position in hallway to outside room
                    int numOfMovesToRoom = Math.abs(position - roomToMoveTo.getPositionInHallway());

                    Long cost = amphipodToMove.energy * (numOfMovesInsideRoom + numOfMovesToRoom * 1L);
                    result.add(new Pair<>(newState, cost));
                }
            }
        }

        return result;
    }

    /*
        Returns a list of valid Burrow states starting from the given state , for only moves from rooms to hallway
    */
    private static List<Pair<BurrowState, Long>> getValidMovesFromRoomsToHallway(BurrowState b) {
        List<Pair<BurrowState, Long>> result = new ArrayList<>();

        for (int position = 0; position < b.hallway.length; position++) {
            Amphipod amphipodAtPos = b.hallway[position];
            if (amphipodAtPos == null && Amphipod.canStopAt(position)) {
                for (Room r: b.rooms) {
                    // No move needed if room already has needed amphipods
                    if (!r.isFinalState(b.roomSize) && b.canMoveFromRoom(r, position)) {
                        // Create a new burrow state with move from room to hallway
                        BurrowState newState = b.clone();
                        Room roomToMoveFrom = newState.rooms[r.roomNum - 1];
                        Amphipod amphipodToMove = roomToMoveFrom.amphipods.pop();
                        newState.hallway[position] = amphipodToMove;

                        // Number of moves to reach outside room
                        int numOfMovesToExit = newState.roomSize - roomToMoveFrom.occupancy();
                        // Number of moves from outside room to new position in hallway
                        int numOfMovesToPos = Math.abs(position - roomToMoveFrom.getPositionInHallway());

                        Long cost = amphipodToMove.energy * (numOfMovesToExit + numOfMovesToPos * 1L);
                        result.add(new Pair<>(newState, cost));
                    }
                }
            }
        }

        return result;
    }

    private static void printPathToState(BurrowState b, Map<BurrowState, Pair<BurrowState, Long>> burrowStateToPrevStateMap) {
        Stack<Pair<BurrowState, Long>> result = new Stack<>();
        Pair<BurrowState, Long> prevBurrowStateAndCost = burrowStateToPrevStateMap.get(b);
        while (prevBurrowStateAndCost.getKey() != null) {
            result.push(new Pair<>(b, prevBurrowStateAndCost.getValue()));
            b = prevBurrowStateAndCost.getKey();
            prevBurrowStateAndCost = burrowStateToPrevStateMap.get(b);
        }
        result.push(new Pair<>(b, 0L));

        while (!result.empty()) {
            System.out.println(result.pop());
            System.out.println();
        }
    }

    private static void findMinEnergyPath(String fileName, boolean printPath) {
        List<String> inputLines = FileUtils.readLinesFromFile(fileName);
        BurrowState initialBurrowState = parseInput(inputLines);
        BurrowState finalBurrowState = getFinalBurrowState(inputLines.size() - 3);

        // Map each state to its cheapeast cost to get there and corresponding state path taken
        Map<BurrowState, Pair<BurrowState, Long>> burrowStateMap = new HashMap<>();
        burrowStateMap.put(initialBurrowState, new Pair<>(null, 0L));

        Queue<BurrowState> burrowStates = new LinkedList<>();
        burrowStates.offer(initialBurrowState);

        while (!burrowStates.isEmpty()) {
            BurrowState b = burrowStates.poll();
            Long costToState = burrowStateMap.get(b).getValue();

            for (Pair<BurrowState, Long> validBurrowStateWithCost: getNextValidBurrowStates(b)) {
                BurrowState validBurrowState = validBurrowStateWithCost.getKey();
                Long newCost = validBurrowStateWithCost.getValue();
                Pair<BurrowState, Long> currentMinPathAndCost = burrowStateMap.get(validBurrowState);

                if (currentMinPathAndCost == null || newCost + costToState < currentMinPathAndCost.getValue()) {
                    burrowStateMap.put(validBurrowState, new Pair<>(b, newCost + costToState));
                    burrowStates.offer(validBurrowState);
                }
            }
        }

        System.out.println(burrowStateMap.get(finalBurrowState).getValue());
        if (printPath) {
            printPathToState(finalBurrowState, burrowStateMap);
        }
    }

    public static void main(String[] args) {
        // Day23_1
        findMinEnergyPath("Day23.txt", false);

        // Daay23_2
        findMinEnergyPath("Day23_2.txt", false);
    }
}
