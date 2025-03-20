package LeetCodeJava.Array;

// https://leetcode.com/problems/majority-element-ii/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 229. Majority Element II
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an integer array of size n, find all elements that appear more than ⌊ n/3 ⌋ times.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,3]
 * Output: [3]
 * Example 2:
 *
 * Input: nums = [1]
 * Output: [1]
 * Example 3:
 *
 * Input: nums = [1,2]
 * Output: [1,2]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 104
 * -109 <= nums[i] <= 109
 *
 *
 * Follow up: Could you solve the problem in linear time and in O(1) space?
 *
 *
 *
 */
public class MajorityElement2 {

    // V0
    // IDEA: HASHMAP
    public List<Integer> majorityElement(int[] nums) {
        List<Integer> res = new ArrayList<>();
        // edge
        if (nums == null || nums.length == 0) {
            return null;
        }
        if (nums.length == 1) {
            res.add(nums[0]);
            return res;
        }
        // hashmap
        Map<Integer, Integer> map = new HashMap<>();
        int cnt = 0;
        for (int n : nums) {
            cnt += 1;
            map.put(n, map.getOrDefault(n, 0) + 1);
        }

        // System.out.println(">>> map = " + map + ", cnt = " + cnt + " cnt / 3 = " +
        // cnt / 3);

        for (int k : map.keySet()) {
            if (map.get(k) > cnt / 3) {
                res.add(k);
            }
        }

        return res;
    }

    // V1

    // V2
}
