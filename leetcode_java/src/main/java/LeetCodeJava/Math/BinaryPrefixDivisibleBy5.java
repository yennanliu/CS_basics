package LeetCodeJava.Math;

// https://leetcode.com/problems/binary-prefix-divisible-by-5/description/

import java.util.*;

/**
 * 1018. Binary Prefix Divisible By 5
 * Easy
 *
 * You are given a binary array nums (0-indexed).
 *
 * We define xi as the number whose binary representation is the subarray nums[0..i] (from most-significant-bit to least-significant-bit).
 *
 * For example, if nums = [1,0,1], then x0 = 1, x1 = 2, and x2 = 5.
 *
 * Return an array of booleans answer where answer[i] is true if xi is divisible by 5.
 *
 * Example 1:
 *
 * Input: nums = [0,1,1]
 * Output: [true,false,false]
 * Explanation: The input numbers in binary are 0, 01, 011; which are 0, 1, and 3 in base-10.
 * Only the first number is divisible by 5, so answer[0] is true.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1]
 * Output: [false,false,false]
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * nums[i] is either 0 or 1.
 *
 */
public class BinaryPrefixDivisibleBy5 {

    // V0
    // IDEA: RUNNING REMAINDER MOD 5 (x_i = 2 * x_(i-1) + nums[i], keep only x_i % 5)
    /**
     * time = O(N)
     * space = O(1)  (excluding the output)
     */
    public List<Boolean> prefixesDivBy5(int[] nums) {
        List<Boolean> result = new ArrayList<>();
        if (nums == null) {
            return result;
        }

        // the prefix can be 10^5 bits long, so never build the number itself:
        // (a * 2 + b) % 5 only depends on a % 5
        int remainder = 0;
        for (int bit : nums) {
            remainder = (remainder * 2 + bit) % 5;
            result.add(remainder == 0);
        }
        return result;
    }
}
