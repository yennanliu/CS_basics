package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-xor-of-subsequences/description/

/**
 * 3681. Maximum XOR of Subsequences
 * Hard
 *
 * You are given an integer array nums of length n where each element is a
 * non-negative integer.
 *
 * Select two subsequences of nums (they may be empty and are allowed to
 * overlap), each preserving the original order of elements, and let:
 *
 * X be the bitwise XOR of all elements in the first subsequence.
 * Y be the bitwise XOR of all elements in the second subsequence.
 *
 * Return the maximum possible value of X XOR Y.
 *
 * Note: The XOR of an empty subsequence is 0.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: 3
 * Explanation:
 * Choose subsequences:
 * First subsequence [2], whose XOR is 2.
 * Second subsequence [2,3], whose XOR is 1.
 * Then, XOR of both subsequences = 2 XOR 1 = 3.
 * This is the maximum XOR value achievable from any two subsequences.
 *
 * Example 2:
 *
 * Input: nums = [5,2]
 * Output: 7
 * Explanation:
 * Choose subsequences:
 * First subsequence [5], whose XOR is 5.
 * Second subsequence [2], whose XOR is 2.
 * Then, XOR of both subsequences = 5 XOR 2 = 7.
 * This is the maximum XOR value achievable from any two subsequences.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^9
 *
 */
public class MaximumXOROfSubsequences {

    // V0
    // IDEA: XOR LINEAR BASIS — THE TWO SUBSEQUENCES COLLAPSE TO ONE SUBSET
    //       X ^ Y is the xor of the symmetric difference, and any subset is reachable
    //       (put it all in the first pick), so the answer is the max subset xor.
    //       gaussian elimination into a basis indexed by leading bit, then greedily
    //       xor in each basis vector from the top bit down when it raises the value.
    /**
     * time = O(N * log C)
     * space = O(log C)
     */
    public int maxXorSubsequences(int[] nums) {
        int[] basis = new int[BITS]; // basis[b] = vector whose highest set bit is b, or 0

        for (int num : nums) {
            int x = num;
            for (int bit = BITS - 1; bit >= 0 && x != 0; bit--) {
                if ((x & (1 << bit)) == 0) {
                    continue;
                }
                if (basis[bit] == 0) {
                    basis[bit] = x; // new independent vector
                    break;
                }
                x ^= basis[bit];    // eliminate this leading bit
            }
        }

        // the vector owning bit b is the only one that can still flip bit b
        int best = 0;
        for (int bit = BITS - 1; bit >= 0; bit--) {
            if ((best ^ basis[bit]) > best) {
                best ^= basis[bit];
            }
        }
        return best;
    }

    private static final int BITS = 31; // values fit in a non-negative int
}
