"""

1713. Minimum Operations to Make a Subsequence
Hard

You are given an array target that consists of distinct integers and another integer array arr that can have duplicates.

In one operation, you can insert any integer at any position in arr. For example, if arr = [1,4,1,2], you can add 3 in the middle and make it [1,4,3,1,2]. Note that you can insert the integer at the very beginning or end of the array.

Return the minimum number of operations needed to make target a subsequence of arr.

A subsequence of an array is a new array generated from the original array by deleting some elements (possibly none) without changing the remaining elements' relative order. For example, [2,7,4] is a subsequence of [4,2,3,7,2,1,4] (the underlined elements), while [2,4,2] is not.


Example 1:

Input: target = [5,1,3], arr = [9,4,2,3,4]
Output: 2
Explanation: You can add 5 and 1 in such a way that makes arr = [5,9,4,1,2,3,4], then target will be a subsequence of arr.

Example 2:

Input: target = [6,4,8,1,3,2], arr = [4,7,6,2,3,8,6,1]
Output: 3


Constraints:

1 <= target.length, arr.length <= 10^5
1 <= target[i], arr[i] <= 10^9
target contains no duplicates.

"""

# V0
# IDEA : LCS -> LIS (target is DISTINCT, so map values to indices) + BINARY SEARCH
#
#   answer = len(target) - LCS(target, arr) : whatever the two already share
#   in order stays put, everything else in target has to be inserted.
#
#   plain LCS is O(m*n) = 10^10, too slow. the trick is that target has NO
#   DUPLICATES, so each value has a unique position:
#     pos[v] = index of v in target
#     nums   = [pos[x] for x in arr if x in pos]
#
#   a common subsequence of target and arr <=> a STRICTLY INCREASING
#   subsequence of nums. so LCS(target, arr) == LIS(nums).
#
#   LIS in O(n log n) : `tails[L]` = smallest possible tail of an increasing
#   subsequence of length L+1; bisect_left keeps it strictly increasing.
#
#   NOTE : bisect_left (not bisect_right) - equal indices may not repeat.
#
# time = O(n * log m), space = O(m + n)
from bisect import bisect_left
class Solution(object):
    def minOperations(self, target, arr):
        pos = {}
        for i, v in enumerate(target):
            pos[v] = i

        tails = []
        for x in arr:
            if x not in pos:
                continue
            i = pos[x]
            k = bisect_left(tails, i)
            if k == len(tails):
                tails.append(i)
            else:
                tails[k] = i

        return len(target) - len(tails)
