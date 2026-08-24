"""

1477. Find Two Non-overlapping Sub-arrays Each With Target Sum
Medium

You are given an array of integers arr and an integer target.

You have to find two non-overlapping sub-arrays of arr each with a sum equal target. There can be multiple answers so you have to find an answer where the sum of the lengths of the two sub-arrays is minimum.

Return the minimum sum of the lengths of the two required sub-arrays, or return -1 if you cannot find such two sub-arrays.


Example 1:

Input: arr = [3,2,2,4,3], target = 3
Output: 2
Explanation: Only two sub-arrays have sum = 3 ([3] and [3]). The sum of their lengths is 2.

Example 2:

Input: arr = [7,3,4,7], target = 7
Output: 2
Explanation: Although we have three non-overlapping sub-arrays of sum = 7 ([7], [3,4] and [7]), but we will choose the first and third sub-arrays as the sum of their lengths is 2.

Example 3:

Input: arr = [4,3,2,6,2,3,4], target = 6
Output: -1
Explanation: We have only one sub-array of sum = 6.


Constraints:

1 <= arr.length <= 10^5
1 <= arr[i] <= 1000
1 <= target <= 10^8

"""

# V0
# IDEA : PREFIX SUM HASH + BEST-SO-FAR DP
#
#   arr[i] >= 1, so a prefix sum is strictly increasing and each value
#   appears at most once -> d[s] = index where prefix sum s occurs.
#   at position i, if s - target was seen at index j then arr[j..i-1] is a
#   valid window of length i - j.
#   f[i] = shortest valid window that ENDS at or before i.
#   pairing it with the window just found gives a candidate answer
#   f[j] + (i - j) -> the two windows cannot overlap because f[j] only
#   looks at the prefix ending at j.
#
"""

DP def
    arr[i] >= 1, so the prefix sum is STRICTLY INCREASING and each value
    appears at most once -> seen[s] = the index where prefix sum s occurs

    f[i]: length of the SHORTEST valid (sum == target) window lying entirely

          within arr[:i]      (inf if none yet)

DP eq

     at position i with prefix sum s, if s - target was seen at index j:

        length = i - j                    # arr[j..i-1] is a valid window

        res  = min(res, f[j] + length)    # pair with the best EARLIER window

        f[i] = min(f[i-1], length)


    -> e.g. the two windows cannot overlap, because f[j] only looks at the
              prefix ENDING at j - i.e. strictly left of the new window

     init: f[0] = inf, seen = {0: 0}
     ans = res, or -1 if still inf

"""
# time = O(n), space = O(n)
class Solution(object):
    def minSumOfLengths(self, arr, target):
        n = len(arr)
        INF = float('inf')

        seen = {0: 0}                 # prefix sum -> index (1-based count)
        f = [INF] * (n + 1)           # f[i] = best window within arr[:i]
        res = INF
        s = 0

        for i in range(1, n + 1):
            s += arr[i - 1]
            f[i] = f[i - 1]
            if s - target in seen:
                j = seen[s - target]
                length = i - j
                if f[j] + length < res:
                    res = f[j] + length      # pair with the best earlier window
                if length < f[i]:
                    f[i] = length
            seen[s] = i

        return -1 if res == INF else res
