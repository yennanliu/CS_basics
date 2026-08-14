"""

2407. Longest Increasing Subsequence II
Hard

You are given an integer array nums and an integer k.

Find the longest subsequence of nums that meets the following requirements:

The subsequence is strictly increasing and
The difference between adjacent elements in the subsequence is at most k.

Return the length of the longest subsequence that meets the requirements.

A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: nums = [4,2,1,4,3,4,5,8,15], k = 3
Output: 5
Explanation:
The longest subsequence that meets the requirements is [1,3,4,5,8].
The subsequence has a length of 5, so we return 5.
Note that the subsequence [1,3,4,5,8,15] does not meet the requirements because 15 - 8 = 7 is larger than 3.

Example 2:

Input: nums = [7,4,5,1,8,12,4,7], k = 5
Output: 4
Explanation:
The longest subsequence that meets the requirements is [4,5,8,12].
The subsequence has a length of 4, so we return 4.

Example 3:

Input: nums = [1,5], k = 1
Output: 1
Explanation:
The longest subsequence that meets the requirements is [1].
The subsequence has a length of 1, so we return 1.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i], k <= 10^5

"""

# V0
# IDEA : DP INDEXED BY *VALUE*, WITH A SEGMENT TREE FOR RANGE MAXIMUM
#
#   best[v] = length of the longest valid subsequence ENDING with value v.
#   processing nums left to right keeps the subsequence order, and
#       best[v] = 1 + max(best[u] for u in [v - k, v - 1])
#   because the predecessor must be strictly smaller but within k.
#
#   that inner max is a RANGE MAXIMUM over the value axis, which an iterative
#   segment tree answers in O(log V). the plain O(n^2) LIS DP would be 10^10
#   here, so the tree is what makes it feasible.
#
#   the tree is a max-tree, so an update only ever raises a leaf.
#
# time = O(n log V), space = O(V)
class Solution(object):
    def lengthOfLIS(self, nums, k):
        limit = max(nums)
        size = 1
        while size < limit + 1:
            size *= 2
        tree = [0] * (2 * size)

        def update(pos, value):
            i = pos + size
            if tree[i] >= value:
                return
            tree[i] = value
            i //= 2
            while i:
                tree[i] = max(tree[2 * i], tree[2 * i + 1])
                i //= 2

        def query(lo, hi):
            """max over the inclusive value range [lo, hi]"""
            if lo > hi:
                return 0
            res = 0
            l, r = lo + size, hi + size + 1
            while l < r:
                if l & 1:
                    res = max(res, tree[l])
                    l += 1
                if r & 1:
                    r -= 1
                    res = max(res, tree[r])
                l //= 2
                r //= 2
            return res

        best = 0
        for v in nums:
            cur = 1 + query(max(1, v - k), v - 1)
            best = max(best, cur)
            update(v, cur)
        return best
