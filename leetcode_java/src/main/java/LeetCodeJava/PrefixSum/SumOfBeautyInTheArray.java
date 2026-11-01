package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/sum-of-beauty-in-the-array/description/

/**
 * 2012. Sum of Beauty in the Array
 * Medium
 *
 * You are given a 0-indexed integer array nums. For each index i (1 <= i <= nums.length - 2)
 * the beauty of nums[i] equals:
 *
 * 2, if nums[j] < nums[i] < nums[k], for all 0 <= j < i and for all i < k <= nums.length - 1.
 * 1, if nums[i - 1] < nums[i] < nums[i + 1], and the previous condition is not satisfied.
 * 0, if none of the previous conditions holds.
 *
 * Return the sum of beauty of all nums[i] where 1 <= i <= nums.length - 2.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: 2
 * Explanation: For each index i in the range 1 <= i <= 1:
 * - The beauty of nums[1] equals 2.
 *
 * Example 2:
 *
 * Input: nums = [2,4,6,4]
 * Output: 1
 * Explanation: For each index i in the range 1 <= i <= 2:
 * - The beauty of nums[1] equals 1.
 * - The beauty of nums[2] equals 0.
 *
 * Example 3:
 *
 * Input: nums = [3,2,1]
 * Output: 0
 * Explanation: For each index i in the range 1 <= i <= 1:
 * - The beauty of nums[1] equals 0.
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class SumOfBeautyInTheArray {

    // V0
    // IDEA: PREFIX MAX (running) + SUFFIX MIN (array)
    // beauty 2 needs nums[i] > every element on its left and < every element on its right,
    // i.e. leftMax < nums[i] < rightMin[i + 1]. precompute the suffix min once, keep the prefix max
    // as we scan, and each index is then checked in O(1). (re-scanning both sides per index is O(N^2).)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int sumOfBeauties(int[] nums) {
        int n = nums.length;
        // edge case: no middle index to score
        if (n < 3) {
            return 0;
        }

        // suffixMin[i] = min of nums[i .. n-1]
        int[] suffixMin = new int[n];
        suffixMin[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suffixMin[i] = Math.min(suffixMin[i + 1], nums[i]);
        }

        int totalBeauty = 0;
        // leftMax = max of nums[0 .. i-1]
        int leftMax = nums[0];
        for (int i = 1; i <= n - 2; i++) {
            if (leftMax < nums[i] && nums[i] < suffixMin[i + 1]) {
                // greater than everything on the left, smaller than everything on the right
                totalBeauty += 2;
            } else if (nums[i - 1] < nums[i] && nums[i] < nums[i + 1]) {
                // only the neighbours are in order
                totalBeauty += 1;
            }
            leftMax = Math.max(leftMax, nums[i]);
        }
        return totalBeauty;
    }
}
