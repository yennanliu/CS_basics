"""

2655. Find Maximal Uncovered Ranges
Medium

You are given an integer n which is the length of a 0-indexed array nums, and a 0-indexed 2D-array ranges, which is a list of sub-ranges of nums (sub-ranges may overlap).

Each row ranges[i] has exactly 2 cells:

ranges[i][0], which shows the start of the ith range (inclusive)
ranges[i][1], which shows the end of the ith range (inclusive)

These ranges cover some cells of nums and leave some cells uncovered. Your task is to find all of the uncovered ranges with maximal length.

Return a 2D-array answer of the uncovered ranges, sorted by the starting point in ascending order.

By all of the uncovered ranges with maximal length, we mean satisfying two conditions:

Each uncovered cell should belong to exactly one sub-range
There should not exist two ranges (l1, r1) and (l2, r2) such that r1 + 1 = l2


Example 1:

Input: n = 10, ranges = [[3,5],[7,8]]
Output: [[0,2],[6,6],[9,9]]
Explanation: The ranges (3, 5) and (7, 8) are covered, so if we simplify the array nums to a binary array where 0 shows an uncovered cell and 1 shows a covered cell, the array becomes [0,0,0,1,1,1,0,1,1,0] in which we can observe that the ranges (0, 2), (6, 6) and (9, 9) aren't covered.

Example 2:

Input: n = 3, ranges = [[0,2]]
Output: []
Explanation: In this example, the whole of the array nums is covered and there are no uncovered cells so the output is an empty array.

Example 3:

Input: n = 7, ranges = [[2,4],[0,3]]
Output: [[5,6]]
Explanation: The ranges (0, 3) and (2, 4) are covered, so if we simplify the array nums to a binary array where 0 shows an uncovered cell and 1 shows a covered cell, the array becomes [1,1,1,1,1,0,0] in which we can observe that the range (5, 6) is uncovered.


Constraints:

1 <= n <= 10^9
0 <= ranges.length <= 10^6
ranges[i].length = 2
0 <= ranges[i][j] <= n - 1
ranges[i][0] <= ranges[i][1]

"""

# V0
# IDEA : SORT BY LEFT ENDPOINT + SWEEP THE "FURTHEST COVERED" WATERMARK
#
#   n can be 10^9 so we can NEVER materialize the binary array. But the
#   number of intervals is only up to 10^6, so we work on the intervals.
#
#   sort the intervals by their left endpoint, then sweep left -> right
#   keeping `last` = the largest index covered so far (start at -1, meaning
#   nothing is covered yet).
#
#   for each interval [l, r]:
#     - if last + 1 < l, then cells last+1 .. l-1 were never touched by any
#       earlier interval (all earlier intervals start at <= l and none
#       reached past `last`), so [last+1, l-1] is a maximal uncovered gap.
#     - last = max(last, r)
#
#   NOTE : `last = max(last, r)` is required, not `last = r` -- an interval
#          fully nested inside an earlier one (e.g. [0,3] then [2,4] after
#          sorting -> [0,3],[2,4]; or [0,9] then [2,4]) would otherwise
#          shrink the watermark and emit a bogus gap.
#
#   NOTE : the tail must be handled after the loop -- if last + 1 < n then
#          [last+1, n-1] is uncovered. This also covers ranges == [].
#
#   the output is already sorted by start because we sweep left -> right,
#   and two emitted gaps can never be adjacent (a covered interval always
#   sits between them), so both "maximal length" conditions hold.
#
# time = O(m * log m), space = O(m) for the sort (m = len(ranges))
class Solution(object):
    def findMaximalUncoveredRanges(self, n, ranges):
        res = []
        last = -1
        for l, r in sorted(ranges):
            if last + 1 < l:
                res.append([last + 1, l - 1])
            if r > last:
                last = r
        if last + 1 < n:
            res.append([last + 1, n - 1])
        return res


# V0-1
# IDEA : +1 / -1 BOUNDARY EVENTS, GAPS ARE WHERE THE COUNTER SITS AT 0
#
#   turn every interval into two events, (l, +1) and (r + 1, -1), sort them by
#   position and sweep. the running counter is how many intervals cover the
#   current position, so an uncovered stretch is exactly the span between the
#   position where the counter FELL to 0 and the next position where it leaves
#   0 again. no interval-nesting special case is needed — nesting just makes
#   the counter go to 2 and back.
#
#   NOTE : at one shared position every +1 must be applied BEFORE the -1s
#          there, otherwise touching intervals such as [0,3] and [4,6] would
#          momentarily show counter 0 and fake a gap. sorting on (pos, -delta)
#          gives that ordering.
#
#   NOTE : the tail [gap, n-1] is emitted after the loop, which also produces
#          the whole array when ranges == [].
#
# time = O(m log m), space = O(m)  (m = len(ranges))
class Solution(object):
    def findMaximalUncoveredRanges(self, n, ranges):
        events = []
        for l, r in ranges:
            events.append((l, 1))
            events.append((r + 1, -1))
        events.sort(key=lambda e: (e[0], -e[1]))

        res = []
        active = 0
        gap = 0                      # start of the current uncovered stretch
        for pos, d in events:
            if active == 0 and d == 1 and pos > gap:
                res.append([gap, pos - 1])
            active += d
            if active == 0:
                gap = pos
        if gap < n:
            res.append([gap, n - 1])
        return res


# V0-2
# IDEA : BRUTE FORCE — PAINT THE COVERED FLAGS, THEN REPORT RUNS OF ZEROS
#
#   the literal reading of the statement : build the binary array the examples
#   describe, paint every interval into it, then emit each maximal run of
#   zeros. maximality and the sorted order come for free from scanning left to
#   right.
#   unusable on the real constraints (n <= 10^9, and painting costs the total
#   interval length), but it is the definition, so it doubles as the oracle
#   for the two interval solutions above.
#
# time = O(n + total interval length), space = O(n)
class Solution(object):
    def findMaximalUncoveredRanges(self, n, ranges):
        covered = [False] * n
        for l, r in ranges:
            for i in range(l, r + 1):
                covered[i] = True

        res = []
        i = 0
        while i < n:
            if covered[i]:
                i += 1
                continue
            j = i
            while j < n and not covered[j]:
                j += 1
            res.append([i, j - 1])
            i = j
        return res
