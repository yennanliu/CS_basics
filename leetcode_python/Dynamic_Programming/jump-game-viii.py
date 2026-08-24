"""

2297. Jump Game VIII
Medium
(premium / locked problem)

You are given a 0-indexed integer array nums of length n. You are initially standing at index 0. You can jump from index i to index j where i < j if:

nums[i] <= nums[j] and nums[k] < nums[i] for all indexes k in the range (i, j), or
nums[i] > nums[j] and nums[k] >= nums[i] for all indexes k in the range (i, j).

You are also given an integer array costs of length n where costs[i] denotes the cost of jumping to index i.

Return the minimum cost to jump to the index n - 1.


Example 1:

Input: nums = [3,2,4,4,1], costs = [3,7,6,4,2]
Output: 8
Explanation: You start at index 0.
- Jump to index 2 with a cost of costs[2] = 6.
- Jump to index 4 with a cost of costs[4] = 2.
The total cost is 8.
It can be proven that 8 is the minimum cost needed.
Two other possible paths are from index 0 -> 1 -> 4 and index 0 -> 2 -> 3 -> 4.
These have costs of 9 and 12, respectively.

Example 2:

Input: nums = [0,1,2], costs = [1,1,1]
Output: 2
Explanation: Start at index 0.
- Jump to index 1 with a cost of costs[1] = 1.
- Jump to index 2 with a cost of costs[2] = 1.
The total cost is 2. Note that you cannot jump directly from index 0 to index 2 because nums[1] <= nums[0].


Constraints:

n == nums.length == costs.length
1 <= n <= 10^5
0 <= nums[i], costs[i] <= 10^5

"""

# V0
# IDEA : EACH INDEX HAS AT MOST TWO OUTGOING JUMPS — FIND THEM WITH STACKS
#
#   read the two jump rules carefully and they collapse into "the FIRST index
#   that satisfies the boundary condition" :
#
#     rule 1 : nums[i] <= nums[j] with everything between strictly smaller
#              -> j is the NEXT GREATER-OR-EQUAL index after i
#     rule 2 : nums[i] > nums[j] with everything between >= nums[i]
#              -> j is the NEXT STRICTLY SMALLER index after i
#
#   anything further away is blocked by that first boundary, so the graph has
#   at most 2n edges. build both "next" arrays with monotonic stacks, then a
#   single forward DP relaxes them (every edge goes strictly right, so one
#   left-to-right pass is enough).
#
"""

DP def
    the two jump rules each collapse to "the FIRST index satisfying the
    boundary condition", so every index has at most TWO outgoing edges:

        next_ge[i] : next j > i with nums[j] >= nums[i]   (rule 1)
        next_lt[i] : next j > i with nums[j] <  nums[i]   (rule 2)

    dp[i]: MIN total cost to reach index i

DP eq

     dp[ next_ge[i] ] = min( dp[next_ge[i]], dp[i] + costs[next_ge[i]] )

     dp[ next_lt[i] ] = min( dp[next_lt[i]], dp[i] + costs[next_lt[i]] )


    -> e.g. anything further away is BLOCKED by that first boundary, so the
              graph has <= 2n edges, and both "next" arrays come from
              monotonic stacks

     every edge goes strictly RIGHT, so ONE left-to-right pass suffices
     (no Dijkstra needed)

     init: dp[0] = 0, rest inf
     ans = dp[n-1]

"""
# time = O(n), space = O(n)
class Solution(object):
    def minCost(self, nums, costs):
        n = len(nums)
        INF = float('inf')

        # next index j > i with nums[j] >= nums[i]
        next_ge = [-1] * n
        stack = []
        for i in range(n):
            while stack and nums[stack[-1]] <= nums[i]:
                next_ge[stack.pop()] = i
            stack.append(i)

        # next index j > i with nums[j] < nums[i]
        next_lt = [-1] * n
        stack = []
        for i in range(n):
            while stack and nums[stack[-1]] > nums[i]:
                next_lt[stack.pop()] = i
            stack.append(i)

        dp = [INF] * n
        dp[0] = 0
        for i in range(n):
            if dp[i] == INF:
                continue
            for j in (next_ge[i], next_lt[i]):
                if j != -1 and dp[i] + costs[j] < dp[j]:
                    dp[j] = dp[i] + costs[j]
        return dp[n - 1]
