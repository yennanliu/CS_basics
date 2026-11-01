package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-subsequence-in-non-increasing-order/description/

import java.util.*;

/**
 * 1403. Minimum Subsequence in Non-Increasing Order
 * Easy
 *
 * Given the array nums, obtain a subsequence of the array whose sum of elements is
 * strictly greater than the sum of the non included elements in such subsequence.
 *
 * If there are multiple solutions, return the subsequence with minimum size and if
 * there still exist multiple solutions, return the subsequence with the maximum
 * total sum of all its elements. A subsequence of an array can be obtained by
 * erasing some (possibly zero) elements from the array.
 *
 * Note that the solution with the given constraints is guaranteed to be unique.
 * Also return the answer sorted in non-increasing order.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,3,10,9,8]
 * Output: [10,9]
 * Explanation: The subsequences [10,9] and [10,8] are minimal such that the sum of
 * their elements is strictly greater than the sum of elements not included.
 * However, the subsequence [10,9] has the maximum total sum of its elements.
 *
 * Example 2:
 *
 * Input: nums = [4,4,7,6,7]
 * Output: [7,7,6]
 * Explanation: The subsequence [7,7] has the sum of its elements equal to 14 which
 * is not strictly greater than the sum of elements not included (14 = 4 + 4 + 6).
 * Therefore, the subsequence [7,6,7] is the minimal satisfying the conditions.
 * Note the subsequence has to be returned in non-increasing order.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 500
 * 1 <= nums[i] <= 100
 *
 */
public class MinimumSubsequenceInNonIncreasingOrder {

    // V0
    // IDEA: GREEDY + SORT (take the largest elements until the taken sum beats the rest)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<Integer> minSubsequence(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }

        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        // taking the largest first maximises the sum for every size, so the first
        // moment taken > rest gives both the minimum size and the maximum sum
        List<Integer> result = new ArrayList<>();
        int taken = 0;
        for (int i = sorted.length - 1; i >= 0; i--) {
            taken += sorted[i];
            result.add(sorted[i]);
            if (taken > total - taken) {
                break;
            }
        }
        // built from largest to smallest, so it is already non-increasing
        return result;
    }
}
