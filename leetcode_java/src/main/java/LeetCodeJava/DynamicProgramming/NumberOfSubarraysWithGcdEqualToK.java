package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-subarrays-with-gcd-equal-to-k/description/

import java.util.*;

/**
 * 2447. Number of Subarrays With GCD Equal to K
 * Medium
 *
 * Given an integer array nums and an integer k, return the number of subarrays of nums where
 * the greatest common divisor of the subarray's elements is k.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 * The greatest common divisor of an array is the largest integer that evenly divides all the
 * array elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [9,3,1,2,6,3], k = 3
 * Output: 4
 * Explanation: The subarrays of nums where 3 is the greatest common divisor of all the subarray's elements are:
 * - [9,3,1,2,6,3]
 * - [9,3,1,2,6,3]
 * - [9,3,1,2,6,3]
 * - [9,3,1,2,6,3]
 *
 * Example 2:
 *
 * Input: nums = [4], k = 7
 * Output: 0
 * Explanation: There are no subarrays of nums where 7 is the greatest common divisor of all the subarray's elements.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i], k <= 10^9
 *
 */
public class NumberOfSubarraysWithGcdEqualToK {

    // V0
    // IDEA: DP OVER "DISTINCT GCDs OF SUBARRAYS ENDING AT i" - ONLY O(log max) OF THEM, SINCE EACH NEW GCD AT LEAST HALVES
    /**
     * time = O(N * log(max) * log(max))
     * space = O(log(max))
     */
    public int subarrayGCD(int[] nums, int k) {
        int result = 0;

        // prevCount.get(g) = how many subarrays ending at i - 1 have gcd g
        Map<Integer, Integer> prevCount = new HashMap<>();

        for (int num : nums) {
            // the subarray holding only nums[i]
            Map<Integer, Integer> curCount = new HashMap<>();
            curCount.put(num, 1);

            // extend every subarray ending at i - 1 by nums[i]
            for (Map.Entry<Integer, Integer> entry : prevCount.entrySet()) {
                int newGcd = gcd(entry.getKey(), num);
                curCount.put(newGcd, curCount.getOrDefault(newGcd, 0) + entry.getValue());
            }

            result += curCount.getOrDefault(k, 0);
            prevCount = curCount;
        }
        return result;
    }

    // V1
    // IDEA: BRUTE FORCE EVERY START, EXTEND WHILE THE RUNNING GCD IS STILL A MULTIPLE OF k (n <= 1000, SO O(N^2) PASSES)
    /**
     * time = O(N^2 * log(max))
     * space = O(1)
     */
    public int subarrayGCD_1(int[] nums, int k) {
        int result = 0;
        for (int start = 0; start < nums.length; start++) {
            int runningGcd = 0;
            for (int end = start; end < nums.length; end++) {
                runningGcd = gcd(runningGcd, nums[end]);
                // the gcd only shrinks; once k does not divide it, it never comes back to k
                if (runningGcd % k != 0) {
                    break;
                }
                if (runningGcd == k) {
                    result++;
                }
            }
        }
        return result;
    }

    /** Euclid's algorithm; gcd(0, x) = x. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
