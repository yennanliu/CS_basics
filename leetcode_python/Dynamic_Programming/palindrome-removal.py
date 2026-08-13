"""

1246. Palindrome Removal
Hard

You are given an integer array arr.

In one move, you can select a palindromic subarray arr[i], arr[i + 1], ..., arr[j] where i <= j, and remove that subarray from the given array. Note that after removing a subarray, the elements on the left and on the right of that subarray move to fill the gap left by the removal.

Return the minimum number of moves needed to remove all numbers from the array.


Example 1:

Input: arr = [1,2]
Output: 2

Example 2:

Input: arr = [1,3,4,1,5]
Output: 3
Explanation: Remove [4] then remove [1,3,1] then remove [5].


Constraints:

1 <= arr.length <= 100
1 <= arr[i] <= 20

"""

# V0
# IDEA: INTERVAL DP
"""
 DP def:
    - dp[i][j] = min moves to remove everything in arr[i..j]

 DP eq:
    - base:  dp[i][i] = 1
    - len 2: dp[i][i+1] = 1 if arr[i] == arr[i+1] else 2
    - else:
        dp[i][j] = dp[i+1][j-1]                      if arr[i] == arr[j]
                   (the two matching ends can ride along with whatever
                    palindrome removes the inner part LAST)
        dp[i][j] = min(dp[i][j], dp[i][k] + dp[k+1][j])  for k in [i, j)

 answer = dp[0][n-1]
"""
# time = O(n^3)
# space = O(n^2)
class Solution(object):
    def minimumMoves(self, arr):
        n = len(arr)
        INF = float('inf')
        dp = [[0] * n for _ in range(n)]
        for i in range(n):
            dp[i][i] = 1

        """
        NOTE !!!

            loop i DOWNWARD and j UPWARD,
            so dp[i+1][j-1] / dp[i][k] / dp[k+1][j] are all ready.
        """
        for i in range(n - 2, -1, -1):
            for j in range(i + 1, n):
                if j == i + 1:
                    dp[i][j] = 1 if arr[i] == arr[j] else 2
                else:
                    t = dp[i + 1][j - 1] if arr[i] == arr[j] else INF
                    for k in range(i, j):
                        t = min(t, dp[i][k] + dp[k + 1][j])
                    dp[i][j] = t

        return dp[0][n - 1]
