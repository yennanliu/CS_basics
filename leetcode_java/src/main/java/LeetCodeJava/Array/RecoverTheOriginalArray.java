package LeetCodeJava.Array;

// https://leetcode.com/problems/recover-the-original-array/description/

import java.util.*;

/**
 * 2122. Recover the Original Array
 * Hard
 *
 * Alice had a 0-indexed array arr consisting of n positive integers. She chose an arbitrary positive integer k and created two new 0-indexed integer arrays lower and higher in the following manner:
 *
 * lower[i] = arr[i] - k, for every index i where 0 <= i < n
 * higher[i] = arr[i] + k, for every index i where 0 <= i < n
 *
 * Unfortunately, Alice lost all three arrays. However, she remembers the integers that were present in the arrays lower and higher, but not the array each integer belonged to. Help Alice and recover the original array.
 *
 * Given an array nums consisting of 2n integers, where exactly n of the integers were present in lower and the remaining in higher, return the original array arr. In case the answer is not unique, return any valid array.
 *
 * Note: The test cases are generated such that there exists at least one valid array arr.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,10,6,4,8,12]
 * Output: [3,7,11]
 * Explanation:
 * If arr = [3,7,11] and k = 1, we get lower = [2,6,10] and higher = [4,8,12].
 * Combining lower and higher gives us [2,6,10,4,8,12], which is a permutation of nums.
 * Another valid possibility is that arr = [5,7,9] and k = 3. In that case, lower = [2,4,6] and higher = [8,10,12].
 *
 * Example 2:
 *
 * Input: nums = [1,1,3,3]
 * Output: [2,2]
 * Explanation:
 * If arr = [2,2] and k = 1, we get lower = [1,1] and higher = [3,3].
 * Combining lower and higher gives us [1,1,3,3], which is equal to nums.
 * Note that arr cannot be [1,3] because in that case, the only possible way to obtain [1,1,3,3] is with k = 0.
 * This is invalid since k must be positive.
 *
 * Example 3:
 *
 * Input: nums = [5,435]
 * Output: [220]
 * Explanation:
 * The only possible combination is arr = [220] and k = 215. Using them, we get lower = [5] and higher = [435].
 *
 *
 * Constraints:
 *
 * 2 * n == nums.length
 * 1 <= n <= 1000
 * 1 <= nums[i] <= 10^9
 * nums[i] is even.
 *
 */
public class RecoverTheOriginalArray {

    // V0
    // IDEA: SORT, TRY EVERY PARTNER FOR THE SMALLEST ELEMENT, VERIFY GREEDILY
    /**
     *  After sorting, nums[0] must be a `lower` value, and its partner is some
     *  nums[j]. That fixes 2k = nums[j] - nums[0], which must be positive and even,
     *  so there are at most 2n candidates.
     *
     *  For one candidate 2k, walk the sorted array: the smallest unused element
     *  must be a `lower`, so pair it with the nearest unused element equal to
     *  value + 2k. A moving pointer keeps each check O(n).
     *
     * time = O(N^2)
     * space = O(N)
     */
    public int[] recoverArray(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int n = sorted.length;

        for (int j = 1; j < n; j++) {
            int doubleK = sorted[j] - sorted[0];
            // k must be a positive integer
            if (doubleK <= 0 || doubleK % 2 != 0) {
                continue;
            }
            int[] original = tryPairing(sorted, doubleK);
            if (original != null) {
                return original;
            }
        }
        // the input guarantees an answer exists
        return new int[0];
    }

    /** Pairs every lower with lower + doubleK; returns the original array, or null if impossible. */
    private int[] tryPairing(int[] sorted, int doubleK) {
        int n = sorted.length;
        boolean[] used = new boolean[n];
        int[] original = new int[n / 2];
        int filled = 0;
        int partner = 0;

        for (int i = 0; i < n; i++) {
            if (used[i]) {
                continue;
            }
            // sorted[i] is the smallest unused value -> it must be a lower
            int target = sorted[i] + doubleK;
            partner = Math.max(partner, i + 1);
            while (partner < n && (used[partner] || sorted[partner] < target)) {
                partner++;
            }
            if (partner == n || sorted[partner] != target) {
                return null;
            }
            used[partner] = true;
            original[filled] = sorted[i] + doubleK / 2;
            filled++;
        }
        return original;
    }
}
