package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-impossible-or/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 2568. Minimum Impossible OR
 * Medium
 *
 * You are given a 0-indexed integer array nums.
 *
 * We say that an integer x is expressible from nums if there exist some integers 0 <= index1 < index2 < ... < indexk < nums.length for which nums[index1] | nums[index2] | ... | nums[indexk] = x. In other words, an integer is expressible if it can be written as the bitwise OR of some subsequence of nums.
 *
 * Return the minimum positive non-zero integer that is not expressible from nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1]
 * Output: 4
 *
 * Example 2:
 *
 * Input: nums = [5,3,2]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class MinimumImpossibleOr {

    // V0
    // IDEA: BRAINTEASER -- the answer is always the smallest power of 2 NOT literally present in nums
    //       (OR never removes bits, so a single-bit value p is reachable only if p itself is in nums;
    //        any other x is reachable once all its single bits are present)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minImpossibleOR(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            seen.add(num);
        }
        // nums[i] <= 10^9 < 2^30, so 2^30 is always absent and the loop ends
        int power = 1;
        while (seen.contains(power)) {
            power *= 2;
        }
        return power;
    }
}
