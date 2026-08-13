"""

1340. Jump Game V
Hard

Given an array of integers arr and an integer d.
In one step you can jump from index i to index:

- i + x where: i + x < arr.length and 0 < x <= d.
- i - x where: i - x >= 0 and 0 < x <= d.

In addition, you can only jump from index i to index j if arr[i] > arr[j]
and arr[i] > arr[k] for all indices k between i and j
(More formally min(i, j) < k < max(i, j)).

You can choose any index of the array and start jumping.
Return the maximum number of indices you can visit.

Notice that you can not jump outside of the array at any time.


Example 1:

Input: arr = [6,4,14,6,8,13,9,7,10,6,12], d = 2
Output: 4
Explanation: You can start at index 10. You can jump 10 --> 8 --> 6 --> 7
as shown.
Note that if you start at index 6 you can only jump to index 7.
You cannot jump to index 5 because 13 > 9. You cannot jump to index 4 because
index 5 is between index 4 and 6 and 13 > 9.
Similarly You cannot jump from index 3 to index 2 or index 1.

Example 2:

Input: arr = [3,3,3,3,3], d = 3
Output: 1
Explanation: You can start at any index. You always cannot jump to any index.

Example 3:

Input: arr = [7,6,5,4,3,2,1], d = 1
Output: 7
Explanation: Start at index 0. You can visit all the indicies.


Constraints:

1 <= arr.length <= 1000
1 <= arr[i] <= 10^5
1 <= d <= arr.length

"""

# V0
# IDEA: SORT BY VALUE + DP
"""
 DP def:
    - dp[i] = max number of indices visitable when STARTING at index i

 key insight:
    - a jump always goes to a STRICTLY SMALLER value
      -> if we process indices in ASCENDING value order,
         every reachable target dp[j] is already final.
    - the "no taller bar in between" rule is handled by scanning
      outward from i and BREAKING the moment we hit arr[j] >= arr[i].

 DP eq:
    - dp[i] = max(dp[i], 1 + dp[j]) for each valid neighbor j
"""
# time = O(n log n + n * d)
# space = O(n)
class Solution(object):
    def maxJumps(self, arr, d):
        n = len(arr)
        dp = [1] * n

        # process indices from the smallest value to the largest
        order = sorted(range(n), key=lambda i: arr[i])

        for i in order:
            # scan left
            for j in range(i - 1, -1, -1):
                if i - j > d or arr[j] >= arr[i]:
                    break
                dp[i] = max(dp[i], 1 + dp[j])
            # scan right
            for j in range(i + 1, n):
                if j - i > d or arr[j] >= arr[i]:
                    break
                dp[i] = max(dp[i], 1 + dp[j])

        return max(dp)


# V0-1
# IDEA: MEMOIZED DFS (top down)
#  distinct trick: recursion instead of value-ordered iteration
#  NOTE: recursion depth can reach n, so we raise the recursion limit
# time = O(n * d)
# space = O(n)
import sys
class Solution(object):
    def maxJumps(self, arr, d):
        n = len(arr)
        sys.setrecursionlimit(max(10000, n * 2 + 100))
        memo = {}

        def dfs(i):
            if i in memo:
                return memo[i]
            res = 1
            for j in range(i - 1, -1, -1):
                if i - j > d or arr[j] >= arr[i]:
                    break
                res = max(res, 1 + dfs(j))
            for j in range(i + 1, n):
                if j - i > d or arr[j] >= arr[i]:
                    break
                res = max(res, 1 + dfs(j))
            memo[i] = res
            return res

        return max(dfs(i) for i in range(n))
