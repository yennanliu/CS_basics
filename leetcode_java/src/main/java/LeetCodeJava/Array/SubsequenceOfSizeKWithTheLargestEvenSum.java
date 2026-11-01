package LeetCodeJava.Array;

// https://leetcode.com/problems/subsequence-of-size-k-with-the-largest-even-sum/description/
// https://leetcode.ca/2021-08-28-2098-Subsequence-of-Size-K-With-the-Largest-Even-Sum/

import java.util.*;

/**
 * 2098. Subsequence of Size K With the Largest Even Sum
 * Medium
 *
 *
 * You are given an integer array nums and an integer k. Find the largest even sum of any subsequence of nums that has a length of k.
 *
 * Return this sum, or -1 if such a sum does not exist.
 *
 * A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,1,5,3,1], k = 3
 * Output: 12
 * Explanation:
 * The subsequence with the largest possible even sum is [4,5,3]. It has a sum of 4 + 5 + 3 = 12.
 *
 * Example 2:
 *
 * Input: nums = [4,6,2], k = 3
 * Output: 12
 * Explanation:
 * The subsequence with the largest possible even sum is [4,6,2]. It has a sum of 4 + 6 + 2 = 12.
 *
 * Example 3:
 *
 * Input: nums = [1,3,5], k = 1
 * Output: -1
 * Explanation:
 * No subsequence of nums with length 1 has an even sum.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^9
 * 1 <= k <= nums.length
 *
 */
public class SubsequenceOfSizeKWithTheLargestEvenSum {

    // V0
    // IDEA: SORT, TAKE THE k LARGEST, THEN FIX THE PARITY WITH ONE SWAP
    /**
     *  A subsequence's sum does not depend on order, so sort and take the top k.
     *  If that sum is odd, exactly one swap flips the parity: drop one chosen
     *  element and take one unchosen element of the OPPOSITE parity. To lose the
     *  least, only two swaps matter:
     *    - drop the smallest chosen odd,  add the largest unchosen even
     *    - drop the smallest chosen even, add the largest unchosen odd
     *
     * time = O(N log N)
     * space = O(N)
     */
    public long largestEvenSum(int[] nums, int k) {
        int n = nums.length;
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        // sorted ascending: the chosen k are sorted[n - k .. n - 1]

        long total = 0;
        for (int i = n - k; i < n; i++) {
            total += sorted[i];
        }
        if (total % 2 == 0) {
            return total;
        }

        // smallest chosen value of each parity (-1 = none)
        long minChosenOdd = -1;
        long minChosenEven = -1;
        for (int i = n - k; i < n; i++) {
            if (sorted[i] % 2 == 1 && minChosenOdd == -1) {
                minChosenOdd = sorted[i];
            }
            if (sorted[i] % 2 == 0 && minChosenEven == -1) {
                minChosenEven = sorted[i];
            }
        }

        // largest unchosen value of each parity (-1 = none)
        long maxRestOdd = -1;
        long maxRestEven = -1;
        for (int i = n - k - 1; i >= 0; i--) {
            if (sorted[i] % 2 == 1 && maxRestOdd == -1) {
                maxRestOdd = sorted[i];
            }
            if (sorted[i] % 2 == 0 && maxRestEven == -1) {
                maxRestEven = sorted[i];
            }
        }

        long best = -1;
        if (minChosenOdd != -1 && maxRestEven != -1) {
            best = Math.max(best, total - minChosenOdd + maxRestEven);
        }
        if (minChosenEven != -1 && maxRestOdd != -1) {
            best = Math.max(best, total - minChosenEven + maxRestOdd);
        }
        return best;
    }
}
