package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-prime-gap-balanced-subarrays/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  3589. Count Prime-Gap Balanced Subarrays
 *  Medium
 *
 *  You are given an integer array nums and an integer k.
 *
 *  Call a subarray prime-gap balanced if:
 *   - It contains at least two prime numbers, and
 *   - The difference between the maximum and minimum prime numbers in that
 *     subarray is less than or equal to k.
 *
 *  Return the count of prime-gap balanced subarrays in nums.
 *
 *  Example 1:
 *    Input: nums = [1,2,3], k = 1
 *    Output: 2
 *    Explanation: [2,3] and [1,2,3] both hold two primes with max - min = 1.
 *
 *  Example 2:
 *    Input: nums = [2,3,5,7], k = 3
 *    Output: 4
 *    Explanation: [2,3], [2,3,5], [3,5] and [5,7] qualify.
 *
 *  Constraints:
 *    1 <= nums.length <= 5 * 10^4
 *    1 <= nums[i] <= 5 * 10^4
 *    0 <= k <= 5 * 10^4
 */
public class CountPrimeGapBalancedSubarrays {

    // V0
    // IDEA: TWO OPPOSING BOUNDS ON THE LEFT ENDPOINT, PER RIGHT ENDPOINT
    //       fix the right end r. shrinking the window from the left can only
    //       remove primes, so "max prime - min prime <= k" holds for every
    //       left >= some threshold L (monotone, maintained by two monotonic
    //       deques over prime indices). the "at least two primes" condition
    //       holds for every left <= the index of the second-to-last prime.
    //       so the valid lefts are exactly [L, secondLastPrimeIdx] and the
    //       count is its size — no inner loop needed.
    /**
     * time = O(N + M log log M)   // M = max(nums), sieve
     * space = O(N + M)
     */
    public int primeSubarray(int[] nums, int k) {
        int n = nums.length;
        int mx = 0;
        for (int v : nums) {
            mx = Math.max(mx, v);
        }
        boolean[] isPrime = new boolean[mx + 1];
        for (int i = 2; i <= mx; i++) {
            isPrime[i] = true;
        }
        for (int i = 2; (long) i * i <= mx; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= mx; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        Deque<Integer> maxq = new ArrayDeque<>(); // decreasing values
        Deque<Integer> minq = new ArrayDeque<>(); // increasing values
        int[] primeIdx = new int[n];
        int primeCnt = 0;
        int left = 0;
        long ans = 0L;

        for (int r = 0; r < n; r++) {
            int v = nums[r];
            if (v <= mx && isPrime[v]) {
                while (!maxq.isEmpty() && nums[maxq.peekLast()] <= v) {
                    maxq.pollLast();
                }
                maxq.addLast(r);
                while (!minq.isEmpty() && nums[minq.peekLast()] >= v) {
                    minq.pollLast();
                }
                minq.addLast(r);
                primeIdx[primeCnt++] = r;
            }
            while (!maxq.isEmpty() && nums[maxq.peekFirst()] - nums[minq.peekFirst()] > k) {
                if (maxq.peekFirst() == left) {
                    maxq.pollFirst();
                }
                if (!minq.isEmpty() && minq.peekFirst() == left) {
                    minq.pollFirst();
                }
                left++;
            }
            if (primeCnt >= 2) {
                int hi = primeIdx[primeCnt - 2];
                if (hi >= left) {
                    ans += hi - left + 1;
                }
            }
        }
        return (int) ans;
    }
}
