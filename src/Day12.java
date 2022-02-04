import common.FileUtils;

import java.util.*;

class Node {
    String name;
    List<Node> connectedNodes = new ArrayList<>();

    public boolean isSmallCave() {
        if ("start".equals(this.name) || "end".equals(this.name)) {
            return false;
        }

        for (char c: this.name.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

public class Day12 {
    private static Map<String, Node> nameToNodeMap = new HashMap<>();

    private static void connectNodes(Node a, Node b) {
        a.connectedNodes.add(b);
        b.connectedNodes.add(a);
    }

    private static void createGraph(List<String> inputLines) {
        for (String inputLine: inputLines) {
            String[] nodeStrings = inputLine.split("-");
            for (String nodeString: nodeStrings) {
                if (!nameToNodeMap.containsKey(nodeString)) {
                    Node n = new Node();
                    n.name = nodeString;
                    nameToNodeMap.put(nodeString, n);
                }
            }

            connectNodes(nameToNodeMap.get(nodeStrings[0]), nameToNodeMap.get(nodeStrings[1]));
        }
    }

    private static boolean hasDuplicateSmallCaves(List<Node> path) {
        Set<Node> smallCavesSet = new HashSet<>();

        for (Node n: path) {
            if (n.isSmallCave()) {
                if (smallCavesSet.contains(n)) {
                    return true;
                }
                smallCavesSet.add(n);
            }
        }
        return false;
    }

    private static void findPaths2(Node start, List<Node> path, List<List<Node>> paths) {
        path.add(start);

        if ("end".equals(start.name)) {
            paths.add(path);
            return;
        }

        for (Node n: start.connectedNodes) {
            if (!"start".equals(n.name) && !(n.isSmallCave() && hasDuplicateSmallCaves(path) && path.contains(n))) {
                List<Node> resultPath = new ArrayList<>(path);
                findPaths2(n, resultPath, paths);
            }
        }
    }

    private static void findPaths(Node node, List<Node> path, List<List<Node>> paths) {
        path.add(node);

        if ("end".equals(node.name)) {
            paths.add(path);
            return;
        }

        for (Node n: node.connectedNodes) {
            if (!"start".equals(n.name) && !(n.isSmallCave() && path.contains(n))) {
                List<Node> resultPath = new ArrayList<>(path);
                findPaths(n, resultPath, paths);
            }
        }
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day12.txt");
        createGraph(inputLines);

        // Day12_1
        List<List<Node>> paths = new ArrayList<>();
        findPaths(nameToNodeMap.get("start"), new ArrayList<>(), paths);
        System.out.println(paths.size());

        // Day12_2
        List<List<Node>> paths2 = new ArrayList<>();
        findPaths2(nameToNodeMap.get("start"), new ArrayList<>(), paths2);
        System.out.println(paths2.size());
    }
}
