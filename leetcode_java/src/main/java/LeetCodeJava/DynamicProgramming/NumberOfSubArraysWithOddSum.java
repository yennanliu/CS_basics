package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-sub-arrays-with-odd-sum/description/

/**
 * 1524. Number of Sub-arrays With Odd Sum
 * Medium
 *
 * Given an array of integers arr, return the number of subarrays with an odd sum.
 *
 * Since the answer can be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,3,5]
 * Output: 4
 * Explanation: All subarrays are [[1],[1,3],[1,3,5],[3],[3,5],[5]]
 * All sub-arrays sum are [1,4,9,3,8,5].
 * Odd sums are [1,9,3,5] so the answer is 4.
 *
 * Example 2:
 *
 * Input: arr = [2,4,6]
 * Output: 0
 * Explanation: All subarrays are [[2],[2,4],[2,4,6],[4],[4,6],[6]]
 * All sub-arrays sum are [2,6,12,4,10,6].
 * All sub-arrays have even sum and the answer is 0.
 *
 * Example 3:
 *
 * Input: arr = [1,2,3,4,5,6,7]
 * Output: 16
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * 1 <= arr[i] <= 100
 *
 */
public class NumberOfSubArraysWithOddSum {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: PREFIX SUM PARITY COUNTING
    //       sum(arr[j..i]) = prefix[i+1] - prefix[j], which is odd exactly when the two
    //       prefixes have DIFFERENT parity. So for each new prefix, add how many earlier
    //       prefixes had the opposite parity. The empty prefix (sum 0) is even.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numOfSubarrays(int[] arr) {
        // the empty prefix has sum 0, which is even
        long evenPrefixes = 1;
        long oddPrefixes = 0;
        long count = 0;
        int prefixParity = 0;

        for (int value : arr) {
            // only the parity of the running sum matters, so the sum itself never overflows
            prefixParity = (prefixParity + value) % 2;
            if (prefixParity == 1) {
                // odd prefix: pairs with every earlier even prefix
                count += evenPrefixes;
                oddPrefixes++;
            } else {
                // even prefix: pairs with every earlier odd prefix
                count += oddPrefixes;
                evenPrefixes++;
            }
        }
        return (int) (count % MOD);
    }
}
