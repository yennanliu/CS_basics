package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/binary-gap/

/**
 *  868. Binary Gap
 *  Easy
 *
 *  Given a positive integer n, find and return the longest distance between any
 *  two adjacent 1's in the binary representation of n. If there are no two
 *  adjacent 1's, return 0.
 *
 *  Two 1's are adjacent if there are only 0's separating them (possibly no 0's).
 *  The distance between two 1's is the absolute difference between their bit
 *  positions.
 *
 *  Example 1:
 *  Input: n = 22
 *  Output: 2
 *  Explanation: 22 in binary is "10110". The first adjacent pair of 1's is
 *  "10110" with a distance of 2, the second is "10110" with a distance of 1.
 *
 *  Example 2:
 *  Input: n = 8
 *  Output: 0
 *  Explanation: 8 in binary is "1000". There are no adjacent pairs of 1's.
 *
 *  Example 3:
 *  Input: n = 5
 *  Output: 2
 *
 *  Constraints:
 *  1 <= n <= 10^9
 */
public class BinaryGap {

    // V0
    // IDEA: scan bit positions, remember the index of the previous set bit and
    //       track the max gap
    /**
     * time = O(32) = O(1)
     * space = O(1)
     */
    public int binaryGap(int n) {
        int last = -1;
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if (((n >> i) & 1) == 1) {
                if (last != -1) {
                    res = Math.max(res, i - last);
                }
                last = i;
            }
        }
        return res;
    }
}
