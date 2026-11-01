package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-maximum-number-of-marked-indices/description/

import java.util.*;

/**
 * 2576. Find the Maximum Number of Marked Indices
 * Medium
 *
 * You are given a 0-indexed integer array nums.
 *
 * Initially, all of the indices are unmarked. You are allowed to make this operation any number of times:
 *
 * Pick two different unmarked indices i and j such that 2 * nums[i] <= nums[j], then mark i and j.
 *
 * Return the maximum possible number of marked indices in nums using the above operation any number of times.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,5,2,4]
 * Output: 2
 * Explanation: In the first operation: pick i = 2 and j = 1, the operation is allowed because 2 * nums[2] <= nums[1]. Then mark index 2 and 1.
 * It can be shown that there's no other valid operation so the answer is 2.
 *
 * Example 2:
 *
 * Input: nums = [9,2,5,4]
 * Output: 4
 * Explanation: In the first operation: pick i = 3 and j = 0, the operation is allowed because 2 * nums[3] <= nums[0]. Then mark index 3 and 0.
 * In the second operation: pick i = 1 and j = 2, the operation is allowed because 2 * nums[1] <= nums[2]. Then mark index 1 and 2.
 * Since there is no other operation, the answer is 4.
 *
 * Example 3:
 *
 * Input: nums = [7,6,8]
 * Output: 0
 * Explanation: There is no valid operation to do, so the answer is 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class FindTheMaximumNumberOfMarkedIndices {

    // V0
    // IDEA: SORT + GREEDY TWO POINTERS
    //       at most n / 2 pairs; the best pairs match the smallest half with the largest half.
    //       walk `left` over the small half, scan the large half (starting at (n + 1) / 2 so the
    //       middle element of an odd n stays out), and lock in a pair whenever 2 * small <= large.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxNumOfMarkedIndices(int[] nums) {
        int n = nums.length;
        int[] sorted = Arrays.copyOf(nums, n);
        Arrays.sort(sorted);

        int left = 0; // next small value waiting for a partner
        for (int right = (n + 1) / 2; right < n; right++) {
            // long: 2 * 1e9 overflows int
            if (2L * sorted[left] <= sorted[right]) {
                left++;
            }
        }
        // each matched small value marks two indices
        return left * 2;
    }
}
