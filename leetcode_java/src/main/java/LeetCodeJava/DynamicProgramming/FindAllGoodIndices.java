package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-all-good-indices/description/

import java.util.*;

/**
 * 2420. Find All Good Indices
 * Medium
 *
 * You are given a 0-indexed integer array nums of size n and a positive integer k.
 *
 * We call an index i in the range k <= i < n - k good if the following conditions are satisfied:
 *
 * The k elements that are just before the index i are in non-increasing order.
 * The k elements that are just after the index i are in non-decreasing order.
 *
 * Return an array of all good indices sorted in increasing order.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,1,1,3,4,1], k = 2
 * Output: [2,3]
 * Explanation: There are two good indices in the array:
 * - Index 2. The subarray [2,1] is in non-increasing order, and the subarray [1,3] is in non-decreasing order.
 * - Index 3. The subarray [1,1] is in non-increasing order, and the subarray [3,4] is in non-decreasing order.
 * Note that the index 4 is not good because [4,1] is not non-decreasing.
 *
 * Example 2:
 *
 * Input: nums = [2,1,1,2], k = 2
 * Output: []
 * Explanation: There are no good indices in this array.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 3 <= n <= 10^5
 * 1 <= nums[i] <= 10^6
 * 1 <= k <= n / 2
 *
 */
public class FindAllGoodIndices {

    // V0
    // IDEA: PREFIX "NON-INCREASING RUN" + SUFFIX "NON-DECREASING RUN" LENGTHS
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<Integer> goodIndices(int[] nums, int k) {
        int n = nums.length;

        // nonIncreasing[i] = length of the non-increasing run ending at i
        int[] nonIncreasing = new int[n];
        Arrays.fill(nonIncreasing, 1);
        for (int i = 1; i < n; i++) {
            if (nums[i] <= nums[i - 1]) {
                nonIncreasing[i] = nonIncreasing[i - 1] + 1;
            }
        }

        // nonDecreasing[i] = length of the non-decreasing run starting at i
        int[] nonDecreasing = new int[n];
        Arrays.fill(nonDecreasing, 1);
        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] <= nums[i + 1]) {
                nonDecreasing[i] = nonDecreasing[i + 1] + 1;
            }
        }

        // i is good when the k elements before it and the k after it both have runs of length >= k
        List<Integer> good = new ArrayList<>();
        for (int i = k; i < n - k; i++) {
            if (nonIncreasing[i - 1] >= k && nonDecreasing[i + 1] >= k) {
                good.add(i);
            }
        }
        return good;
    }
}
