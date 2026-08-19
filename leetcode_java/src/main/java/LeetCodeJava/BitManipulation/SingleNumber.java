package LeetCodeJava.BitManipulation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// https://leetcode.com/problems/single-number/

/**
 *  136. Single Number
 *  Easy
 *
 *  Given a non-empty array of integers nums, every element appears twice except
 *  for one. Find that single one.
 *
 *  You must implement a solution with a linear runtime complexity and use only
 *  constant extra space.
 *
 *  Example 1:
 *   Input: nums = [2,2,1]
 *   Output: 1
 *
 *  Example 2:
 *   Input: nums = [4,1,2,1,2]
 *   Output: 4
 *
 *  Example 3:
 *   Input: nums = [1]
 *   Output: 1
 *
 *  Constraints:
 *   1 <= nums.length <= 3 * 10^4
 *   -3 * 10^4 <= nums[i] <= 3 * 10^4
 *   Each element appears twice except for one element which appears only once.
 */
public class SingleNumber {

    // V0
    // IDEA: XOR. x ^ x == 0 and x ^ 0 == x, so XOR-ing everything cancels the
    //       pairs and leaves the unique value.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int num : nums) {
            res ^= num;
        }
        return res;
    }

    // V1
    // IDEA: hash set toggle - insert on first sight, delete on the second; the only
    //       element left in the set is the unique one
    /**
     * time = O(n)
     * space = O(n)
     */
    public int singleNumber_1(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) {
                seen.remove(num);
            }
        }
        return seen.iterator().next();
    }

    // V2
    // IDEA: sort first - the duplicated values then sit side by side, so walking in
    //       steps of 2 finds the first index whose neighbour differs
    /**
     * time = O(n log n)
     * space = O(n) for the defensive copy
     */
    public int singleNumber_2(int[] nums) {
        int[] arr = nums.clone();
        Arrays.sort(arr);
        for (int i = 0; i + 1 < arr.length; i += 2) {
            if (arr[i] != arr[i + 1]) {
                return arr[i];
            }
        }
        return arr[arr.length - 1];
    }
}
