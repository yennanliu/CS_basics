package LeetCodeJava.Sort;

// https://leetcode.com/problems/largest-number-after-digit-swaps-by-parity/

/**
 *  2231. Largest Number After Digit Swaps by Parity
 *  Easy
 *
 *  You are given a positive integer num. You may swap any two digits of num that
 *  have the same parity (i.e. both odd digits or both even digits).
 *
 *  Return the largest possible value of num after any number of swaps.
 *
 *  Example 1:
 *    Input: num = 1234
 *    Output: 3412
 *    Explanation: Swap the digit 3 with the digit 1 -> 3214. Swap the digit 2 with
 *                 the digit 4 -> 3412. We may not swap 4 with 1 (different parity).
 *
 *  Example 2:
 *    Input: num = 65875
 *    Output: 87655
 *
 *  Constraints:
 *    1 <= num <= 10^9
 */
public class LargestNumberAfterDigitSwapsByParity {

    // V0
    // IDEA: BUCKET COUNT PER PARITY, THEN REFILL LEFT-TO-RIGHT WITH THE LARGEST
    //       swaps are unrestricted WITHIN a parity class, so the multiset of odd
    //       digits can be permuted freely among the positions that currently hold
    //       an odd digit (same for even). to maximise the number, put the largest
    //       remaining digit of the matching parity at each position, left to right.
    /**
     * time = O(D * 10)     // D = number of digits (<= 10)
     * space = O(D)
     */
    public int largestInteger(int num) {
        char[] digits = String.valueOf(num).toCharArray();

        // cnt[d] = how many times digit d appears
        int[] cnt = new int[10];
        for (char c : digits) {
            cnt[c - '0']++;
        }

        int res = 0;
        for (char c : digits) {
            int parity = (c - '0') % 2;
            // pick the biggest unused digit with the same parity
            for (int d = 9; d >= 0; d--) {
                if (d % 2 == parity && cnt[d] > 0) {
                    cnt[d]--;
                    res = res * 10 + d;
                    break;
                }
            }
        }
        return res;
    }
}
