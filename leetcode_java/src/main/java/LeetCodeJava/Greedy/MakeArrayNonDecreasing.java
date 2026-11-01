package LeetCodeJava.Greedy;

// https://leetcode.com/problems/make-array-non-decreasing/description/

/**
 * 3523. Make Array Non-decreasing
 * Medium
 *
 * You are given an integer array nums. In one operation, you can select a subarray
 * and replace it with a single element equal to its maximum value.
 *
 * Return the maximum possible size of the array after performing zero or more
 * operations such that the resulting array is non-decreasing.
 *
 * Example 1:
 *
 * Input: nums = [4,2,5,3,5]
 *
 * Output: 3
 *
 * Explanation:
 *
 * One way to achieve the maximum size is:
 *
 * Replace subarray nums[1..2] = [2, 5] with 5 → [4, 5, 3, 5].
 *
 * Replace subarray nums[2..3] = [3, 5] with 5 → [4, 5, 5].
 *
 * The final array [4, 5, 5] is non-decreasing with size 3.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3]
 *
 * Output: 3
 *
 * Explanation:
 *
 * No operation is needed as the array [1,2,3] is already non-decreasing.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 10^5
 *
 * 1 <= nums[i] <= 2 * 10^5
 *
 */
public class MakeArrayNonDecreasing {

    // V0
    // IDEA: GREEDY — ABSORB EVERY DIP INTO THE BLOCK ON ITS LEFT
    //       operations partition nums into consecutive blocks, each collapsed to its max;
    //       we want as many blocks as possible with non-decreasing maxima.
    //       a value >= the current max starts a new block; a smaller one must be
    //       absorbed (which leaves that block's max unchanged).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumPossibleSize(int[] nums) {
        int currentMax = 0;
        int blocks = 0;
        for (int v : nums) {
            if (v >= currentMax) {
                blocks++;
                currentMax = v;
            }
        }
        return blocks;
    }
}
