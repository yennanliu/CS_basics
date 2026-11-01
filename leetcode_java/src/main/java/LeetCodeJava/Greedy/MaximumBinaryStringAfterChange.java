package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-binary-string-after-change/description/

/**
 * 1702. Maximum Binary String After Change
 * Medium
 *
 * You are given a binary string binary consisting of only 0's or 1's. You can apply
 * each of the following operations any number of times:
 *
 * Operation 1: If the number contains the substring "00", you can replace it with
 * "10". For example, "00010" -> "10010"
 * Operation 2: If the number contains the substring "10", you can replace it with
 * "01". For example, "00010" -> "00001"
 *
 * Return the maximum binary string you can obtain after any number of operations.
 * Binary string x is greater than binary string y if x's decimal representation is
 * greater than y's decimal representation.
 *
 *
 * Example 1:
 *
 * Input: binary = "000110"
 * Output: "111011"
 *
 * Example 2:
 *
 * Input: binary = "01"
 * Output: "01"
 * Explanation: "01" cannot be transformed any further.
 *
 *
 * Constraints:
 *
 * 1 <= binary.length <= 10^5
 * binary consist of '0' and '1'.
 *
 */
public class MaximumBinaryStringAfterChange {

    // V0
    // IDEA: GREEDY / INVARIANT (OP 2 GATHERS THE ZEROS, OP 1 EATS ALL BUT ONE)
    //       the leading run of 1s is frozen. Op 2 slides every later zero left
    //       until all zeros sit together after that run; op 1 then turns z zeros
    //       into z-1 ones and one zero. So the answer is all 1s with exactly one
    //       0, at index (first zero) + (zeros after the first zero).
    /**
     * time = O(N)
     * space = O(N)
     */
    public String maximumBinaryString(String binary) {
        int n = binary.length();
        int firstZero = binary.indexOf('0');
        // no zero at all: already the maximum
        if (firstZero == -1) {
            return binary;
        }

        int zerosAfterFirst = 0;
        for (int i = firstZero + 1; i < n; i++) {
            if (binary.charAt(i) == '0') {
                zerosAfterFirst++;
            }
        }

        // the single surviving zero ends up as far right as the zeros can push it
        int zeroIndex = firstZero + zerosAfterFirst;
        char[] result = new char[n];
        for (int i = 0; i < n; i++) {
            result[i] = '1';
        }
        result[zeroIndex] = '0';
        return new String(result);
    }
}
