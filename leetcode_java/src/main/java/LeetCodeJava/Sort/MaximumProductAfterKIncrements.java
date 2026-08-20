package LeetCodeJava.Sort;

// https://leetcode.com/problems/maximum-product-after-k-increments/

import java.util.PriorityQueue;

/**
 *  2233. Maximum Product After K Increments
 *  Medium
 *
 *  You are given an array of non-negative integers nums and an integer k. In one
 *  operation, you may choose any element from nums and increment it by 1.
 *
 *  Return the maximum product of nums after at most k operations. Since the answer
 *  may be very large, return it modulo 10^9 + 7. Note that you should maximize the
 *  product before taking the modulo.
 *
 *  Example 1:
 *    Input: nums = [0,4], k = 5
 *    Output: 20
 *    Explanation: Increment the first number 5 times -> nums = [5,4], product 20.
 *
 *  Example 2:
 *    Input: nums = [6,3,3,2], k = 2
 *    Output: 216
 *    Explanation: nums becomes [6,4,3,3] -> 6 * 4 * 3 * 3 = 216.
 *
 *  Constraints:
 *    1 <= nums.length, k <= 10^5
 *    0 <= nums[i] <= 10^6
 */
public class MaximumProductAfterKIncrements {

    // V0
    // IDEA: GREEDY + MIN-HEAP (always feed the currently smallest element)
    //       exchange argument: if a <= b then moving one unit from b to a changes
    //       the pair product from a*b to (a+1)*(b-1) = a*b + (b - a - 1) >= a*b
    //       whenever b > a. so an optimal allocation keeps the values as EQUAL as
    //       possible, which is exactly "give every +1 to the current minimum".
    //
    //       NOTE: take the modulo only at the very end - the greedy comparison
    //             itself must use the true (unmodded) values.
    /**
     * time = O((N + K) log N)
     * space = O(N)
     */
    public int maximumProduct(int[] nums, int k) {
        final long MOD = 1_000_000_007L;

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : nums) {
            pq.add(x);
        }

        for (int i = 0; i < k; i++) {
            int smallest = pq.poll();
            pq.add(smallest + 1);
        }

        long res = 1L;
        while (!pq.isEmpty()) {
            res = (res * pq.poll()) % MOD;
        }
        return (int) res;
    }
}
