"""

715. Range Module
Hard

A Range Module is a module that tracks ranges of numbers. Design a data structure
to track the ranges represented as half-open intervals and query about them.

A half-open interval [left, right) denotes all the real numbers x where left <= x < right.

Implement the RangeModule class:

  - RangeModule() Initializes the object of the data structure.
  - void addRange(int left, int right) Adds the half-open interval [left, right),
    tracking every real number in that interval. Adding an interval that partially
    overlaps with currently tracked numbers should add any numbers in the interval
    [left, right) that are not already tracked.
  - boolean queryRange(int left, int right) Returns true if every real number in the
    interval [left, right) is currently being tracked, and false otherwise.
  - void removeRange(int left, int right) Stops tracking every real number currently
    being tracked in the half-open interval [left, right).


Example 1:

Input
["RangeModule", "addRange", "removeRange", "queryRange", "queryRange", "queryRange"]
[[], [10, 20], [14, 16], [10, 14], [13, 15], [16, 17]]
Output
[null, null, null, true, false, true]

Explanation
RangeModule rangeModule = new RangeModule();
rangeModule.addRange(10, 20);
rangeModule.removeRange(14, 16);
rangeModule.queryRange(10, 14); // return True,(Every number in [10, 14) is being tracked)
rangeModule.queryRange(13, 15); // return False,(Numbers like 14, 14.03, 14.17 in [13, 15) are not being tracked)
rangeModule.queryRange(16, 17); // return True, (The number 16 in [16, 17) is still being tracked, despite the remove operation)


Constraints:

1 <= left < right <= 10^9
At most 10^4 calls will be made to addRange, queryRange, and removeRange.

"""

import bisect


# V0
# IDEA : SORTED LIST OF BOUNDARIES
#
#   Keep a single flat sorted list `bounds` of the endpoints of the tracked,
#   pairwise disjoint, half-open intervals:
#
#       bounds = [s0, e0, s1, e1, ...]   ->  [s0,e0) U [s1,e1) U ...
#
#   So an EVEN index is a range start and an ODD index is a range end.
#   That single parity fact drives all three operations:
#
#     - a point x is tracked  <=>  bisect_right(bounds, x) is ODD
#     - adding    [l, r): splice out everything between l and r, keeping l
#                 only if l was NOT already inside a range (index even),
#                 and keeping r only if r was NOT inside a range.
#     - removing  [l, r): the mirror image (keep the boundary when the index is ODD,
#                 i.e. when we are cutting a hole inside an existing range).
#
# time = O(n) per addRange / removeRange (list splice), O(log n) per queryRange
# space = O(n), n = number of tracked disjoint intervals
class RangeModule(object):

    def __init__(self):
        # flat sorted boundary list; even idx = start, odd idx = end
        self.bounds = []

    def addRange(self, left, right):
        i = bisect.bisect_left(self.bounds, left)
        j = bisect.bisect_right(self.bounds, right)

        merged = []
        # `left` sits outside any tracked range -> it becomes the new start
        if i % 2 == 0:
            merged.append(left)
        # `right` sits outside any tracked range -> it becomes the new end
        if j % 2 == 0:
            merged.append(right)

        # everything strictly between is swallowed by the new range
        self.bounds[i:j] = merged

    def queryRange(self, left, right):
        i = bisect.bisect_right(self.bounds, left)
        j = bisect.bisect_left(self.bounds, right)
        # no boundary in between (i == j) AND `left` is inside a range (odd index)
        return i == j and i % 2 == 1

    def removeRange(self, left, right):
        i = bisect.bisect_left(self.bounds, left)
        j = bisect.bisect_right(self.bounds, right)

        kept = []
        # `left` is inside a tracked range -> that range now ends at `left`
        if i % 2 == 1:
            kept.append(left)
        # `right` is inside a tracked range -> a new range starts at `right`
        if j % 2 == 1:
            kept.append(right)

        self.bounds[i:j] = kept


# Your RangeModule object will be instantiated and called as such:
# obj = RangeModule()
# obj.addRange(left,right)
# param_2 = obj.queryRange(left,right)
# obj.removeRange(left,right)
