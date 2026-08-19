package LeetCodeJava.Stack;

// https://leetcode.com/problems/collecting-chocolates/

/**
 *  2735. Collecting Chocolates
 *  Medium
 *
 *  You are given a 0-indexed integer array nums of size n representing the cost
 *  of collecting different chocolates. The cost of collecting the chocolate at
 *  index i is nums[i]. Each chocolate is of a different type, and initially the
 *  chocolate at index i is of ith type.
 *
 *  In one operation, you can do the following with an incurred cost of x:
 *  simultaneously change the chocolate of ith type to ((i + 1) mod n)th type
 *  for all chocolates.
 *
 *  Return the minimum cost to collect chocolates of all types, given that you
 *  can perform as many operations as you would like.
 *
 *  Example 1:
 *    Input: nums = [20,1,15], x = 5
 *    Output: 13
 *    Explanation: buy type 1 for 1, rotate (5), buy type 2 for 1, rotate (5),
 *                 buy type 0 for 1 -> 1 + 5 + 1 + 5 + 1 = 13
 *
 *  Example 2:
 *    Input: nums = [1,2,3], x = 4
 *    Output: 6
 *    Explanation: buy everything at its own price, no operation.
 *
 *  Constraints:
 *    1 <= nums.length <= 1000
 *    1 <= nums[i] <= 10^9
 *    1 <= x <= 10^9
 */
public class CollectingChocolates {

    // V0
    // IDEA: ENUMERATE THE TOTAL NUMBER OF ROTATIONS + RUNNING PREFIX MIN
    //       The operations are global rotations and buying may happen at any
    //       moment, so the only real decision is k = how many rotations we do.
    //       Fix k: we pay x * k, and each type t can be bought at any of the
    //       k + 1 snapshots -> price min(nums[t], nums[t-1], ..., nums[t-k]).
    //       answer = min over k in [0, n-1] of ( x * k + sum_t best(t, k) ).
    //       k >= n never helps (at k = n-1 the window covers the whole array).
    //       best(t, k) = min(best(t, k-1), nums[(t - k) % n]) -> sweep k upward
    //       per starting index with ONE running minimum.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public long minCost(int[] nums, int x) {
        int n = nums.length;
        long[] cost = new long[n]; // cost[k] = total cost with exactly k rotations
        for (int k = 0; k < n; k++) {
            cost[k] = (long) x * k;
        }
        for (int i = 0; i < n; i++) {
            int cur = nums[i];
            for (int k = 0; k < n; k++) {
                int v = nums[((i - k) % n + n) % n];
                if (v < cur) {
                    cur = v;
                }
                cost[k] += cur;
            }
        }
        long res = cost[0];
        for (int k = 1; k < n; k++) {
            res = Math.min(res, cost[k]);
        }
        return res;
    }
}
