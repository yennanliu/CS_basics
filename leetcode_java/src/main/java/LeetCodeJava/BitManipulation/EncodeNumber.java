package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/encode-number/description/

/**
 * 1256. Encode Number
 * Medium
 *
 * Given a non-negative integer num, Return its encoding string.
 *
 * The encoding is done by converting the integer to a string using a secret function that you
 * should deduce from the following table:
 *
 * n    f(n)
 * 0    ""
 * 1    "0"
 * 2    "1"
 * 3    "00"
 * 4    "01"
 * 5    "10"
 * 6    "11"
 * 7    "000"
 * 8    "001"
 *
 *
 * Example 1:
 *
 * Input: num = 23
 * Output: "1000"
 *
 * Example 2:
 *
 * Input: num = 107
 * Output: "101100"
 *
 *
 * Constraints:
 *
 * 0 <= num <= 10^9
 *
 */
public class EncodeNumber {

    // V0
    // IDEA: BINARY OF num + 1 WITH THE LEADING 1 DROPPED
    //   outputs of length L cover n = 2^L - 1 .. 2^(L+1) - 2, i.e. n + 1 lies in
    //   [2^L, 2^(L+1)): exactly the numbers with L + 1 binary digits. their binary form is a
    //   leading 1 followed by the L-bit offset inside the block, which is the encoding.
    /**
     * time = O(log N)
     * space = O(log N)
     */
    public String encode(int num) {
        // num + 1 can reach 1e9 + 1, still inside int range
        String binary = Integer.toBinaryString(num + 1);
        // drop the leading '1'
        return binary.substring(1);
    }
}
