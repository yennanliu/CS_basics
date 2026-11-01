package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-special-subsequences/description/

import java.util.*;

/**
 * 3404. Count Special Subsequences
 * Medium
 *
 * You are given an array nums consisting of positive integers.
 *
 * A special subsequence is defined as a subsequence of length 4, represented by
 * indices (p, q, r, s), where p < q < r < s. This subsequence must satisfy the
 * following conditions:
 *
 * nums[p] * nums[r] == nums[q] * nums[s]
 * There must be at least one element between each pair of indices. In other words,
 * q - p > 1, r - q > 1 and s - r > 1.
 *
 * Return the number of different special subsequences in nums.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,3,6,1]
 *
 * Output: 1
 *
 * Explanation:
 *
 * There is one special subsequence in nums.
 *
 * (p, q, r, s) = (0, 2, 4, 6):
 *
 * This corresponds to elements (1, 3, 3, 1).
 * nums[p] * nums[r] = nums[0] * nums[4] = 1 * 3 = 3
 * nums[q] * nums[s] = nums[2] * nums[6] = 3 * 1 = 3
 *
 * Example 2:
 *
 * Input: nums = [3,4,3,4,3,4,3,4]
 *
 * Output: 3
 *
 * Explanation:
 *
 * There are three special subsequences in nums.
 *
 * (p, q, r, s) = (0, 2, 4, 6):
 *
 * This corresponds to elements (3, 3, 3, 3).
 * nums[p] * nums[r] = nums[0] * nums[4] = 3 * 3 = 9
 * nums[q] * nums[s] = nums[2] * nums[6] = 3 * 3 = 9
 *
 * (p, q, r, s) = (1, 3, 5, 7):
 *
 * This corresponds to elements (4, 4, 4, 4).
 * nums[p] * nums[r] = nums[1] * nums[5] = 4 * 4 = 16
 * nums[q] * nums[s] = nums[3] * nums[7] = 4 * 4 = 16
 *
 * (p, q, r, s) = (0, 2, 5, 7):
 *
 * This corresponds to elements (3, 3, 4, 4).
 * nums[p] * nums[r] = nums[0] * nums[5] = 3 * 4 = 12
 * nums[q] * nums[s] = nums[2] * nums[7] = 3 * 4 = 12
 *
 * Constraints:
 *
 * 7 <= nums.length <= 1000
 * 1 <= nums[i] <= 1000
 *
 */
public class CountSpecialSubsequences {

    // V0
    // IDEA: REWRITE THE PRODUCT AS A RATIO, THEN COUNT (p, q) PAIRS BY REDUCED FRACTION
    //   nums[p] * nums[r] == nums[q] * nums[s]  <=>  nums[p] / nums[q] == nums[s] / nums[r].
    //   the left ratio uses only the early indices, the right one only the late
    //   ones. sweep r: just before handling r, every pair (p, q = r - 2) becomes
    //   legal (q needs r - q > 1, p needs q - p > 1) and is added to a counter keyed
    //   by the reduced fraction. then each s >= r + 2 adds the count of its own
    //   reduced fraction nums[s] / nums[r]. reducing by gcd makes 2/4 and 3/6 equal.
    /**
     * time = O(N^2 * log(MAX))
     * space = O(N^2)
     */
    public long numberOfSubsequences(int[] nums) {
        int n = nums.length;
        // reduced fraction (numerator / denominator) -> number of legal (p, q) pairs
        Map<Long, Integer> ratioCount = new HashMap<>();
        long count = 0;

        for (int r = 4; r < n - 2; r++) {
            // pairs with q = r - 2 just became legal for this r and all later ones
            int q = r - 2;
            for (int p = 0; p < q - 1; p++) {
                long key = fractionKey(nums[p], nums[q]);
                ratioCount.put(key, ratioCount.getOrDefault(key, 0) + 1);
            }

            // every s at least two past r closes quadruples with matching ratio
            for (int s = r + 2; s < n; s++) {
                long key = fractionKey(nums[s], nums[r]);
                count += ratioCount.getOrDefault(key, 0);
            }
        }
        return count;
    }

    /** Encodes the reduced fraction a / b as one long (both are positive ints). */
    private long fractionKey(int a, int b) {
        int g = gcd(a, b);
        long numerator = a / g;
        long denominator = b / g;
        return numerator * 1_000_000_007L + denominator;
    }

    /** Euclid's greatest common divisor. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
