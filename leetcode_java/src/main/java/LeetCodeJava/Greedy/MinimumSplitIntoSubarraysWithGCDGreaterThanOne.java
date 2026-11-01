package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-split-into-subarrays-with-gcd-greater-than-one/description/

import java.util.*;

/**
 * 2436. Minimum Split Into Subarrays With GCD Greater Than One
 * Medium
 *
 * You are given an array nums consisting of positive integers.
 *
 * Split the array into one or more disjoint subarrays such that:
 *
 * Each element of the array belongs to exactly one subarray, and
 * The GCD of the elements of each subarray is strictly greater than 1.
 *
 * Return the minimum number of subarrays that can be obtained after the split.
 *
 * Note that:
 *
 * The GCD of a subarray is the largest positive integer that evenly divides all the elements of the subarray.
 * A subarray is a contiguous part of the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [12,6,3,14,8]
 * Output: 2
 * Explanation: We can split the array into the subarrays: [12,6,3] and [14,8].
 * - The GCD of 12, 6 and 3 is 3, which is strictly greater than 1.
 * - The GCD of 14 and 8 is 2, which is strictly greater than 1.
 * It can be shown that splitting the array into one subarray will make the GCD = 1.
 *
 * Example 2:
 *
 * Input: nums = [4,12,6,14]
 * Output: 1
 * Explanation: We can split the array into only one subarray, which is the whole array.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2000
 * 2 <= nums[i] <= 10^9
 *
 */
public class MinimumSplitIntoSubarraysWithGCDGreaterThanOne {

    // V0
    // IDEA: GREEDY + RUNNING GCD (EXTEND THE PIECE WHILE ITS GCD STAYS > 1)
    //       gcd only shrinks as a piece grows, so cutting as late as possible is optimal.
    //       every nums[i] >= 2, so a single element is always a valid piece.
    /**
     * time = O(N * log(max(nums)))
     * space = O(1)
     */
    public int minimumSplits(int[] nums) {
        int pieces = 1;
        // gcd of the open piece; gcd(0, x) = x starts it
        int currentGcd = 0;
        for (int num : nums) {
            currentGcd = gcd(currentGcd, num);
            if (currentGcd == 1) {
                // num cannot join this piece: it starts a new one
                pieces++;
                currentGcd = num;
            }
        }
        return pieces;
    }

    /** Euclid's algorithm. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
