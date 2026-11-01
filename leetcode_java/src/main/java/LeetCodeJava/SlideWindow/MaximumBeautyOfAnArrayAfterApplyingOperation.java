package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-beauty-of-an-array-after-applying-operation/description/

import java.util.Arrays;

/**
 * 2779. Maximum Beauty of an Array After Applying Operation
 * Medium
 *
 * You are given a 0-indexed array nums and a non-negative integer k.
 *
 * In one operation, you can do the following:
 *
 * Choose an index i that hasn't been chosen before from the range [0, nums.length - 1].
 * Replace nums[i] with any integer from the range [nums[i] - k, nums[i] + k].
 *
 * The beauty of the array is the length of the longest subsequence consisting of equal elements.
 *
 * Return the maximum possible beauty of the array nums after applying the operation any number
 * of times.
 *
 * Note that you can apply the operation to each index only once.
 *
 * A subsequence of an array is a new array generated from the original array by deleting some
 * elements (possibly none) without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,6,1,2], k = 2
 * Output: 3
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1], k = 10
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i], k <= 10^5
 *
 */
public class MaximumBeautyOfAnArrayAfterApplyingOperation {

    // V0
    // IDEA: SORT + SLIDING WINDOW OF WIDTH 2k
    //  a group can be made equal iff the intervals [v - k, v + k] share a point, i.e.
    //  max - min <= 2k; a subsequence ignores order, so sort and take the widest window.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maximumBeauty(int[] nums, int k) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int span = 2 * k;
        int best = 0;
        int left = 0;
        for (int right = 0; right < sorted.length; right++) {
            // shrink until every value in [left, right] fits in one 2k-wide range
            while (sorted[right] - sorted[left] > span) {
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
