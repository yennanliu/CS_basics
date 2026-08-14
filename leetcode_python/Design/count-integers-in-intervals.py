"""

2276. Count Integers in Intervals
Hard

Given an empty set of intervals, implement a data structure that can:

Add an interval to the set of intervals.
Count the number of integers that are present in at least one interval.

Implement the CountIntervals class:

CountIntervals() Initializes the object with an empty set of intervals.
void add(int left, int right) Adds the interval [left, right] to the set of intervals.
int count() Returns the number of integers that are present in at least one interval.

Note that an interval [left, right] denotes all the integers x where left <= x <= right.


Example 1:

Input
["CountIntervals", "add", "add", "count", "add", "count"]
[[], [2, 3], [7, 10], [], [5, 8], []]
Output
[null, null, null, 6, null, 8]

Explanation
CountIntervals countIntervals = new CountIntervals(); // initialize the object with an empty set of intervals.
countIntervals.add(2, 3);  // add [2, 3] to the set of intervals.
countIntervals.add(7, 10); // add [7, 10] to the set of intervals.
countIntervals.count();    // return 6
                           // the integers 2 and 3 are present in the interval [2, 3].
                           // the integers 7, 8, 9, and 10 are present in the interval [7, 10].
countIntervals.add(5, 8);  // add [5, 8] to the set of intervals.
countIntervals.count();    // return 8
                           // the integers 2 and 3 are present in the interval [2, 3].
                           // the integers 5 and 6 are present in the interval [5, 8].
                           // the integers 7 and 8 are present in the intervals [5, 8] and [7, 10].
                           // the integers 9 and 10 are present in the interval [7, 10].


Constraints:

1 <= left <= right <= 10^9
At most 10^5 calls in total will be made to add and count.
At least one call will be made to count.

"""

# V0
# IDEA : KEEP THE INTERVALS DISJOINT AND MERGE ON INSERT
#
#   store the set as DISJOINT intervals, sorted by start, plus a running
#   `total` of covered integers. adding [left, right] :
#
#     1. locate the last interval whose start is <= right (bisect)
#     2. walk BACKWARDS while it overlaps (its end >= left), absorbing it :
#        widen [left, right], subtract its length from `total`, delete it
#     3. insert the widened interval and add its length back
#
#   each interval is inserted once and deleted at most once, so the merging
#   loop is amortised O(1) per add — the log factor comes from the bisect and
#   the list insertion.
#
#   `count()` is then just the maintained total.
#
# time = O(log n) amortised per add, O(1) per count; space = O(n)
import bisect


class CountIntervals(object):

    def __init__(self):
        self.starts = []      # sorted starts of the disjoint intervals
        self.end_of = {}      # start -> end
        self.total = 0

    def add(self, left, right):
        i = bisect.bisect_right(self.starts, right) - 1
        while i >= 0:
            st = self.starts[i]
            en = self.end_of[st]
            if en < left:
                break                      # no overlap, and none earlier either
            left = min(left, st)
            right = max(right, en)
            self.total -= en - st + 1
            del self.end_of[st]
            self.starts.pop(i)
            i -= 1

        bisect.insort(self.starts, left)
        self.end_of[left] = right
        self.total += right - left + 1

    def count(self):
        return self.total


# Your CountIntervals object will be instantiated and called as such:
# obj = CountIntervals()
# obj.add(left,right)
# param_2 = obj.count()
