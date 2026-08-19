package LeetCodeJava.Heap;

// https://leetcode.com/problems/maximize-sum-of-at-most-k-distinct-elements/

import java.util.Arrays;

/**
 *  3684. Maximize Sum of At Most K Distinct Elements
 *  Easy
 *
 *  You are given a positive integer array nums and an integer k.
 *
 *  Choose at most k elements from nums so that their sum is maximized.
 *  However, the chosen numbers must be distinct.
 *
 *  Return an array containing the chosen numbers in strictly descending order.
 *
 *  Example 1:
 *    Input: nums = [84,93,100,77,90], k = 3
 *    Output: [100,93,90]
 *
 *  Example 2:
 *    Input: nums = [84,93,100,77,93], k = 3
 *    Output: [100,93,84]
 *    Explanation: we cannot pick 93 twice, the chosen numbers must be distinct.
 *
 *  Example 3:
 *    Input: nums = [1,1,1,2,2,2], k = 6
 *    Output: [2,1]
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    1 <= nums[i] <= 10^9
 *    1 <= k <= nums.length
 */
public class MaximizeSumOfAtMostKDistinctElements {

    // V0
    // IDEA: DEDUPE, THEN GREEDILY TAKE THE k LARGEST DISTINCT VALUES
    //       "distinct" makes duplicates worthless, so the universe of choices is
    //       set(nums). all values are positive -> adding any unused value strictly
    //       increases the sum, so take the largest min(k, #distinct) of them.
    //       sorting descending already gives the required output order.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] maxKDistinct(int[] nums, int k) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted); // ascending

        int[] res = new int[Math.min(k, sorted.length)];
        int cnt = 0;
        for (int i = sorted.length - 1; i >= 0 && cnt < k; i--) {
            // skip duplicates (scanning from the largest downwards)
            if (i < sorted.length - 1 && sorted[i] == sorted[i + 1]) {
                continue;
            }
            res[cnt++] = sorted[i];
        }

        return cnt == res.length ? res : Arrays.copyOf(res, cnt);
    }
}
