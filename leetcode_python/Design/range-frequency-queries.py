"""

2080. Range Frequency Queries
Medium

Design a data structure to find the frequency of a given value in a given subarray.

The frequency of a value in a subarray is the number of occurrences of that value in the subarray.

Implement the RangeFreqQuery class:

RangeFreqQuery(int[] arr) Constructs an instance of the class with the given 0-indexed integer array arr.
int query(int left, int right, int value) Returns the frequency of value in the subarray arr[left...right].

A subarray is a contiguous sequence of elements within an array. arr[left...right] denotes the inclusive subarray consisting of the elements of arr between the indices left and right (inclusive).


Example 1:

Input
["RangeFreqQuery", "query", "query"]
[[[12, 33, 4, 56, 22, 2, 34, 33, 22, 12, 34, 56]], [1, 2, 4], [0, 11, 33]]
Output
[null, 1, 2]

Explanation
RangeFreqQuery rangeFreqQuery = new RangeFreqQuery([12, 33, 4, 56, 22, 2, 34, 33, 22, 12, 34, 56]);
rangeFreqQuery.query(1, 2, 4); // return 1. The value 4 occurs 1 time in the subarray [33, 4]
rangeFreqQuery.query(0, 11, 33); // return 2. The value 33 occurs 2 times in the whole array.


Constraints:

1 <= arr.length <= 10^5
1 <= arr[i], value <= 10^4
0 <= left <= right < arr.length
1 <= queries <= 10^5

"""

# V0
# IDEA : VALUE -> SORTED LIST OF ITS INDICES, THEN BINARY SEARCH THE RANGE
#
#   building the map is one pass, and because indices are appended in order
#   each list is already sorted. a query is then
#       bisect_right(idx, right) - bisect_left(idx, left)
#   i.e. how many of that value's positions fall inside [left, right].
#
#   NOTE : a value never seen has no entry -> frequency 0.
#
# time = O(n) build, O(log n) per query, space = O(n)
import bisect
from collections import defaultdict


class RangeFreqQuery(object):

    def __init__(self, arr):
        self.pos = defaultdict(list)
        for i, x in enumerate(arr):
            self.pos[x].append(i)

    def query(self, left, right, value):
        if value not in self.pos:
            return 0
        idx = self.pos[value]
        return bisect.bisect_right(idx, right) - bisect.bisect_left(idx, left)


# Your RangeFreqQuery object will be instantiated and called as such:
# obj = RangeFreqQuery(arr)
# param_1 = obj.query(left,right,value)
