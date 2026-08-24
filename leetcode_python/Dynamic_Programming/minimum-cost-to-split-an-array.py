"""

2547. Minimum Cost to Split an Array
Hard

You are given an integer array nums and an integer k.

Split the array into some number of non-empty subarrays. The cost of a split is the sum of the importance value of each subarray in the split.

Let trimmed(subarray) be the version of the subarray where all numbers which appear only once are removed.

For example, trimmed([3,1,2,4,3,4]) = [3,4,3,4].

The importance value of a subarray is k + trimmed(subarray).length.

For example, if a subarray is [1,2,3,3,3,4,4], then trimmed([1,2,3,3,3,4,4]) = [3,3,3,4,4]. The importance value of this subarray will be k + 5.

Return the minimum possible cost of a split of nums.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [1,2,1,2,1,3,3], k = 2
Output: 8
Explanation: We split nums to have two subarrays: [1,2], [1,2,1,3,3].
The importance value of [1,2] is 2 + (0) = 2.
The importance value of [1,2,1,3,3] is 2 + (2 + 2) = 6.
The cost of the split is 2 + 6 = 8. It can be shown that this is the minimum possible cost among all the possible splits.

Example 2:

Input: nums = [1,2,1,2,1], k = 2
Output: 6
Explanation: We split nums to have two subarrays: [1,2], [1,2,1].
The importance value of [1,2] is 2 + (0) = 2.
The importance value of [1,2,1] is 2 + (2) = 4.
The cost of the split is 2 + 4 = 6. It can be shown that this is the minimum possible cost among all the possible splits.

Example 3:

Input: nums = [1,2,1,2,1], k = 5
Output: 10
Explanation: We split nums to have one subarray: [1,2,1,2,1].
The importance value of [1,2,1,2,1] is 5 + (3 + 2) = 10.
The cost of the split is 10. It can be shown that this is the minimum possible cost among all the possible splits.


Constraints:

1 <= nums.length <= 1000
0 <= nums[i] < nums.length
1 <= k <= 10^9

"""

# V0
# IDEA : DP OVER SUFFIXES + INCREMENTAL "APPEARS ONCE" COUNTER
#
#   dp[i] = min cost of splitting nums[i:]. dp[n] = 0, answer is dp[0].
#   transition: pick the end j of the first piece nums[i..j], pay its
#   importance, then recurse:
#       dp[i] = min over j >= i of  ( k + len - once + dp[j + 1] )
#   where len = j - i + 1 and `once` = how many DISTINCT values appear exactly
#   once inside nums[i..j]. trimmed-length = len - once.
#
#   the inner loop extends j one element at a time, so `once` is maintained in
#   O(1) per step: a value hitting count 1 adds to `once`, hitting count 2
#   removes from it (counts >= 3 change nothing).
#
#   NOTE : `once` counts DISTINCT singleton VALUES, not elements — that is why
#          only the 1 -> 2 transition decrements it, never later increments.
#   NOTE : written bottom-up (i from n-1 down to 0) rather than recursively,
#          since n can be 1000 and deep recursion is a needless risk.
#
"""

DP def
    dp[i]: MIN cost of splitting the suffix nums[i:]

    `once`: how many DISTINCT values appear EXACTLY ONCE inside the current

            candidate piece nums[i..j]   -> trimmed length = len - once

DP eq

     dp[i] = min over j >= i of

                ( k + (j - i + 1) - once(i, j) + dp[j+1] )


    -> e.g. the inner loop extends j one element at a time, so `once` is
              maintained in O(1) per step: a value hitting count 1 ADDS to
              once, hitting count 2 REMOVES from it (counts >= 3 change
              nothing)

     NOTE !!! `once` counts distinct singleton VALUES, not elements - which
              is why only the 1 -> 2 transition decrements it, never later
              increments

     written bottom-up rather than recursively (n can be 1000)

     init: dp[n] = 0
     ans = dp[0]

"""
# time = O(n^2), space = O(n)
class Solution(object):
    def minCost(self, nums, k):
        n = len(nums)
        INF = float('inf')
        dp = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            cnt = {}
            once = 0
            best = INF
            for j in range(i, n):
                v = nums[j]
                c = cnt.get(v, 0) + 1
                cnt[v] = c
                if c == 1:
                    once += 1
                elif c == 2:
                    once -= 1
                cost = k + (j - i + 1) - once + dp[j + 1]
                if cost < best:
                    best = cost
            dp[i] = best
        return dp[0]
