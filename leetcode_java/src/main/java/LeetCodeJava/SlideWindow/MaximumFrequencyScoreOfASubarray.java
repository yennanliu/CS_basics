package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-frequency-score-of-a-subarray/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 2524. Maximum Frequency Score of a Subarray
 * Hard
 *
 * You are given an integer array nums and a positive integer k.
 *
 * The frequency score of an array is the sum of the distinct values in the array
 * raised to the power of their frequencies, taking the sum modulo 10^9 + 7.
 *
 * For example, the frequency score of the array [5,4,5,7,4,4] is
 * (4^3 + 5^2 + 7^1) modulo (10^9 + 7) = 96.
 *
 * Return the maximum frequency score of a subarray of size k in nums.
 * You should maximize the value under the modulo and not the actual value.
 *
 * A subarray is a contiguous part of an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1,2,1,2], k = 3
 * Output: 5
 * Explanation: The subarray [2,1,2] has a frequency score equal to 5.
 * It can be shown that it is the maximum frequency score we can have.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1,1,1], k = 4
 * Output: 1
 * Explanation: All the subarrays of length 4 have a frequency score equal to 1.
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^6
 *
 */
public class MaximumFrequencyScoreOfASubarray {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW + COUNT MAP + INCREMENTAL MODULAR UPDATE
    //  a window's score is the sum over distinct v of v^count[v] (mod 1e9+7). recomputing it
    //  per window is O(n * k); instead only patch the two values whose counts change:
    //  subtract the old term v^old, change the count, add the new term v^new.
    //  NOTE: a value absent from the window contributes 0, not v^0 == 1.
    //  NOTE: the maximum is taken over the REDUCED residues, so keep score in [0, MOD).
    /**
     * time = O(N * log K)   (log factor = modular power)
     * space = O(N)
     */
    public int maxFrequencyScore(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < k; i++) {
            count.put(nums[i], count.getOrDefault(nums[i], 0) + 1);
        }
        long score = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            score = (score + term(entry.getKey(), entry.getValue())) % MOD;
        }

        long best = score;
        for (int i = k; i < nums.length; i++) {
            int out = nums[i - k];
            int in = nums[i];
            // same value leaves and enters -> the window's multiset is unchanged
            if (out != in) {
                score = changeCount(count, out, -1, score);
                score = changeCount(count, in, +1, score);
            }
            best = Math.max(best, score);
        }
        return (int) best;
    }

    /** Moves value's count by delta and returns the score patched for that one value. */
    private long changeCount(Map<Integer, Integer> count, int value, int delta, long score) {
        int oldCount = count.getOrDefault(value, 0);
        int newCount = oldCount + delta;
        // + MOD keeps the subtraction non-negative
        score = (score - term(value, oldCount) + MOD) % MOD;
        score = (score + term(value, newCount)) % MOD;
        count.put(value, newCount);
        return score;
    }

    /** Contribution of one distinct value: value^times mod MOD, or 0 if it is absent. */
    private long term(int value, int times) {
        if (times == 0) {
            return 0;
        }
        return modPow(value, times);
    }

    /** base^exponent mod MOD by repeated squaring. */
    private long modPow(long base, long exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            // the lowest bit of the exponent says whether this power of base is included
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
