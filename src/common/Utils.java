package common;

public class Utils {
    public static long convertBinaryToLong(String binary) {
        long ret = 0;
        for (int i = binary.length() - 1; i >= 0; i--) {
            int bit = Integer.parseInt(binary.substring(i, i + 1));
            if (bit == 1) {
                ret += Math.pow(2, binary.length() - i - 1);
            }
        }
        return ret;
    }
}
