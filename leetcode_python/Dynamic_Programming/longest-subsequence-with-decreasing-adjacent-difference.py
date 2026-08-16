"""

3409. Longest Subsequence With Decreasing Adjacent Difference
Medium

You are given an array of integers nums.

Your task is to find the length of the longest subsequence seq of nums, such
that the absolute differences between consecutive elements form a non-increasing
sequence of integers. In other words, for a subsequence seq_0, seq_1, seq_2,
..., seq_m of nums, |seq_1 - seq_0| >= |seq_2 - seq_1| >= ... >= |seq_m - seq_m
- 1|.

Return the length of such a subsequence.

Example 1:

Input: nums = [16,6,3]

Output: 3

Explanation:

The longest subsequence is [16, 6, 3] with the absolute adjacent differences
[10, 3].

Example 2:

Input: nums = [6,5,3,4,2,1]

Output: 4

Explanation:

The longest subsequence is [6, 4, 2, 1] with the absolute adjacent differences
[2, 2, 1].

Example 3:

Input: nums = [10,20,10,19,10,20]

Output: 5

Explanation:

The longest subsequence is [10, 20, 10, 19, 10] with the absolute adjacent
differences [10, 10, 9, 9].

Constraints:

2 <= nums.length <= 10^4
1 <= nums[i] <= 300

"""

# V0
# IDEA : DP KEYED ON (LAST VALUE, DIFFERENCE FLOOR) — VALUES ARE ONLY 1..300
#
#   the only thing the future cares about is the last value we picked and how
#   big the last gap was, and both live in [0, 300].  so define
#
#     dp[v][d] = longest valid subsequence seen so far that ends at value v and
#                whose final gap is *at least* d.
#
#   storing "at least d" instead of "exactly d" is the trick: appending a new
#   element x after such a subsequence is legal precisely when |x - v| <= the
#   final gap, so a single lookup dp[v][|x-v|] answers the question without
#   scanning all larger gaps.
#
#   for a new element x we therefore try every gap d and look at the two values
#   that sit exactly d away, x - d and x + d.  the fresh entry we build has gap
#   exactly d, so it must also be folded into every smaller floor — that is the
#   `row[d] = max(..., row[d+1])` sweep from large d down to 0, which restores
#   the "at least" invariant in one pass.
#
#   a lone element is a valid subsequence of length 1 for every floor, which is
#   why the candidate is best + 1 even when best is 0.
#
# time = O(n * V) with V = 301, space = O(V^2)
class Solution(object):
    def longestSubsequence(self, nums):
        V = 300
        dp = [[0] * (V + 2) for _ in range(V + 1)]
        ans = 1
        for x in nums:
            row = [0] * (V + 2)
            for d in range(V, -1, -1):
                best = 0
                lo = x - d
                if lo >= 0:
                    v = dp[lo][d]
                    if v > best:
                        best = v
                hi = x + d
                if hi <= V:
                    v = dp[hi][d]
                    if v > best:
                        best = v
                cand = best + 1
                nxt = row[d + 1]
                row[d] = cand if cand > nxt else nxt
            cur = dp[x]
            for d in range(V + 1):
                if row[d] > cur[d]:
                    cur[d] = row[d]
            if cur[0] > ans:
                ans = cur[0]
        return ans
