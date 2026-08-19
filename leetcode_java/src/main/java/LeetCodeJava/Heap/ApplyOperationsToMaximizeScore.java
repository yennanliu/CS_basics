package LeetCodeJava.Heap;

// https://leetcode.com/problems/apply-operations-to-maximize-score/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  2818. Apply Operations to Maximize Score
 *  Hard
 *
 *  You are given an array nums of n positive integers and an integer k.
 *
 *  Initially, you start with a score of 1. You have to maximize your score by
 *  applying the following operation at most k times:
 *
 *    - Choose any non-empty subarray nums[l, ..., r] that you haven't chosen previously.
 *    - Choose an element x of nums[l, ..., r] with the highest prime score. If multiple
 *      such elements exist, choose the one with the smallest index.
 *    - Multiply your score by x.
 *
 *  The prime score of an integer x is equal to the number of distinct prime factors
 *  of x. For example, the prime score of 300 is 3 since 300 = 2 * 2 * 3 * 5 * 5.
 *
 *  Return the maximum possible score after applying at most k operations.
 *  Since the answer may be large, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: nums = [8,3,9,3,8], k = 2
 *    Output: 81
 *    Explanation: pick nums[2..2] (score 9), then nums[2..3] (nums[2] wins the tie),
 *                 so the score is 9 * 9 = 81.
 *
 *  Example 2:
 *    Input: nums = [19,12,14,6,10,18], k = 3
 *    Output: 4788
 *
 *  Constraints:
 *    1 <= nums.length == n <= 10^5
 *    1 <= nums[i] <= 10^5
 *    1 <= k <= min(n * (n + 1) / 2, 10^9)
 */
public class ApplyOperationsToMaximizeScore {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: SIEVE FOR PRIME SCORES + MONOTONIC STACK "DOMINANCE RANGE" + GREEDY
    //
    //   1) a sieve over 1..max(nums) counts DISTINCT prime factors of every value
    //      at once, far cheaper than factoring each number separately.
    //
    //   2) index i is the chosen element of nums[l..r] exactly when it beats
    //      everything else under (prime score, then smallest index):
    //        every j in [l, i-1] has score[j] <  score[i]
    //        every j in [i+1, r] has score[j] <= score[i]
    //      NOTE the asymmetry - that IS the "smallest index wins ties" rule, and
    //      getting it wrong double counts subarrays. Two monotonic stacks give
    //        left[i]  = last index on the left with score >= score[i]
    //        right[i] = first index on the right with score >  score[i]
    //      so cnt[i] = (i - left[i]) * (right[i] - i), and sum(cnt) = n(n+1)/2.
    //
    //   3) every operation costs one distinct subarray, so spend the k operations
    //      on the LARGEST values first: take min(cnt[i], k) copies of nums[i].
    //      Use fast modular exponentiation - k can reach 10^9.
    /**
     * time = O(V log log V + n log n)   // V = max(nums) <= 10^5
     * space = O(V + n)
     */
    public int maximumScore(int[] nums, int k) {
        int n = nums.length;
        int top = 0;
        for (int v : nums) {
            top = Math.max(top, v);
        }

        // --- step 1 : distinct prime factor count for every value <= top ---
        int[] omega = new int[top + 1];
        for (int p = 2; p <= top; p++) {
            if (omega[p] == 0) {                 // p untouched so far -> prime
                for (int mult = p; mult <= top; mult += p) {
                    omega[mult]++;
                }
            }
        }
        int[] score = new int[n];
        for (int i = 0; i < n; i++) {
            score[i] = omega[nums[i]];
        }

        // --- step 2 : dominance range of each index via monotonic stacks ---
        int[] left = new int[n];
        int[] right = new int[n];
        int[] stack = new int[n];
        int sp = 0;

        for (int i = 0; i < n; i++) {            // last j < i with score[j] >= score[i]
            while (sp > 0 && score[stack[sp - 1]] < score[i]) {
                sp--;
            }
            left[i] = (sp > 0) ? stack[sp - 1] : -1;
            stack[sp++] = i;
        }

        sp = 0;
        for (int i = n - 1; i >= 0; i--) {       // first j > i with score[j] > score[i]
            while (sp > 0 && score[stack[sp - 1]] <= score[i]) {
                sp--;
            }
            right[i] = (sp > 0) ? stack[sp - 1] : n;
            stack[sp++] = i;
        }

        // --- step 3 : greedily spend k operations on the largest values ---
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        final int[] ref = nums;
        Arrays.sort(order, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return ref[b] - ref[a];          // value descending
            }
        });

        long remain = k;
        long ans = 1L;
        for (int idx = 0; idx < n && remain > 0; idx++) {
            int i = order[idx];
            long cnt = (long) (i - left[i]) * (long) (right[i] - i);
            long take = Math.min(cnt, remain);
            ans = ans * modPow(nums[i], take) % MOD;
            remain -= take;
        }
        return (int) ans;
    }

    private long modPow(long base, long exp) {
        long res = 1L;
        base %= MOD;
        while (exp > 0) {
            if ((exp & 1L) == 1L) {
                res = res * base % MOD;
            }
            base = base * base % MOD;
            exp >>= 1;
        }
        return res;
    }
}
