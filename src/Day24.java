import common.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

class Operation {
    String ins;
    List<String> operands;

    public Operation(String ins, List<String> operands) {
        this.ins = ins;
        this.operands = operands;
    }
}

class Variable {
    char var;
    int value = 0;

    public Variable(char var) {
        this.var = var;
    }

    public  Variable(char var, int value) {
        this.var = var;
        this.value = value;
    }
}

/*
Note: This solution uses some concrete analysis of my personal input.
# It is not guaranteed to work on every distributed input.
#
# The input consists of 14 different subprograms.
# Every subprogram can be categorized into two different types (type 1 or type 2).
#
# Type 1 programs change the value of `z` by the following rule
#    z = 26 * z + input + some_number
# where `some_number` are the values in `add_to_z`
#
# Type 2 programs change the value of `z` by the rule
#    z = z ÷ 26
# iff
#    z % 26 + some_number == input    (*)
# where `some_number` is `add_to_x`
#
# To achieve a `z` value of 0 at the end, we must make sure that type 2 programs
# always reduce the value of `z` (by satisfying (*))
# The values of type 1 programs need to be guessed. Here I use a recursive backtracking search.
 */
class Program {
    int type = 2;
    int addToX;
    int addToY;
    int programIndex;
}

public class Day24 {
    private static Variable w = new Variable('w');
    private static Variable x = new Variable('x');
    private static Variable y = new Variable('y');
    private static Variable z = new Variable('z');
    private static Map<String, Variable> operandVarMap = new HashMap<String, Variable>() {{
        put("w", w);
        put("x", x);
        put("y", y);
        put("z", z);
    }};

    private static void resetVars() {
        w.value = 0;
        x.value = 0;
        y.value = 0;
        z.value = 0;
    }

    private static Queue<Integer> getDigits(Long number) {
        Queue<Integer> result = new LinkedList<>();
        char[] numberChars = number.toString().toCharArray();

        for (char c: numberChars) {
            result.add(c - '0');
        }
        return result;
    }

    private static Integer parseOperand(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ne){
            return null;
        }
    }

    private static boolean isValidModelNumber(Long modelNumber, List<Operation> operations) {
        Queue<Integer> modelNumberDigits = getDigits(modelNumber);

        if (modelNumberDigits.contains(0)) {
            return false;
        }

        resetVars();

        for (Operation o: operations) {
            Variable v = operandVarMap.get(o.operands.get(0));
            Integer operandVal = null;
            if (!"inp".equals(o.ins)) {
                String operand = o.operands.get(1);
                operandVal = parseOperand(operand);
                if (operandVal == null) {
                    operandVal = operandVarMap.get(operand).value;
                }
            }
            switch (o.ins) {
                case "inp":
                    v.value = modelNumberDigits.poll();
                    break;
                case "add":
                    v.value += operandVal;
                    break;
                case "mul":
                    v.value *= operandVal;
                    break;
                case "div":
                    if (operandVal == 0) {
                        return false;
                    }
                    v.value /= operandVal;
                    break;
                case "mod":
                    if (v.value < 0 || operandVal < 0) {
                        return false;
                    }
                    v.value %= operandVal;
                    break;
                case "eql":
                    v.value = v.value == operandVal ? 1 : 0;
                    break;
            }
        }

        if (z.value == 0) {
            return true;
        } else {
            return false;
        }
    }

    private static Operation parseInput(String inputLine) {
        List<String> splitInput = new ArrayList<>(Arrays.asList(inputLine.split("\\s+")));
        String instruction = splitInput.get(0);
        splitInput.remove(0);
        return new Operation(instruction, splitInput);
    }

    private static List<Program> parseOperations(List<Operation> operations) {
        List<Program> result = new ArrayList<>();

        /*
            Each program contains 18 lines.
            Line 5 decides program type - 1 or 2.
            Line 6 and 16 have addToX and addToY values
            There are total of 14 programs
         */

        for (int i = 0; i < 14; i++) {
            Program p = new Program();
            p.programIndex = i;
            result.add(p);
            if (operations.get(18 * i + 4).operands.get(1).equals("1")) {
                p.type = 1;
            }
            p.addToX = parseOperand(operations.get(18 * i + 5).operands.get(1));
            p.addToY = parseOperand(operations.get(18 * i + 15).operands.get(1));
        }

        return result;
    }

    /*
        For Type 2 programs, z = prevZ/26, which is 26 * prevPrevZ + prevInput + prevY.
        For program to be type2, it must satisfy z % 26 + X == input. Substituting above we get (prevInput + prevY) % 26  + X == input,
        as 26 * prevPrevZ % 26 is 0.
        Assuming (prevInput + prevY) < 26, we get prevInput + prevY + X == input. To maximize inputs, we strive for input and prevInput to be close to 9 and 1 for vice-versa.
        We set prevInput to targetDigit and check if prevInput + prevY + X is valid, that is value is between 1 and 9. Else, we set input to targetDigit and compute prevInput from it.
    */
    private static Long findModelNumber(List<Program> programs, int targetDigit) {
        Stack<Program> typeOnePrograms = new Stack<>();
        int[] modelNumber = new int[14];
        for (Program p: programs) {
            if (p.type == 1) {
                typeOnePrograms.push(p);
            } else {
                Program prevProgram = typeOnePrograms.pop();
                int prevYPlusX = prevProgram.addToY + p.addToX;
                int potentialCurrProgramInput = targetDigit + prevYPlusX;
                // Valid Digit
                if (potentialCurrProgramInput >=1 && potentialCurrProgramInput <= 9) {
                    modelNumber[prevProgram.programIndex] = targetDigit;
                    modelNumber[p.programIndex] = targetDigit + prevYPlusX;
                } else {
                    modelNumber[p.programIndex] = targetDigit;
                    modelNumber[prevProgram.programIndex] = targetDigit - prevYPlusX;
                }
            }
        }

        StringBuilder modelNumberString = new StringBuilder();
        Arrays.stream(modelNumber).forEach(modelNumberString::append);
        return Long.parseLong(modelNumberString.toString());
    }

    public static void main(String[] args) {
        List<String> inputLines = FileUtils.readLinesFromFile("Day24.txt");
        List<Operation> operations = inputLines.stream().map(Day24::parseInput).collect(Collectors.toList());
        List<Program> programs = parseOperations(operations);

        // Day24_1
        Long modelNumber = findModelNumber(programs, 9);
        System.out.println(modelNumber);
        // Verify
        System.out.println(isValidModelNumber(modelNumber, operations));

        // Day24_2
        Long modelNumber2 = findModelNumber(programs, 1);
        System.out.println(modelNumber2);
        // Verify
        System.out.println(isValidModelNumber(modelNumber2, operations));
    }
}
