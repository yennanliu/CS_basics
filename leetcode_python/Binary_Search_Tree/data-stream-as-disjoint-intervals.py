"""

352. Data Stream as Disjoint Intervals
Hard

Given a data stream input of non-negative integers a1, a2, ..., an, summarize the numbers
seen so far as a list of disjoint intervals.

Implement the SummaryRanges class:

- SummaryRanges() Initializes the object with an empty stream.
- void addNum(int value) Adds the integer value to the stream.
- int[][] getIntervals() Returns a summary of the integers in the stream currently as a
  list of disjoint intervals [starti, endi]. The answer should be sorted by starti.


Example 1:

Input
["SummaryRanges", "addNum", "getIntervals", "addNum", "getIntervals", "addNum",
 "getIntervals", "addNum", "getIntervals", "addNum", "getIntervals"]
[[], [1], [], [3], [], [7], [], [2], [], [6], []]
Output
[null, null, [[1, 1]], null, [[1, 1], [3, 3]], null, [[1, 1], [3, 3], [7, 7]], null,
 [[1, 3], [7, 7]], null, [[1, 3], [6, 7]]]

Explanation
SummaryRanges summaryRanges = new SummaryRanges();
summaryRanges.addNum(1);      // arr = [1]
summaryRanges.getIntervals(); // return [[1, 1]]
summaryRanges.addNum(3);      // arr = [1, 3]
summaryRanges.getIntervals(); // return [[1, 1], [3, 3]]
summaryRanges.addNum(7);      // arr = [1, 3, 7]
summaryRanges.getIntervals(); // return [[1, 1], [3, 3], [7, 7]]
summaryRanges.addNum(2);      // arr = [1, 2, 3, 7]
summaryRanges.getIntervals(); // return [[1, 3], [7, 7]]
summaryRanges.addNum(6);      // arr = [1, 2, 3, 6, 7]
summaryRanges.getIntervals(); // return [[1, 3], [6, 7]]


Constraints:

0 <= value <= 10^4
At most 3 * 10^4 calls will be made to addNum and getIntervals.
At most 10^2 calls will be made to getIntervals.


Follow up: What if there are lots of merges and the number of disjoint intervals is small
compared to the size of the data stream?

"""

# V0
# IDEA : SORTED LIST OF DISJOINT INTERVALS + BINARY SEARCH (bisect)
#
#  Keep `self.intervals` sorted by start and always disjoint & non-adjacent.
#  For a new value there are only 4 cases:
#    1) already covered by the interval on the left  -> no-op
#    2) touches BOTH neighbours  -> merge the two intervals into one
#    3) touches only the left    -> extend its end
#    4) touches only the right   -> extend its start
#    else                        -> insert a fresh [value, value]
#
# time  = O(log n) search + O(n) list shift per addNum, O(n) per getIntervals
# space = O(n)
from bisect import bisect_left
class SummaryRanges(object):

    def __init__(self):
        self.intervals = []  # list of [start, end], sorted & disjoint

    def addNum(self, value):
        """
        :type value: int
        :rtype: None
        """
        arr = self.intervals

        # idx = index of the first interval whose start > value
        # ([s, e] compares > [value + 1] exactly when s >= value + 1)
        idx = bisect_left(arr, [value + 1])

        # case 1: value already inside the interval to the left -> nothing to do
        if idx > 0 and arr[idx - 1][1] >= value:
            return

        touch_left = idx > 0 and arr[idx - 1][1] + 1 == value
        touch_right = idx < len(arr) and arr[idx][0] == value + 1

        if touch_left and touch_right:
            # value glues the two neighbouring intervals together
            arr[idx - 1][1] = arr[idx][1]
            del arr[idx]
        elif touch_left:
            arr[idx - 1][1] = value
        elif touch_right:
            arr[idx][0] = value
        else:
            arr.insert(idx, [value, value])

    def getIntervals(self):
        """
        :rtype: List[List[int]]
        """
        # return a copy so callers cannot corrupt the internal state
        return [list(x) for x in self.intervals]


# Your SummaryRanges object will be instantiated and called as such:
# obj = SummaryRanges()
# obj.addNum(value)
# param_2 = obj.getIntervals()
