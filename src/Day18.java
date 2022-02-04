import common.FileUtils;

import java.util.*;

class SnailNumNode {
    SnailNumNode left;
    SnailNumNode right;
    SnailNumNode parent;
    Integer val;
    int depth;

    public boolean isRegularNum() {
        return this.val != null;
    }

    public String toString() {
        if (this.isRegularNum()) {
            return this.val.toString();
        } else {
            return "[" + this.left.toString() + "," + this.right.toString() + "]";
        }
    }
}

public class Day18 {
    private static SnailNumNode getSnailNumber(String inputLine) {
        SnailNumNode root = null;
        SnailNumNode curr = null;

        for (char c: inputLine.toCharArray()) {
            if (c == '[') {
                SnailNumNode s = new SnailNumNode();
                if (root == null) {
                    root = s;
                } else {
                    if (curr.left == null) {
                        curr.left = s;
                    } else {
                        curr.right = s;
                    }
                    s.parent = curr;
                    s.depth = curr.depth + 1;
                }
                curr = s;
            }
            if (c == ']') {
                curr = curr.parent;
            }
            if (c >= '0' && c <= '9') {
                SnailNumNode s = new SnailNumNode();
                s.val = c - '0';
                if (curr.left == null) {
                    curr.left = s;
                } else {
                    curr.right = s;
                }
                s.parent = curr;
                s.depth = curr.depth + 1;
            }
        }
        return root;
    }

    private static List<SnailNumNode> getInorderList(SnailNumNode node) {
        Stack<SnailNumNode> stack = new Stack<>();
        Set<SnailNumNode> visitedNodes = new HashSet<>();
        stack.add(node);

        List<SnailNumNode> result = new ArrayList<>();

        while (!stack.isEmpty()) {
            SnailNumNode s = stack.peek();
            visitedNodes.add(s);
            if (s.left != null && !visitedNodes.contains(s.left)) {
                stack.add(s.left);
            } else if (s.right != null && !visitedNodes.contains(s.right)) {
                stack.add(s.right);
            } else {
                if (s.isRegularNum()) {
                    result.add(s);
                }
                stack.pop();
            }
        }

        return result;
    }

    private static boolean explodeNode(SnailNumNode snailNumNode) {
        List<SnailNumNode> inOrderNums = getInorderList(snailNumNode);
        boolean isExploded = false;
        SnailNumNode leftNode = null, rightNode = null, explodeNode = null;

        Iterator<SnailNumNode> inOrderNumsIt = inOrderNums.iterator();

        while (!isExploded && inOrderNumsIt.hasNext()) {
            SnailNumNode s = inOrderNumsIt.next();
            if (s.depth > 4) {
                isExploded = true;
                explodeNode = s.parent;
                // Skip right element
                inOrderNumsIt.next();
            } else {
                leftNode = s;
            }
        }

        if (inOrderNumsIt.hasNext()) {
            rightNode = inOrderNumsIt.next();
        }

        if (isExploded) {
            if (leftNode != null) {
                leftNode.val += explodeNode.left.val;
            }
            if (rightNode != null) {
                rightNode.val += explodeNode.right.val;
            }
            explodeNode.left = null;
            explodeNode.right = null;
            explodeNode.val = 0;
        }

        return isExploded;
    }

    private static boolean splitNode(SnailNumNode snailNumNode) {
        List<SnailNumNode> inOrderNums = getInorderList(snailNumNode);

        for (SnailNumNode s: inOrderNums) {
            if (s.val >= 10) {
                int mod = s.val % 2;
                int q = s.val / 2;

                SnailNumNode left = new SnailNumNode();
                s.left = left;
                left.parent = s;
                left.val = q;
                left.depth = s.depth + 1;

                SnailNumNode right = new SnailNumNode();
                s.right = right;
                right.parent = s;
                right.val = q + mod;
                right.depth = s.depth + 1;

                s.val = null;

                return true;
            }
        }

        return false;
    }

    private static void reduceSnailNum(SnailNumNode snailNumNode) {
        while (explodeNode(snailNumNode) || splitNode(snailNumNode)) {
            while (explodeNode(snailNumNode));
            splitNode(snailNumNode);
        }
    }

    private static void printSnailNum(SnailNumNode node) {
        Stack<SnailNumNode> stack = new Stack<>();
        Set<SnailNumNode> visitedNodes = new HashSet<>();
        stack.add(node);

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            SnailNumNode s = stack.peek();
            visitedNodes.add(s);
            if (s.left != null && !visitedNodes.contains(s.left)) {
                sb.append("[");
                stack.add(s.left);
            } else if (s.left == null && s.right == null) {
                sb.append(s.val);
                stack.pop();
            } else if (s.right != null && !visitedNodes.contains(s.right)) {
                sb.append(",");
                stack.add(s.right);
            } else {
                sb.append("]");
                stack.pop();
            }
        }

        System.out.println(sb);
    }

    private static void incrementDepths(SnailNumNode snailNumNode) {
        Stack<SnailNumNode> stack = new Stack<>();
        Set<SnailNumNode> visitedNodes = new HashSet<>();
        stack.add(snailNumNode);

        while (!stack.isEmpty()) {
            SnailNumNode s = stack.peek();
            visitedNodes.add(s);
            if (s.left != null && !visitedNodes.contains(s.left)) {
                stack.add(s.left);
            } else if (s.right != null && !visitedNodes.contains(s.right)) {
                stack.add(s.right);
            } else {
                s.depth++;
                stack.pop();
            }
        }
    }

    private static SnailNumNode cloneSnailNum(SnailNumNode snailNumNode) {
        SnailNumNode result = new SnailNumNode();
        result.depth = snailNumNode.depth;
        result.val = snailNumNode.val;

        if (snailNumNode.left != null) {
            result.left = cloneSnailNum(snailNumNode.left);
            result.left.parent = result;
        }
        if (snailNumNode.right != null) {
            result.right = cloneSnailNum(snailNumNode.right);
            result.right.parent = result;
        }

        return result;
    }

    private static SnailNumNode addSnailNums(SnailNumNode s1, SnailNumNode s2) {
        s1 = cloneSnailNum(s1);
        s2 = cloneSnailNum(s2);

        SnailNumNode result = new SnailNumNode();
        result.left = s1;
        s1.parent = result;
        result.right = s2;
        s2.parent = result;

        incrementDepths(s1);
        incrementDepths(s2);

        reduceSnailNum(result);

        return result;
    }

    private static Long getMagnitude(SnailNumNode snailNumNode) {
        if (snailNumNode.isRegularNum()) {
            return snailNumNode.val.longValue();
        } else {
            return 3 * getMagnitude(snailNumNode.left) + 2 * getMagnitude(snailNumNode.right);
        }
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day18.txt");
        List<SnailNumNode> snailNumbers = new ArrayList<>();

        for (String inputLine: inputLines) {
            snailNumbers.add(getSnailNumber(inputLine));
        }

        // Day18_1
        SnailNumNode firstElem = snailNumbers.remove(0);
        SnailNumNode result = firstElem;
        for (SnailNumNode s: snailNumbers) {
            result = addSnailNums(result, s);
        }
        System.out.println(result.toString());
        System.out.println(getMagnitude(result));

        // Day18_2
        snailNumbers.add(firstElem);
        Long maxMagnitude = Long.MIN_VALUE;
        for (int i = 0; i < snailNumbers.size(); i++) {
            for (int j = i + 1; j < snailNumbers.size(); j++) {
                SnailNumNode s1 = snailNumbers.get(i);
                SnailNumNode s2 = snailNumbers.get(j);
                maxMagnitude = Math.max(maxMagnitude, getMagnitude(addSnailNums(s1, s2)));
                maxMagnitude = Math.max(maxMagnitude, getMagnitude(addSnailNums(s2, s1)));
            }
        }
        System.out.println(maxMagnitude);
    }
}
