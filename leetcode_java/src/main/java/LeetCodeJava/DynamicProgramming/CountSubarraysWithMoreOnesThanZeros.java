package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-subarrays-with-more-ones-than-zeros/description/

import java.util.*;

/**
 * 2031. Count Subarrays With More Ones Than Zeros
 * Medium
 *
 * You are given a binary array nums containing only the integers 0 and 1. Return the
 * number of subarrays in nums that have more 1's than 0's. Since the answer may be very
 * large, return it modulo 10^9 + 7.
 *
 * A subarray is a contiguous sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,1,0,1]
 * Output: 9
 * Explanation:
 * The subarrays of size 1 that have more ones than zeros are: [1], [1], [1]
 * The subarrays of size 2 that have more ones than zeros are: [1,1]
 * The subarrays of size 3 that have more ones than zeros are: [0,1,1], [1,1,0], [1,0,1]
 * The subarrays of size 4 that have more ones than zeros are: [1,1,0,1]
 * The subarrays of size 5 that have more ones than zeros are: [0,1,1,0,1]
 *
 * Example 2:
 *
 * Input: nums = [0]
 * Output: 0
 * Explanation:
 * No subarrays have more ones than zeros.
 *
 * Example 3:
 *
 * Input: nums = [1]
 * Output: 1
 * Explanation:
 * The subarrays of size 1 that have more ones than zeros are: [1]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 1
 *
 */
public class CountSubarraysWithMoreOnesThanZeros {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: MAP 0 -> -1, COUNT EARLIER PREFIXES STRICTLY BELOW THE CURRENT ONE (PREFIX MOVES BY +-1)
    /**
     *  With 0 rewritten as -1, nums[i..j-1] has more ones than zeros
     *  <=> prefix[j] - prefix[i] > 0.
     *
     *  State:
     *     - countOfPrefix[v] = how many prefixes seen so far equal v
     *     - below            = how many prefixes seen so far are STRICTLY below the
     *                          current prefix value
     *
     *  Transition (the prefix moves by exactly 1 each step, so no BIT is needed):
     *     prefix p -> p+1 : every prefix equal to p joins "below"   -> below += count[p]
     *     prefix p -> p-1 : every prefix equal to p-1 leaves it     -> below -= count[p-1]
     *  `below` is then the number of valid subarrays ending at this index.
     *
     *  time = O(N)
     *  space = O(N)
     */
    public int subarraysWithMoreZerosThanOnes(int[] nums) {
        int n = nums.length;
        // prefix ranges over [-n, n]; shift by n to index an array
        int offset = n;
        long[] countOfPrefix = new long[2 * n + 1];
        countOfPrefix[offset] = 1; // the empty prefix

        int prefix = 0;
        long below = 0;
        long result = 0;

        for (int num : nums) {
            if (num == 1) {
                // the prefixes equal to the old value are now strictly below
                below += countOfPrefix[prefix + offset];
                prefix++;
            } else {
                prefix--;
                // the prefixes equal to the new value are no longer strictly below
                below -= countOfPrefix[prefix + offset];
            }
            result = (result + below) % MOD;
            countOfPrefix[prefix + offset]++;
        }
        return (int) result;
    }
}
