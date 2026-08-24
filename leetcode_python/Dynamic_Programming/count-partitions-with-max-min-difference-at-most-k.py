"""

3578. Count Partitions With Max-Min Difference at Most K
Medium

You are given an integer array nums and an integer k. Your task is to partition nums into one or more non-empty contiguous segments such that in each segment, the difference between its maximum and minimum elements is at most k.

Return the total number of ways to partition nums under this condition.

Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [9,4,1,3,7], k = 4
Output: 6
Explanation:
There are 6 valid partitions where the difference between the maximum and minimum elements in each segment is at most k = 4:
[[9], [4], [1], [3], [7]]
[[9], [4], [1], [3, 7]]
[[9], [4], [1, 3], [7]]
[[9], [4, 1], [3], [7]]
[[9], [4, 1], [3, 7]]
[[9], [4, 1, 3], [7]]

Example 2:

Input: nums = [3,3,4], k = 0
Output: 2
Explanation:
There are 2 valid partitions that satisfy the given conditions:
[[3], [3], [4]]
[[3, 3], [4]]


Constraints:

2 <= nums.length <= 5 * 10^4
1 <= nums[i] <= 10^9
0 <= k <= 10^9

"""

# V0
# IDEA : DP OVER PREFIXES + SLIDING WINDOW OF FEASIBLE CUT POINTS
#
#   dp[i] counts the partitions of the first i elements. the last segment is
#   nums[j..i-1] for some j, and it is legal exactly when max-min over that
#   range is <= k. shrinking j only shrinks the range, so for a fixed i the
#   legal j form a contiguous suffix [left, i-1] — and `left` never moves
#   backwards as i grows, which is what makes the two-deque sliding window
#   valid.
#
#   summing dp[left..i-1] is then a prefix-sum lookup, giving overall linear
"""

DP def
    dp[i]: number of valid partitions of the first i elements

    pre[i]: dp[0] + dp[1] + ... + dp[i]      (prefix sum)

DP eq

     the last segment is nums[j..i-1], legal iff max - min over it <= k

     dp[i] = sum( dp[j] )  for j in [left, i-1]

           = pre[i-1] - pre[left-1]


    -> e.g. shrinking j only shrinks the range, so for a fixed i the legal
              j form a CONTIGUOUS suffix [left, i-1], and `left` never moves
              backwards as i grows

         -> a monotonic max-deque + min-deque sliding window finds `left`
            in amortised O(1), giving overall O(n)

     init: dp[0] = 1, pre[0] = 1
     ans = dp[n] % (10^9 + 7)

"""
#   time.
#
# time = O(n), space = O(n)
from collections import deque


class Solution(object):
    def countPartitions(self, nums, k):
        MOD = 10 ** 9 + 7
        n = len(nums)
        dp = [0] * (n + 1)
        pre = [0] * (n + 1)   # pre[i] = dp[0] + ... + dp[i]
        dp[0] = 1
        pre[0] = 1

        maxq, minq = deque(), deque()
        left = 0
        for i in range(1, n + 1):
            r = i - 1
            v = nums[r]
            while maxq and nums[maxq[-1]] <= v:
                maxq.pop()
            maxq.append(r)
            while minq and nums[minq[-1]] >= v:
                minq.pop()
            minq.append(r)
            while nums[maxq[0]] - nums[minq[0]] > k:
                if maxq[0] == left:
                    maxq.popleft()
                if minq[0] == left:
                    minq.popleft()
                left += 1
            total = pre[i - 1] - (pre[left - 1] if left > 0 else 0)
            dp[i] = total % MOD
            pre[i] = (pre[i - 1] + dp[i]) % MOD
        return dp[n]
