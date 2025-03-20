package LeetCodeJava.Array;

// https://leetcode.com/problems/first-missing-positive/
// https://leetcode.ca/all/41.html

import java.util.HashMap;
import java.util.Map;

/**
 * 41. First Missing Positive
 * Solved
 * Hard
 * Topics
 * Companies
 * Hint
 * Given an unsorted integer array nums. Return the smallest positive integer that is not present in nums.
 *
 * You must implement an algorithm that runs in O(n) time and uses O(1) auxiliary space.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,0]
 * Output: 3
 * Explanation: The numbers in the range [1,2] are all in the array.
 * Example 2:
 *
 * Input: nums = [3,4,-1,1]
 * Output: 2
 * Explanation: 1 is in the array but 2 is missing.
 * Example 3:
 *
 * Input: nums = [7,8,9,11,12]
 * Output: 1
 * Explanation: The smallest positive integer 1 is missing.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * -231 <= nums[i] <= 231 - 1
 *
 */
public class FirstMissingPositive {

    // V0
    // IDEA: HASHMAP + MATH
    public int firstMissingPositive(int[] nums) {

        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // {key : cnt}
        int maxVal = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            maxVal = Math.max(maxVal, n);
            map.put(n, map.getOrDefault(n, 0) + 1);
        }

        int start = 1;
        while (start <= maxVal) {
            if (!map.containsKey(start)) {
                return start;
            }
            start += 1;
        }

        return start;
    }

    // V1

    // V2
}
