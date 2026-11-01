package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-if-an-array-is-consecutive/description/
// https://leetcode.ca/2022-01-06-2229-Check-if-an-Array-Is-Consecutive/

import java.util.HashSet;
import java.util.Set;

/**
 * 2229. Check if an Array Is Consecutive
 * Easy
 *
 * Given an integer array nums, return true if nums is consecutive, otherwise return false.
 *
 * An array is consecutive if it contains every number in the range [x, x + n - 1] (inclusive), where x is the minimum number in the array and n is the length of the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,4,2]
 * Output: true
 * Explanation:
 * The minimum value is 1 and the length of nums is 4.
 * All of the values in the range [x, x + n - 1] = [1, 1 + 4 - 1] = [1, 4] = (1, 2, 3, 4) occur in nums.
 * Therefore, nums is consecutive.
 *
 * Example 2:
 *
 * Input: nums = [1,3]
 * Output: false
 * Explanation:
 * The minimum value is 1 and the length of nums is 2.
 * The value 2 in the range [x, x + n - 1] = [1, 1 + 2 - 1], = [1, 2] = (1, 2) does not occur in nums.
 * Therefore, nums is not consecutive.
 *
 * Example 3:
 *
 * Input: nums = [3,5,4]
 * Output: true
 * Explanation:
 * The minimum value is 3 and the length of nums is 3.
 * All of the values in the range [x, x + n - 1] = [3, 3 + 3 - 1] = [3, 5] = (3, 4, 5) occur in nums.
 * Therefore, nums is consecutive.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^5
 *
 */
public class CheckIfAnArrayIsConsecutive {

    // V0
    // IDEA: HASH SET - n DISTINCT VALUES SPANNING EXACTLY [min, min + n - 1]
    //   max - min + 1 == n alone is not enough: [1,1,3] passes it but has a duplicate,
    //   so the distinct count is checked as well.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isConsecutive(int[] nums) {
        int n = nums.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        Set<Integer> distinct = new HashSet<>();
        for (int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
            distinct.add(num);
        }
        return distinct.size() == n && max - min + 1 == n;
    }
}
