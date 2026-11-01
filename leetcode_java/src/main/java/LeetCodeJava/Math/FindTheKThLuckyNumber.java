package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-k-th-lucky-number/description/

/**
 * 2802. Find The K-th Lucky Number
 * Medium
 *
 * We know that 4 and 7 are lucky digits. Also, a number is called lucky if it contains only lucky digits.
 *
 * You are given an integer k, return the kth lucky number represented as a string.
 *
 *
 * Example 1:
 *
 * Input: k = 4
 * Output: "47"
 *
 * Example 2:
 *
 * Input: k = 10
 * Output: "477"
 * Explanation: Here are lucky numbers sorted in increasing order:
 * 4, 7, 44, 47, 74, 77, 444, 447, 474, 477. So the 10th lucky number is 477.
 *
 * Example 3:
 *
 * Input: k = 1000
 * Output: "777747447"
 *
 *
 * Constraints:
 *
 * 1 <= k <= 10^9
 *
 */
public class FindTheKThLuckyNumber {

    // V0
    // IDEA: BINARY ENCODING (4 -> 0, 7 -> 1) -- there are 2^len lucky numbers of each length;
    //       peel whole length-blocks off k, then pick '4' (lower half) or '7' (upper half) digit by digit
    /**
     * time = O(log K)
     * space = O(log K)
     */
    public String kthLuckyNumber(int k) {
        long rank = k;

        // find the digit length: skip every block of 2^length numbers that k lies past
        int length = 1;
        long blockSize = 2;
        while (rank > blockSize) {
            rank -= blockSize;
            length++;
            blockSize *= 2;
        }

        // rank is now 1-based inside the block of length-digit lucky numbers
        StringBuilder result = new StringBuilder();
        for (int remainingDigits = length - 1; remainingDigits >= 0; remainingDigits--) {
            // how many numbers share each choice of the current digit
            long half = 1L << remainingDigits;
            if (rank <= half) {
                result.append('4');
            } else {
                result.append('7');
                rank -= half;
            }
        }
        return result.toString();
    }
}
