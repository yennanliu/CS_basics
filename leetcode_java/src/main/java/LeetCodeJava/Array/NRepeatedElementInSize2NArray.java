package LeetCodeJava.Array;

// https://leetcode.com/problems/n-repeated-element-in-size-2n-array/

import java.util.HashSet;
import java.util.Set;

/**
 *  961. N-Repeated Element in Size 2N Array
 *  Easy
 *
 *  You are given an integer array nums with the following properties:
 *
 *  nums.length == 2 * n.
 *  nums contains n + 1 unique elements.
 *  Exactly one element of nums is repeated n times.
 *
 *  Return the element that is repeated n times.
 *
 *
 *  Example 1:
 *
 *  Input: nums = [1,2,3,3]
 *  Output: 3
 *
 *  Example 2:
 *
 *  Input: nums = [2,1,2,5,3,2]
 *  Output: 2
 *
 *  Example 3:
 *
 *  Input: nums = [5,1,5,2,5,3,5,4]
 *  Output: 5
 *
 *
 *  Constraints:
 *
 *  2 <= n <= 5000
 *  nums.length == 2 * n
 *  0 <= nums[i] <= 10^4
 *  nums contains n + 1 unique elements and one of them is repeated exactly n times.
 */
public class NRepeatedElementInSize2NArray {

    // V0
    // IDEA: HASH SET — the first value seen twice is the answer
    /**
     * time = O(n)
     * space = O(n)
     */
    public int repeatedNTimes(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int x : nums) {
            if (!seen.add(x)) {
                return x;
            }
        }
        return -1;
    }

    // V1
    // IDEA: PIGEONHOLE — with n+1 distinct values in 2n slots, two copies of the
    //       repeated value must land within distance 3 of each other
    /**
     * time = O(n)
     * space = O(1)
     */
    public int repeatedNTimes_1(int[] nums) {
        for (int gap = 1; gap <= 3; gap++) {
            for (int i = 0; i + gap < nums.length; i++) {
                if (nums[i] == nums[i + gap]) {
                    return nums[i];
                }
            }
        }
        return -1;
    }
}
