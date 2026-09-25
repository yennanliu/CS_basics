"""

4056. Number of Intersecting Interval Pairs I
Easy

You are given a 2D integer array intervals of n elements, where
intervals[i] = [starti, endi] represents the closed interval from starti to
endi.

Return the number of pairs of indices (i, j) such that 0 <= i < j < n and
intervals[i] and intervals[j] intersect.

Two intervals intersect if they have at least one point in common, including
when they only share an endpoint.

Example 1:

Input: intervals = [[1,2],[2,3],[3,4]]

Output: 2

Explanation:

There are 2 intersecting interval pairs:
Intervals [1, 2] and [2, 3] intersect at the point 2.
Intervals [2, 3] and [3, 4] intersect at the point 3.

Example 2:

Input: intervals = [[1,5],[2,4],[3,6]]

Output: 3

Explanation:

There are 3 intersecting interval pairs:
The intersection of [1, 5] and [2, 4] is [2, 4].
The intersection of [1, 5] and [3, 6] is [3, 5].
The intersection of [2, 4] and [3, 6] is [3, 4].

Example 3:

Input: intervals = [[1,2],[3,4],[5,6]]

Output: 0

Explanation:

There are no intersecting interval pairs. Hence, the answer is 0.

Constraints:

2 <= n == intervals.length <= 100
intervals[i] = [starti, endi]
0 <= starti <= endi <= 100

"""

# V0
# IDEA : SORT BY START + DOUBLE LOOP WITH EARLY BREAK
#
#   pairs are unordered, so sorting does not change the answer. once sorted by
#   start, for i < j we already know start[j] >= start[i], so the two closed
#   intervals meet iff start[j] <= end[i].
#
#   and every j after the first one with start[j] > end[i] starts even later,
#   so none of them can meet interval i either -> break.
#
#   e.g. [[1,2],[2,3],[3,4]] : i=0 -> [2,3] meets (2 <= 2), [3,4] breaks (3 > 2)
#                              i=1 -> [3,4] meets (3 <= 3)            -> 2
#
# time = O(n^2), space = O(1) (sort in place aside)
class Solution(object):
    def countIntersectingIntervals(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: int
        """
        # edge
        if not intervals or len(intervals) <= 1:
            return 0

        # sort on start, then end : small -> big
        intervals.sort(key=lambda x: (x[0], x[1]))

        cnt = 0
        n = len(intervals)

        for i in range(n):
            prev = intervals[i]
            for j in range(i + 1, n):
                cur = intervals[j]
                # NOTE !!! `>` not `>=` : closed intervals sharing an endpoint DO intersect
                if cur[0] > prev[1]:
                    # no later j can reach back to prev either
                    break
                cnt += 1

        return cnt
