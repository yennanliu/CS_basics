"""

3117. Minimum Sum of Values by Dividing Array
Hard

You are given two arrays nums and andValues of length n and m respectively.

The value of an array is equal to the last element of that array.

You have to divide nums into m disjoint contiguous subarrays such that for the ith subarray [li, ri], the bitwise AND of the subarray elements is equal to andValues[i], in other words, nums[li] & nums[li + 1] & ... & nums[ri] == andValues[i] for all 1 <= i <= m, where & represents the bitwise AND operator.

Return the minimum possible sum of the values of the m subarrays nums is divided into. If it is not possible to divide nums into m subarrays satisfying these conditions, return -1.


Example 1:

Input: nums = [1,4,3,3,2], andValues = [0,3,3,2]
Output: 12
Explanation:
The only possible way to divide nums is:
[1,4] as 1 & 4 == 0.
[3] as the bitwise AND of a single element subarray is that element itself.
[3] as the bitwise AND of a single element subarray is that element itself.
[2] as the bitwise AND of a single element subarray is that element itself.
The sum of the values for these subarrays is 4 + 3 + 3 + 2 = 12.

Example 2:

Input: nums = [2,3,5,7,7,7,5], andValues = [0,7,5]
Output: 17
Explanation:
There are three ways to divide nums:
[[2,3,5],[7,7,7],[5]] with the sum of the values 5 + 7 + 5 == 17.
[[2,3,5,7],[7,7],[5]] with the sum of the values 7 + 7 + 5 == 19.
[[2,3,5,7,7],[7],[5]] with the sum of the values 7 + 7 + 5 == 19.
The minimum possible sum of the values is 17.

Example 3:

Input: nums = [1,2,3,4], andValues = [2]
Output: -1
Explanation:
The bitwise AND of the entire array nums is 0. As there is no possible way to divide nums into a single subarray to have the bitwise AND of elements 2, return -1.


Constraints:

1 <= n == nums.length <= 10^4
1 <= m == andValues.length <= 10
1 <= nums[i] < 10^5
0 <= andValues[j] < 10^5

"""

# V0
# IDEA : DP BY SEGMENT INDEX, CARRYING THE FEW DISTINCT SUFFIX-AND VALUES
#
#   dp[j][i] = min total value after splitting the first i elements into j
#   subarrays. the naive transition tries every start of the j-th subarray,
#   which is O(n^2 * m).
#
#   the saving trick : as the start of a subarray ending at i moves left, the
#   running AND only loses bits, so it takes at most ~17 DISTINCT values.
#   keep them as a dict
#
#       cur[a] = best dp[j-1][start] over all starts whose AND from start to
#                i equals a
#
#   extending i to i+1 maps every key a to a & nums[i], merging collisions by
#   the smaller dp value, plus the fresh single-element start.
#
#   whenever the wanted andValues[j-1] is a key, the segment may close here
#   at a cost of nums[i] (the value of a subarray is its LAST element).
#
"""

DP def
    dp[j][i]: MIN total value after splitting the first i elements into

              exactly j subarrays

    cur[a]  : (the speed-up) best dp[j-1][start] over all starts whose

              running AND from start to i equals a

DP eq

     naive: dp[j][i] = min over starts of dp[j-1][start] + nums[i-1]
            when AND(start..i-1) == andValues[j-1]     -> O(n^2 * m)

     instead: as the start moves LEFT the running AND only LOSES bits, so it
     takes at most ~17 DISTINCT values. extending i to i+1 maps every key a
     to a & nums[i], merging collisions by the SMALLER dp value, plus the
     fresh single-element start:

        cur_new[a & nums[i]] = min(...)  ;  cur_new[nums[i]] = min(..., dp[j-1][i])

     whenever andValues[j-1] is a key, the segment may CLOSE here:

        dp[j][i+1] = min(dp[j][i+1], cur[target] + nums[i])


    -> e.g. the value of a subarray is its LAST element, hence + nums[i]

     init: dp[0][0] = 0
     ans = dp[m][n], or -1 if inf

"""
# time = O(n * m * 17), space = O(n)
class Solution(object):
    def minimumValueSum(self, nums, andValues):
        n, m = len(nums), len(andValues)
        INF = float('inf')

        prev = [INF] * (n + 1)          # dp[0][i] : 0 elements used
        prev[0] = 0

        for j in range(1, m + 1):
            target = andValues[j - 1]
            cur_dp = [INF] * (n + 1)
            states = {}                 # running AND -> best dp[j-1][start]
            for i in range(n):
                nxt = {}
                for a, best in states.items():
                    v = a & nums[i]
                    if v not in nxt or best < nxt[v]:
                        nxt[v] = best
                if prev[i] < INF:       # a new subarray may start at i
                    v = nums[i]
                    if v not in nxt or prev[i] < nxt[v]:
                        nxt[v] = prev[i]
                states = nxt
                if target in states and states[target] < INF:
                    cur_dp[i + 1] = states[target] + nums[i]
            prev = cur_dp

        return -1 if prev[n] == INF else prev[n]
