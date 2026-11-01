package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-number-of-increasing-subsequence-to-be-removed/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3231. Minimum Number of Increasing Subsequence to Be Removed
 * Hard
 *
 * 🔒 (premium)
 *
 * Given an array of integers nums, you are allowed to perform the following operation any number of times:
 *
 * Remove a strictly increasing subsequence from the array.
 *
 * Your task is to find the minimum number of operations required to make the array empty.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,3,1,4,2]
 * Output: 3
 * Explanation:
 * We remove subsequences [1, 2], [3, 4], [5].
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 1
 * Explanation:
 * We remove the subsequence [1, 2, 3, 4, 5].
 *
 * Example 3:
 *
 * Input: nums = [5,4,3,2,1]
 * Output: 5
 * Explanation:
 * We remove the subsequences [5], [4], [3], [2], [1].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class MinimumNumberOfIncreasingSubsequenceToBeRemoved {

    // V0
    // IDEA: DILWORTH -> LONGEST NON-INCREASING SUBSEQUENCE (PATIENCE SORTING)
    // each removal takes one strictly increasing subsequence (a chain), so the answer is the
    // minimum chain cover, which by Dilworth's theorem equals the largest antichain: the
    // longest NON-INCREASING subsequence. Compute it with the tails array: tails[len - 1] is
    // the largest possible last value of a non-increasing subsequence of length len.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minOperations(int[] nums) {
        // tails is non-increasing
        List<Integer> tails = new ArrayList<>();
        for (int value : nums) {
            // first pile whose tail is strictly smaller than value (equal values may extend a pile)
            int left = 0;
            int right = tails.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) < value) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            if (left == tails.size()) {
                tails.add(value);
            } else {
                tails.set(left, value);
            }
        }
        return tails.size();
    }
}
