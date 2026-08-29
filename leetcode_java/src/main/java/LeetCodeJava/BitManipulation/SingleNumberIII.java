package LeetCodeJava.BitManipulation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// https://leetcode.com/problems/single-number-iii/

/**
 *  260. Single Number III
 *  Medium
 *
 *  Given an integer array nums, in which exactly two elements appear only once
 *  and all the other elements appear exactly twice. Find the two elements that
 *  appear only once. You can return the answer in any order.
 *
 *  You must write an algorithm that runs in linear runtime complexity and uses
 *  only constant extra space.
 *
 *  Example 1:
 *   Input: nums = [1,2,1,3,2,5]
 *   Output: [3,5]   ([5,3] is also accepted)
 *
 *  Example 2:
 *   Input: nums = [-1,0]
 *   Output: [-1,0]
 *
 *  Example 3:
 *   Input: nums = [0,1]
 *   Output: [1,0]
 *
 *  Constraints:
 *   2 <= nums.length <= 3 * 10^4
 *   -2^31 <= nums[i] <= 2^31 - 1
 *   Each integer appears twice except two integers which appear once.
 */
public class SingleNumberIII {

    // V0
    // IDEA: XOR everything -> a ^ b. Any set bit of that XOR is a bit where a and
    //       b differ, so partition the array by the lowest such bit and XOR each
    //       group separately.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int[] singleNumber(int[] nums) {
        int xorAll = 0;
        for (int num : nums) {
            xorAll ^= num;
        }

        // lowest set bit (safe for Integer.MIN_VALUE too)
        int diffBit = xorAll & (-xorAll);

        int a = 0;
        int b = 0;
        for (int num : nums) {
            if ((num & diffBit) != 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        return new int[]{a, b};
    }

    // V1
    // IDEA: hash set toggle - insert on first sight, delete on the second; the two
    //       survivors in the set are the answer
    /**
     * time = O(n)
     * space = O(n)
     */
    public int[] singleNumber_1(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) {
                seen.remove(num);
            }
        }
        int[] res = new int[2];
        int i = 0;
        for (Integer v : seen) {
            res[i++] = v;
        }
        return res;
    }

    // V2
    // IDEA: sort first - equal values become adjacent, so scan and pick up every value
    //       that is NOT followed by a copy of itself
    /**
     * time = O(n log n)
     * space = O(n) for the defensive copy
     */
    public int[] singleNumber_2(int[] nums) {
        int[] arr = nums.clone();
        Arrays.sort(arr);
        int[] res = new int[2];
        int k = 0;
        int i = 0;
        while (i < arr.length) {
            if (i + 1 < arr.length && arr[i] == arr[i + 1]) {
                i += 2;
            } else {
                res[k++] = arr[i];
                i++;
            }
        }
        return res;
    }
}
