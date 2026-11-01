package LeetCodeJava.Greedy;

// https://leetcode.com/problems/make-k-subarray-sums-equal/description/

import java.util.*;

/**
 * 2607. Make K-Subarray Sums Equal
 * Medium
 *
 * You are given a 0-indexed integer array arr and an integer k. The array arr is circular. In other words, the first element of the array is the next element of the last element, and the last element of the array is the previous element of the first element.
 *
 * You can do the following operation any number of times:
 *
 * - Pick any element from arr and increase or decrease it by 1.
 *
 * Return the minimum number of operations such that the sum of each subarray of length k is equal.
 *
 * A subarray is a contiguous part of the array.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,4,1,3], k = 2
 * Output: 1
 * Explanation: we can do one operation on index 1 to make its value equal to 3.
 * The array after the operation is [1,3,1,3]
 * - Subarray starts at index 0 is [1, 3], and its sum is 4
 * - Subarray starts at index 1 is [3, 1], and its sum is 4
 * - Subarray starts at index 2 is [1, 3], and its sum is 4
 * - Subarray starts at index 3 is [3, 1], and its sum is 4
 *
 * Example 2:
 *
 * Input: arr = [2,5,5,7], k = 3
 * Output: 5
 * Explanation: we can do three operations on index 0 to make its value equal to 5 and two operations on index 3 to make its value equal to 5.
 * The array after the operations is [5,5,5,5]
 * - Subarray starts at index 0 is [5,5,5], and its sum is 15
 * - Subarray starts at index 1 is [5,5,5], and its sum is 15
 * - Subarray starts at index 2 is [5,5,5], and its sum is 15
 * - Subarray starts at index 3 is [5,5,5], and its sum is 15
 *
 *
 * Constraints:
 *
 * 1 <= k <= arr.length <= 10^5
 * 1 <= arr[i] <= 10^9
 *
 */
public class MakeKSubarraySumsEqual {

    // V0
    // IDEA: GCD CYCLE GROUPING + MEDIAN
    //       equal window sums <=> arr[i] == arr[(i + k) % n]. following i, i + k, i + 2k (mod n)
    //       visits exactly the indexes congruent to i mod g, g = gcd(n, k). each of the g groups
    //       must become one value; sum |x - v| is minimised at the group's median.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long makeSubKSumEqual(int[] arr, int k) {
        int n = arr.length;
        int g = gcd(n, k);

        long totalOps = 0; // long: up to 1e5 * 1e9
        for (int r = 0; r < g; r++) {
            // collect group r: arr[r], arr[r + g], arr[r + 2g], ...
            List<Integer> group = new ArrayList<>();
            for (int i = r; i < n; i += g) {
                group.add(arr[i]);
            }
            Collections.sort(group);
            int median = group.get(group.size() / 2);

            for (int x : group) {
                totalOps += Math.abs((long) x - median);
            }
        }
        return totalOps;
    }

    /** Greatest common divisor (Euclid). */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
