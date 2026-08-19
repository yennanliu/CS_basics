package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/hamming-distance/

/**
 *  461. Hamming Distance
 *  Easy
 *
 *  The Hamming distance between two integers is the number of positions at
 *  which the corresponding bits are different.
 *
 *  Given two integers x and y, return the Hamming distance between them.
 *
 *  Example 1:
 *  Input: x = 1, y = 4
 *  Output: 2
 *  Explanation:
 *  1   (0 0 0 1)
 *  4   (0 1 0 0)
 *         ^   ^
 *
 *  Example 2:
 *  Input: x = 3, y = 1
 *  Output: 1
 *
 *  Constraints:
 *  0 <= x, y <= 2^31 - 1
 */
public class HammingDistance {

    // V0
    // IDEA: XOR gives 1 on differing bits -> count set bits of x ^ y
    /**
     * time = O(32) = O(1)
     * space = O(1)
     */
    public int hammingDistance(int x, int y) {
        int z = x ^ y;
        int cnt = 0;
        while (z != 0) {
            z &= (z - 1); // drop lowest set bit
            cnt++;
        }
        return cnt;
    }
}
