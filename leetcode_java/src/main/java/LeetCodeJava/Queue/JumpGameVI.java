package LeetCodeJava.Queue;

// https://leetcode.com/problems/jump-game-vi/

/**
 *  1696. Jump Game VI
 *  Medium
 *
 *  You are given a 0-indexed integer array nums and an integer k.
 *
 *  You are initially standing at index 0. In one move, you can jump at most k
 *  steps forward without going outside the boundaries of the array. That is, you
 *  can jump from index i to any index in the range [i + 1, min(n - 1, i + k)]
 *  inclusive.
 *
 *  You want to reach the last index of the array (index n - 1). Your score is the
 *  sum of all nums[j] for each index j you visited in the array.
 *
 *  Return the maximum score you can get.
 *
 *  Example 1:
 *    Input: nums = [1,-1,-2,4,-7,3], k = 2
 *    Output: 7
 *    Explanation: You can choose your jumps forming the subsequence [1,-1,4,3].
 *                 The sum is 7.
 *
 *  Example 2:
 *    Input: nums = [10,-5,-2,4,0,3], k = 3
 *    Output: 17
 *    Explanation: You can choose your jumps forming the subsequence [10,4,3].
 *
 *  Constraints:
 *    1 <= nums.length, k <= 10^5
 *    -10^4 <= nums[i] <= 10^4
 */
public class JumpGameVI {

    // V0
    // IDEA: DP + MONOTONIC DEQUE (SLIDING WINDOW MAXIMUM OVER THE LAST k STATES)
    //       dp[i] = nums[i] + max(dp[i-k] ... dp[i-1])
    //       the naive max scan is O(N*k); instead keep a deque of indices whose dp
    //       values are DECREASING:
    //         - the front is the best reachable predecessor; pop it once it falls
    //           out of the window (i - front > k)
    //         - before pushing i, pop every tail with dp <= dp[i]: such a tail is
    //           both older AND worse, so it can never be the window max again
    //       NOTE: values may be negative, so we cannot greedily jump onto the max -
    //             every landing spot's score is forced, which makes this a DP.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxResult(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];

        // ring-free array deque of indices
        int[] dq = new int[n];
        int head = 0;
        int tail = 0;
        dq[tail++] = 0;

        for (int i = 1; i < n; i++) {
            while (head < tail && i - dq[head] > k) {
                head++;
            }
            dp[i] = nums[i] + dp[dq[head]];
            while (head < tail && dp[dq[tail - 1]] <= dp[i]) {
                tail--;
            }
            dq[tail++] = i;
        }
        return dp[n - 1];
    }
}
