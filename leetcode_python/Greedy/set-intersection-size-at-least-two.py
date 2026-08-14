"""

757. Set Intersection Size At Least Two
Hard

You are given a 2D integer array intervals where intervals[i] = [start_i, end_i]
represents all the integers from start_i to end_i inclusively.

A containing set is an array nums where each interval from intervals has at least
two integers in nums.

  - For example, if intervals = [[1,3], [3,7], [8,9]], then [1,2,4,7,8,9] and
    [2,3,4,8,9] are containing sets.

Return the minimum possible size of a containing set.


Example 1:

Input: intervals = [[1,3],[3,7],[8,9]]
Output: 5
Explanation: let nums = [2, 3, 4, 8, 9].
It can be shown that there cannot be any containing array of size 4.

Example 2:

Input: intervals = [[1,3],[1,4],[2,5],[3,5]]
Output: 3
Explanation: let nums = [2, 3, 4].
It can be shown that there cannot be any containing array of size 2.

Example 3:

Input: intervals = [[1,2],[2,3],[2,4],[4,5]]
Output: 5
Explanation: let nums = [1, 2, 3, 4, 5].
It can be shown that there cannot be any containing array of size 4.


Constraints:

1 <= intervals.length <= 3000
intervals[i].length == 2
0 <= start_i < end_i <= 10^8

"""

# V0
# IDEA : GREEDY (sort by end asc, start desc; always pick the LARGEST points)
#
#   Sort by end ascending — then when we must add points for an interval, picking
#   the largest possible ones (end and end-1) maximises the chance they are reused
#   by later intervals (whose ends are all >= this one).
#   The secondary "start descending" ordering makes tighter intervals come first
#   among equal ends, so we never pick points that a tighter sibling can't use.
#
#   We only need the two LARGEST chosen points (a < b) — every previously chosen
#   point is <= b, and any earlier point is too small to help future intervals.
#     - start <= a : both a and b fall in [start, end] -> nothing to add
#     - start <= b : only b is inside -> add one point, the largest (end)
#     - else       : none inside -> add two points, end - 1 and end
#
# time = O(n log n)
# space = O(1) (ignoring the sort)
class Solution(object):
    def intersectionSizeTwo(self, intervals):
        intervals.sort(key=lambda x: (x[1], -x[0]))

        res = 0
        a, b = -1, -1  # the two largest chosen points so far, a < b

        for start, end in intervals:
            if start <= a:
                # both a and b are inside [start, end] already
                continue
            if start <= b:
                # only b is inside -> one more point, as far right as allowed
                res += 1
                a, b = b, end
            else:
                # nothing inside -> take the two rightmost points
                res += 2
                a, b = end - 1, end

        return res
