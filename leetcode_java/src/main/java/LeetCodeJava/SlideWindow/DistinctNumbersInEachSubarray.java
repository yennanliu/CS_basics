package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/distinct-numbers-in-each-subarray/description/
// https://leetcode.ca/2020-12-25-1852-Distinct-Numbers-in-Each-Subarray/

import java.util.HashMap;
import java.util.Map;

/**
 * 1852. Distinct Numbers in Each Subarray
 * Medium
 *
 * You are given an integer array nums of length n and an integer k. Your task is to find the number of distinct elements in every subarray of size k within nums.
 *
 * Return an array ans such that ans[i] is the count of distinct elements in nums[i..(i + k - 1)] for each index 0 <= i < n - k.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,2,2,1,3], k = 3
 * Output: [3,2,2,2,3]
 * Explanation: The number of distinct elements in each subarray goes as follows:
 * - nums[0..2] = [1,2,3] so ans[0] = 3
 * - nums[1..3] = [2,3,2] so ans[1] = 2
 * - nums[2..4] = [3,2,2] so ans[2] = 2
 * - nums[3..5] = [2,2,1] so ans[3] = 2
 * - nums[4..6] = [2,1,3] so ans[4] = 3
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1,2,3,4], k = 4
 * Output: [1,2,3,4]
 * Explanation: The number of distinct elements in each subarray goes as follows:
 * - nums[0..3] = [1,1,1,1] so ans[0] = 1
 * - nums[1..4] = [1,1,1,2] so ans[1] = 2
 * - nums[2..5] = [1,1,2,3] so ans[2] = 3
 * - nums[3..6] = [1,2,3,4] so ans[3] = 4
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class DistinctNumbersInEachSubarray {

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW + COUNT MAP (distinct == number of live keys)
    //       build the counts for the first window, then slide one step at a
    //       time: add nums[i], drop nums[i - k]. the distinct count is the map's
    //       size, which stays right only if a key is REMOVED the moment its
    //       count reaches zero.
    /**
     * time = O(N)
     * space = O(K)
     */
    public int[] distinctNumbers(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Map<Integer, Integer> count = new HashMap<>();

        // first window : nums[0 .. k-1]
        for (int i = 0; i < k; i++) {
            count.put(nums[i], count.getOrDefault(nums[i], 0) + 1);
        }
        result[0] = count.size();

        for (int i = k; i < n; i++) {
            // nums[i] enters
            count.put(nums[i], count.getOrDefault(nums[i], 0) + 1);

            // nums[i - k] leaves; drop the key at zero so size() stays exact
            int leaving = nums[i - k];
            int remaining = count.get(leaving) - 1;
            if (remaining == 0) {
                count.remove(leaving);
            } else {
                count.put(leaving, remaining);
            }

            result[i - k + 1] = count.size();
        }
        return result;
    }
}
