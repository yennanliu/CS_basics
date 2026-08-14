"""

1714. Sum Of Special Evenly-Spaced Elements In Array
Hard

You are given a 0-indexed integer array nums consisting of n non-negative integers.

You are also given an array queries, where queries[i] = [xi, yi]. The answer to the ith query is the sum of all nums[j] where xi <= j < n and (j - xi) is divisible by yi.

Return an array answer where answer.length == queries.length and answer[i] is the answer to the ith query modulo 10^9 + 7.


Example 1:

Input: nums = [0,1,2,3,4,5,6,7], queries = [[0,3],[5,1],[4,2]]
Output: [9,18,10]
Explanation: The answers of the queries are as follows:
1) The j indices that satisfy this query are 0, 3, and 6. nums[0] + nums[3] + nums[6] = 9
2) The j indices that satisfy this query are 5, 6, and 7. nums[5] + nums[6] + nums[7] = 18
3) The j indices that satisfy this query are 4 and 6. nums[4] + nums[6] = 10

Example 2:

Input: nums = [100,200,101,201,102,202,103,203], queries = [[0,7]]
Output: [303]


Constraints:

n == nums.length
1 <= n <= 5 * 10^4
0 <= nums[i] <= 10^9
1 <= queries.length <= 1.5 * 10^5
0 <= xi < n
1 <= yi <= 5 * 10^4

"""

# V0
# IDEA : SQRT DECOMPOSITION (precompute small steps, brute force big steps)
#
#   a query [x, y] wants nums[x] + nums[x+y] + nums[x+2y] + ...
#
#   two regimes, split at B = sqrt(n):
#     - y >  B : the walk visits < n/B = sqrt(n) cells -> just add them up.
#     - y <= B : too many cells to walk per query, but there are only B
#                distinct step values, so PRECOMPUTE a suffix table
#                    suf[y][j] = nums[j] + suf[y][j + y]
#                built right-to-left; then the answer is suf[y][x], O(1).
#
#   precompute costs O(B * n) = O(n * sqrt(n)), each query is O(sqrt(n)).
#
#   NOTE : suf has n+1 columns so the j + y overflow lands on a 0 sentinel;
#          clamp with min(n, j + y).
#
# time = O((n + q) * sqrt(n)), space = O(n * sqrt(n))
class Solution(object):
    def solve(self, nums, queries):
        MOD = 10 ** 9 + 7

        n = len(nums)
        b = int(n ** 0.5)

        # suf[y][j] for 1 <= y <= b
        suf = [None] * (b + 1)
        for y in range(1, b + 1):
            cur = [0] * (n + 1)
            for j in range(n - 1, -1, -1):
                cur[j] = (cur[min(n, j + y)] + nums[j]) % MOD
            suf[y] = cur

        res = []
        for x, y in queries:
            if y <= b:
                res.append(suf[y][x])
            else:
                s = 0
                for j in range(x, n, y):
                    s += nums[j]
                res.append(s % MOD)

        return res
