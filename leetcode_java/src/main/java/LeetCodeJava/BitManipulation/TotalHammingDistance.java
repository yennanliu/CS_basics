package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/total-hamming-distance/

/**
 *  477. Total Hamming Distance
 *  Medium
 *
 *  The Hamming distance between two integers is the number of positions at
 *  which the corresponding bits are different.
 *
 *  Given an integer array nums, return the sum of Hamming distances between all
 *  the pairs of the integers in nums.
 *
 *  Example 1:
 *  Input: nums = [4,14,2]
 *  Output: 6
 *  Explanation: In binary representation, 4 is 0100, 14 is 1110, and 2 is 0010.
 *  HammingDistance(4, 14) + HammingDistance(4, 2) + HammingDistance(14, 2)
 *   = 2 + 2 + 2 = 6.
 *
 *  Example 2:
 *  Input: nums = [4,14,4]
 *  Output: 4
 *
 *  Constraints:
 *  1 <= nums.length <= 10^4
 *  0 <= nums[i] <= 10^9
 *  The answer for the given input will fit in a 32-bit integer.
 */
public class TotalHammingDistance {

    // V0
    // IDEA: count per bit column - if k numbers have that bit set and (n - k)
    //       don't, that column contributes k * (n - k) to the total
    /**
     * time = O(32 * n)
     * space = O(1)
     */
    public int totalHammingDistance(int[] nums) {
        int n = nums.length;
        int res = 0;
        for (int bit = 0; bit < 32; bit++) {
            int ones = 0;
            for (int num : nums) {
                ones += (num >> bit) & 1;
            }
            res += ones * (n - ones);
        }
        return res;
    }
}
