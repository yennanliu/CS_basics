"""

3413. Maximum Coins From K Consecutive Bags
Medium

There are an infinite amount of bags on a number line, one bag for each
coordinate. Some of these bags contain coins.

You are given a 2D array coins, where coins[i] = [l_i, r_i, c_i] denotes that
every bag from l_i to r_i contains c_i coins.

The segments that coins contain are non-overlapping.

You are also given an integer k.

Return the maximum amount of coins you can obtain by collecting k consecutive
bags.

Example 1:

Input: coins = [[8,10,1],[1,3,2],[5,6,4]], k = 4

Output: 10

Explanation:

Selecting bags at positions [3, 4, 5, 6] gives the maximum number of coins: 2 +
0 + 4 + 4 = 10.

Example 2:

Input: coins = [[1,10,3]], k = 2

Output: 6

Explanation:

Selecting bags at positions [1, 2] gives the maximum number of coins: 3 + 3 = 6.

Constraints:

1 <= coins.length <= 10^5
1 <= k <= 10^9
coins[i] == [l_i, r_i, c_i]
1 <= l_i <= r_i <= 10^9
1 <= c_i <= 1000
The given segments are non-overlapping.

"""

# V0
# IDEA : THE BEST WINDOW ALWAYS TOUCHES A SEGMENT EDGE — SLIDE TWICE
#
#   the coordinate line is huge (up to 1e9) but only the 1e5 given segments
#   carry coins, so the answer is decided by segment boundaries.  slide a window
#   of width k: while both of its ends sit strictly inside gaps or inside a
#   single segment, moving it towards the side with the higher coin density
#   never loses.  pushing that argument to the end shows some optimal window has
#   its left edge on a segment's l, or its right edge on a segment's r.
#
#   so run two sweeps.  the first anchors the window at each l_i and adds up
#   whole segments with a two pointer, plus the partial slice of the one segment
#   that hangs over the right edge — there is never a partial on the left,
#   because the segments are disjoint and sorted so everything before i ends
#   before l_i.
#
#   the second sweep is the mirror image, obtained for free by reflecting every
#   segment through the origin ([l, r] -> [-r, -l]) and re-running the first.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maximumCoins(self, coins, k):
        def sweep(segs):
            segs.sort()
            m = len(segs)
            ans = 0
            j = 0
            cur = 0
            for i in range(m):
                if j < i:
                    j = i
                    cur = 0
                right = segs[i][0] + k - 1
                while j < m and segs[j][1] <= right:
                    cur += (segs[j][1] - segs[j][0] + 1) * segs[j][2]
                    j += 1
                part = 0
                if j < m and segs[j][0] <= right:
                    part = (right - segs[j][0] + 1) * segs[j][2]
                if cur + part > ans:
                    ans = cur + part
                if i < j:
                    cur -= (segs[i][1] - segs[i][0] + 1) * segs[i][2]
            return ans

        forward = [[l, r, c] for l, r, c in coins]
        backward = [[-r, -l, c] for l, r, c in coins]
        return max(sweep(forward), sweep(backward))
