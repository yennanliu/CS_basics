"""

3505. Minimum Operations to Make Elements Within K Subarrays Equal
Hard

You are given an integer array nums and two integers, x and k. You can perform
the following operation any number of times (including zero):

Increase or decrease any element of nums by 1.

Return the minimum number of operations needed to have at least k
non-overlapping subarrays of size exactly x in nums, where all elements within
each subarray are equal.

Example 1:

Input: nums = [5,-2,1,3,7,3,6,4,-1], x = 3, k = 2

Output: 8

Explanation:

Use 3 operations to add 3 to nums[1] and use 2 operations to subtract 2 from
nums[3]. The resulting array is [5, 1, 1, 1, 7, 3, 6, 4, -1].

Use 1 operation to add 1 to nums[5] and use 2 operations to subtract 2 from
nums[6]. The resulting array is [5, 1, 1, 1, 7, 4, 4, 4, -1].

Now, all elements within each subarray [1, 1, 1] (from indices 1 to 3) and [4,
4, 4] (from indices 5 to 7) are equal. Since 8 total operations were used, 8 is
the output.

Example 2:

Input: nums = [9,-2,-2,-2,1,5], x = 2, k = 2

Output: 3

Explanation:

Use 3 operations to subtract 3 from nums[4]. The resulting array is [9, -2, -2,
-2, -2, 5].

Now, all elements within each subarray [-2, -2] (from indices 1 to 2) and [-2,
-2] (from indices 3 to 4) are equal. Since 3 operations were used, 3 is the
output.

Constraints:

2 <= nums.length <= 10^5

-10^6 <= nums[i] <= 10^6

2 <= x <= nums.length

1 <= k <= 15

2 <= k * x <= nums.length

"""

# V0
# IDEA : SLIDING WINDOW MEDIAN COST + DP OVER THE NUMBER OF PICKED WINDOWS
#
#   making every element of one window equal costs sum|a_i - t| for the target
#   t we settle on, and that sum is minimised at the median of the window.  so
#   every window of size x carries a fixed, independent price -- two chosen
#   windows never interact, because they are required to be disjoint.
#
#   step 1: price every window.  consecutive windows differ by one insertion
#   and one deletion, so a two-heap structure (max-heap holding the lower half,
#   min-heap holding the upper half) plus the two half-sums gives the median
#   and the cost in O(log n) per shift.  tagging every heap entry with its
#   index makes lazy deletion unambiguous even when values repeat.
#   with median v and m = (x + 1) // 2 elements in the lower half,
#   cost = (v*m - sumLow) + (sumHigh - v*(x - m)).
#
#   step 2: choose k of those windows, pairwise non-overlapping, minimising the
#   total price.  scanning left to right there are only two options at each
#   prefix length i: no window ends at i (dp[j][i-1]), or one does
#   (dp[j-1][i-x] + cost[i-x]).  k <= 15 makes this pass cheap.
#
"""

DP def
    making one window equal costs sum |a_i - t|, minimised at the MEDIAN - so
    every window of size x carries a fixed, INDEPENDENT price (two chosen
    windows never interact, being required to be disjoint)

    cost[i]  : price of the window nums[i : i+x]

    dp[j][i] : MIN total price having chosen j pairwise non-overlapping

               windows entirely inside nums[:i]

DP eq

     step 1) cost[i] via a SLIDING-WINDOW MEDIAN: a max-heap for the lower
             half and a min-heap for the upper half, plus their two sums

        with median v and m = (x+1)//2 elements below:
        cost = (v*m - sumLow) + (sumHigh - v*(x - m))

     step 2) dp[j][i] = min(
                dp[j][i-1],                        # no window ends at i
                dp[j-1][i-x] + cost[i-x]           # one does
             )


    -> e.g. tagging every heap entry with its INDEX makes lazy deletion
              unambiguous even when values repeat

     k <= 15 makes step 2 cheap
     init: dp[0][i] = 0
     ans = dp[k][n]

"""
# time = O(n * log n + n * k), space = O(n)
import heapq


class Solution(object):
    def minOperations(self, nums, x, k):
        n = len(nums)
        cost = self._window_costs(nums, x)     # cost[i] = price of nums[i:i+x]
        INF = float("inf")
        prev = [0] * (n + 1)                   # j = 0 windows -> nothing to pay
        for j in range(1, k + 1):
            cur = [INF] * (n + 1)
            for i in range(j * x, n + 1):
                cand = prev[i - x] + cost[i - x]
                cur[i] = cur[i - 1] if cur[i - 1] < cand else cand
            prev = cur
        return prev[n]

    def _window_costs(self, nums, x):
        n = len(nums)
        m = (x + 1) // 2                       # the lower half owns the median
        lo = []                                # max-heap of (-value, index)
        hi = []                                # min-heap of (value, index)
        side = [0] * n                         # 0 -> in lo, 1 -> in hi
        dead = [False] * n
        lo_n = hi_n = 0
        lo_s = hi_s = 0

        def prune_lo():
            while lo and dead[lo[0][1]]:
                heapq.heappop(lo)

        def prune_hi():
            while hi and dead[hi[0][1]]:
                heapq.heappop(hi)

        res = []
        for i, v in enumerate(nums):
            prune_lo()
            if lo_n and v <= -lo[0][0]:
                heapq.heappush(lo, (-v, i))
                side[i] = 0
                lo_n += 1
                lo_s += v
            else:
                heapq.heappush(hi, (v, i))
                side[i] = 1
                hi_n += 1
                hi_s += v
            if i >= x:                          # the element leaving the window
                j = i - x
                dead[j] = True
                if side[j] == 0:
                    lo_n -= 1
                    lo_s -= nums[j]
                else:
                    hi_n -= 1
                    hi_s -= nums[j]
                prune_lo()
                prune_hi()
            while lo_n > m:
                prune_lo()
                nv, idx = heapq.heappop(lo)
                t = -nv
                lo_n -= 1
                lo_s -= t
                heapq.heappush(hi, (t, idx))
                side[idx] = 1
                hi_n += 1
                hi_s += t
            while lo_n < m and hi_n:
                prune_hi()
                t, idx = heapq.heappop(hi)
                hi_n -= 1
                hi_s -= t
                heapq.heappush(lo, (-t, idx))
                side[idx] = 0
                lo_n += 1
                lo_s += t
            if i >= x - 1:
                prune_lo()
                med = -lo[0][0]
                res.append((med * lo_n - lo_s) + (hi_s - med * hi_n))
        return res
