package LeetCodeJava.Greedy;

// https://leetcode.com/problems/transform-array-to-all-equal-elements/description/

/**
 * 3576. Transform Array to All Equal Elements
 * Medium
 *
 * You are given an integer array nums of size n containing only 1 and -1, and an integer k.
 *
 * You can perform the following operation at most k times:
 *
 * Choose an index i (0 <= i < n - 1), and multiply both nums[i] and nums[i + 1] by -1.
 *
 * Note that you can choose the same index i more than once in different operations.
 *
 * Return true if it is possible to make all elements of the array equal after at most k operations, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,-1,1,-1,1], k = 3
 * Output: True
 * Explanation:
 * We can make all elements in the array equal in 2 operations as follows:
 * Choose index i = 1, and multiply both nums[1] and nums[2] by -1. Now nums = [1,1,-1,-1,1].
 * Choose index i = 2, and multiply both nums[2] and nums[3] by -1. Now nums = [1,1,1,1,1].
 *
 * Example 2:
 *
 * Input: nums = [-1,-1,-1,1,1,1], k = 5
 * Output: False
 * Explanation:
 * It is not possible to make all elements equal in at most 5 operations.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 * nums[i] is either -1 or 1.
 * 1 <= k <= n
 *
 */
public class TransformArrayToAllEqualElements {

    // V0
    // IDEA: FORCED LEFT-TO-RIGHT SWEEP, TRY BOTH TARGET SIGNS
    //       the operation at i flips nums[i] and nums[i + 1]. once the target sign is
    //       fixed, the decision at each i is forced: flip iff nums[i] (with any flip
    //       carried from i - 1) differs from the target. count the flips; the last
    //       element must match on its own.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean canMakeEqual(int[] nums, int k) {
        return canReach(nums, k, 1) || canReach(nums, k, -1);
    }

    /** True if every element can become `target` using at most k operations. */
    private boolean canReach(int[] nums, int k, int target) {
        int n = nums.length;
        int carriedFlip = 1; // -1 if the operation at i - 1 flipped nums[i]
        int used = 0;

        for (int i = 0; i < n - 1; i++) {
            int value = nums[i] * carriedFlip;
            if (value != target) {
                // must apply the operation at i, which also flips nums[i + 1]
                used++;
                if (used > k) {
                    return false;
                }
                carriedFlip = -1;
            } else {
                carriedFlip = 1;
            }
        }
        return nums[n - 1] * carriedFlip == target;
    }
}
