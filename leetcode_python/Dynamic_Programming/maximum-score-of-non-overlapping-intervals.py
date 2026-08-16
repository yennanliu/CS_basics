"""

3414. Maximum Score of Non-overlapping Intervals
Hard

You are given a 2D integer array intervals, where intervals[i] = [l_i, r_i,
weight_i]. Interval i starts at position l_i and ends at r_i, and has a weight
of weight_i. You can choose up to 4 non-overlapping intervals. The score of the
chosen intervals is defined as the total sum of their weights.

Return the lexicographically smallest array of at most 4 indices from intervals
with maximum score, representing your choice of non-overlapping intervals.

Two intervals are said to be non-overlapping if they do not share any points. In
particular, intervals sharing a left or right boundary are considered
overlapping.

Example 1:

Input: intervals = [[1,3,2],[4,5,2],[1,5,5],[6,9,3],[6,7,1],[8,9,1]]

Output: [2,3]

Explanation:

You can choose the intervals with indices 2, and 3 with respective weights of 5,
and 3.

Example 2:

Input: intervals = [[5,8,1],[6,7,7],[4,7,3],[9,10,6],[7,8,2],[11,14,3],[3,5,5]]

Output: [1,3,5,6]

Explanation:

You can choose the intervals with indices 1, 3, 5, and 6 with respective weights
of 7, 6, 3, and 5.

Constraints:

1 <= intevals.length <= 5 * 10^4
intervals[i].length == 3
intervals[i] = [l_i, r_i, weight_i]
1 <= l_i <= r_i <= 10^9
1 <= weight_i <= 10^9

"""

# V0
# IDEA : SUFFIX DP OVER (INTERVAL, BUDGET), CARRYING THE WINNING INDEX SET
#
#   sort the intervals by left endpoint.  scanning that order from the right,
#   the state is "I am allowed to use intervals sorted[i:] and may still take j
#   of them", and each step is a plain take-or-skip:
#
#     f[i][j] = better( f[i+1][j],  weight_i + f[nxt(i)][j-1] )
#
#   where nxt(i) is found by binary search — the first interval whose left
#   endpoint is strictly past r_i, since touching endpoints already count as
#   overlapping.
#
#   the tie-break is the interesting part.  among equal scores we want the
#   lexicographically smallest *sorted* list of original indices, so each state
#   stores the whole (at most 4 element) winning list and comparison is just
#   tuple order after re-sorting.  that is legal because inserting one fixed
#   index into two sorted lists preserves their lexicographic order: the
#   insertion happens at the same slot in both as long as their common prefix
#   lasts, and past the first difference the smaller list is still smaller.  so
#   the sub-problem really can be optimised on its own.
#
# time = O(n log n) with a 4-fold constant, space = O(n)
from bisect import bisect_right


class Solution(object):
    def maximumWeight(self, intervals):
        n = len(intervals)
        order = sorted(range(n), key=lambda i: intervals[i][0])
        ls = [intervals[i][0] for i in order]

        # f[i][j] = (score, sorted tuple of original indices)
        f = [[(0, ())] * 5 for _ in range(n + 1)]
        for i in range(n - 1, -1, -1):
            oi = order[i]
            l, r, w = intervals[oi]
            nxt = bisect_right(ls, r)
            row = f[i]
            below = f[i + 1]
            jump = f[nxt]
            for j in range(1, 5):
                skip = below[j]
                sc, lst = jump[j - 1]
                take = (sc + w, tuple(sorted(lst + (oi,))))
                if take[0] > skip[0] or (take[0] == skip[0] and take[1] < skip[1]):
                    row[j] = take
                else:
                    row[j] = skip
        return list(f[0][4][1])
