"""

1420. Build Array Where You Can Find The Maximum Exactly K Comparisons
Hard

You are given three integers n, m and k. Consider the following algorithm to find the maximum element of an array of positive integers:

  maximum_value = -1
  maximum_index = -1
  search_cost = 0
  n = arr.length
  for (i = 0; i < n; i++) {
      if (maximum_value < arr[i]) {
          maximum_value = arr[i]
          maximum_index = i
          search_cost = search_cost + 1
      }
  }
  return maximum_index

You should build the array arr which has the following properties:

arr has exactly n integers.
1 <= arr[i] <= m where (0 <= i < n).
After applying the mentioned algorithm to arr, the value search_cost is equal to k.

Return the number of ways to build the array arr under the mentioned conditions. As the answer may grow large, the answer must be computed modulo 10^9 + 7.


Example 1:

Input: n = 2, m = 3, k = 1
Output: 6
Explanation: The possible arrays are [1, 1], [2, 1], [2, 2], [3, 1], [3, 2] [3, 3]

Example 2:

Input: n = 5, m = 2, k = 3
Output: 0
Explanation: There are no possible arrays that satisfy the mentioned conditions.

Example 3:

Input: n = 9, m = 1, k = 1
Output: 1
Explanation: The only possible array is [1, 1, 1, 1, 1, 1, 1, 1, 1]


Constraints:

1 <= n <= 50
1 <= m <= 100
0 <= k <= n

"""

# V0
# IDEA : DP ON (length, prefix max, cost) + PREFIX SUM
#
#   search_cost counts how many times a new prefix maximum appears, so the
#   state must remember the running maximum.
#   dp[c][j] = # of arrays built so far whose prefix max is j and cost is c.
#   appending one more element:
#     - value <= j  (j choices) -> max and cost unchanged : dp[c][j] * j
#     - value == j and j is new -> cost grows : sum of dp[c-1][j0], j0 < j
#   the inner sum is a running prefix sum over j, which drops the naive
#   O(n*m^2*k) down to O(n*m*k).
#   NOTE : k == 0 is impossible for a non-empty array -> answer 0.
#
# time = O(n * m * k), space = O(m * k)
class Solution(object):
    def numOfArrays(self, n, m, k):
        MOD = 10 ** 9 + 7
        if k == 0 or k > m:
            return 0

        # dp[c][j] after placing 1 element
        dp = [[0] * (m + 1) for _ in range(k + 1)]
        for j in range(1, m + 1):
            dp[1][j] = 1

        for i in range(2, n + 1):
            nxt = [[0] * (m + 1) for _ in range(k + 1)]
            for c in range(1, min(k, i) + 1):
                pre = 0                      # sum of dp[c-1][j0] for j0 < j
                for j in range(1, m + 1):
                    nxt[c][j] = (dp[c][j] * j + pre) % MOD
                    pre = (pre + dp[c - 1][j]) % MOD
            dp = nxt

        return sum(dp[k][1:]) % MOD
