package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-operations-to-make-the-array-k-increasing/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 2111. Minimum Operations to Make the Array K-Increasing
 * Hard
 *
 * You are given a 0-indexed array arr consisting of n positive integers, and a positive integer k.
 *
 * The array arr is called K-increasing if arr[i-k] <= arr[i] holds for every index i, where
 * k <= i <= n-1.
 *
 * For example, arr = [4, 1, 5, 2, 6, 2] is K-increasing for k = 2 because:
 * arr[0] <= arr[2] (4 <= 5)
 * arr[1] <= arr[3] (1 <= 2)
 * arr[2] <= arr[4] (5 <= 6)
 * arr[3] <= arr[5] (2 <= 2)
 * However, the same arr is not K-increasing for k = 1 (because arr[0] > arr[1]) or k = 3 (because
 * arr[0] > arr[3]).
 *
 * In one operation, you can choose an index i and change arr[i] into any positive integer.
 *
 * Return the minimum number of operations required to make the array K-increasing for the given k.
 *
 *
 * Example 1:
 *
 * Input: arr = [5,4,3,2,1], k = 1
 * Output: 4
 * Explanation:
 * For k = 1, the resultant array has to be non-decreasing.
 * Some of the K-increasing arrays that can be formed are [5,6,7,8,9], [1,1,1,1,1], [2,2,3,4,4]. All of
 * them require 4 operations.
 * It is suboptimal to change the array to, for example, [6,7,8,9,10] because it would take 5 operations.
 * It can be shown that we cannot make the array K-increasing in less than 4 operations.
 *
 * Example 2:
 *
 * Input: arr = [4,1,5,2,6,2], k = 2
 * Output: 0
 * Explanation:
 * This is the same example as the one in the problem description.
 * Here, for every index i where 2 <= i <= 5, arr[i-2] <= arr[i].
 * Since the given array is already K-increasing, we do not need to perform any operations.
 *
 * Example 3:
 *
 * Input: arr = [4,1,5,2,6,2], k = 3
 * Output: 2
 * Explanation:
 * Indices 3 and 5 are the only ones not satisfying arr[i-3] <= arr[i] for 3 <= i <= 5.
 * One of the ways we could make the array K-increasing is by changing arr[3] to 4 and arr[5] to 5.
 * The array will now be [4,1,5,4,6,5].
 * Note that there could be other ways to make the array K-increasing, but none of them require less
 * than 2 operations.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * 1 <= arr[i], k <= arr.length
 *
 */
public class MinimumOperationsToMakeTheArrayKIncreasing {

    // V0
    // IDEA: SPLIT INTO k INDEPENDENT CHAINS, KEEP EACH CHAIN'S LONGEST NON-DECREASING SUBSEQUENCE
    /**
     *  The constraint only relates indices k apart, so the array splits into k chains:
     *  arr[r], arr[r + k], arr[r + 2k], ...  Each chain must end up non-decreasing, and the
     *  untouched elements must already form a non-decreasing subsequence of it, so a chain
     *  costs  chainLength - LNDS(chain).
     *
     *  LNDS uses the patience tails list with an upper bound (first tail > value), which
     *  allows equal values; a lower bound would give the strictly increasing variant.
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int kIncreasing(int[] arr, int k) {
        int operations = 0;
        for (int start = 0; start < k; start++) {
            List<Integer> tails = new ArrayList<>();
            int chainLength = 0;

            for (int i = start; i < arr.length; i += k) {
                chainLength++;
                int value = arr[i];

                // upper bound: first tail strictly greater than value
                int left = 0;
                int right = tails.size();
                while (left < right) {
                    int mid = left + (right - left) / 2;
                    if (tails.get(mid) > value) {
                        right = mid;
                    } else {
                        left = mid + 1;
                    }
                }
                if (left == tails.size()) {
                    tails.add(value);
                } else {
                    tails.set(left, value);
                }
            }
            // every element outside the kept subsequence has to be changed
            operations += chainLength - tails.size();
        }
        return operations;
    }
}
