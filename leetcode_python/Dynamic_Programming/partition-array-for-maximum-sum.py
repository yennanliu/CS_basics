"""

1043. Partition Array for Maximum Sum
Medium

Given an integer array arr, partition the array into (contiguous) subarrays of
length at most k. After partitioning, each subarray has their values changed to
become the maximum value of that subarray.

Return the largest sum of the given array after partitioning.
Test cases are generated so that the answer fits in a 32-bit integer.


Example 1:

Input: arr = [1,15,7,9,2,5,10], k = 3
Output: 84
Explanation: arr becomes [15,15,15,9,10,10,10]

Example 2:

Input: arr = [1,4,1,5,7,3,6,1,9,9,3], k = 4
Output: 83

Example 3:

Input: arr = [1], k = 1
Output: 1


Constraints:

1 <= arr.length <= 500
0 <= arr[i] <= 10^9
1 <= k <= arr.length

"""

# V0
# IDEA: 1D DP
"""
 DP def:
    - dp[i] = largest sum obtainable from the first i elements (arr[0..i-1])

 DP eq:
    - the last subarray ends at i-1 and has length L (1 <= L <= k)
      dp[i] = max over L of ( dp[i-L] + max(arr[i-L .. i-1]) * L )
    - keep a running `mx` while walking L backwards, so no re-scan is needed

 base:
    - dp[0] = 0

 answer: dp[n]
"""
# time = O(n * k)
# space = O(n)
class Solution(object):
    def maxSumAfterPartitioning(self, arr, k):
        n = len(arr)
        dp = [0] * (n + 1)

        for i in range(1, n + 1):
            mx = 0
            # L = length of the last subarray
            for L in range(1, min(k, i) + 1):
                mx = max(mx, arr[i - L])
                dp[i] = max(dp[i], dp[i - L] + mx * L)

        return dp[n]
