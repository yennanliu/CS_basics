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


# V0-1
# IDEA : HASHMAP (VALUE -> SORTED LIST OF ITS INDICES) + BINARY SEARCH (GPT)
from collections import defaultdict
class RangeFreqQuery(object):

    def __init__(self, arr):
        """
        :type arr: List[int]
        """
        # {value: sorted list of indices}
        self.val_idx = defaultdict(list)

        for i, val in enumerate(arr):
            self.val_idx[val].append(i)

    def query(self, left, right, value):
        """
        :type left: int
        :type right: int
        :type value: int
        :rtype: int
        """
        if value not in self.val_idx:
            return 0

        indices = self.val_idx[value]

        # First position where indices[pos] >= left
        l = self.get_left_bound(indices, left)

        # First position where indices[pos] > right
        r = self.get_right_bound(indices, right)

        return r - l

    def get_left_bound(self, indices, target):
        l = 0
        r = len(indices) - 1
        res = len(indices)

        while l <= r:
            mid = l + (r - l) // 2

            if indices[mid] >= target:
                res = mid
                r = mid - 1
            else:
                l = mid + 1

        return res

    def get_right_bound(self, indices, target):
        l = 0
        r = len(indices) - 1
        res = len(indices)

        while l <= r:
            mid = l + (r - l) // 2

            if indices[mid] > target:
                res = mid
                r = mid - 1
            else:
                l = mid + 1

        return res


# V0-2
# IDEA: PREFIX SUM + HASHMAP (TLE)
from collections import Counter
class RangeFreqQuery(object):

    def __init__(self, arr):
        """
        :type arr: List[int]
        """
        val_idx = Counter(arr)
        n = len(arr)

        self.prefix_map = {}

        # Prefix sum + hashmap
        for k in val_idx:
            prefix = [0] * (n + 1)

            for i in range(n):
                # prefix[i + 1] counts occurrences in arr[0:i+1]
                prefix[i + 1] = prefix[i]

                if arr[i] == k:
                    prefix[i + 1] += 1

            self.prefix_map[k] = prefix

    def query(self, left, right, value):
        """
        :type left: int
        :type right: int
        :type value: int
        :rtype: int
        """
        if value not in self.prefix_map:
            return 0

        prefix = self.prefix_map[value]

        # Inclusive range [left, right]
        return prefix[right + 1] - prefix[left]


# V0-3
# IDEA : HASHMAP (VALUE -> SORTED LIST OF ITS INDICES) + BINARY SEARCH (GEMINI)
from collections import defaultdict

class RangeFreqQuery(object):

  def __init__(self, arr):
    """:type arr: List[int]
    """
    # {value: sorted list of indices}
    self.val_idx = defaultdict(list)

    for i, val in enumerate(arr):
      self.val_idx[val].append(i)

  def query(self, left, right, value):
    """:type left: int
    :type right: int
    :type value: int
    :rtype: int
    """
    if value not in self.val_idx:
      return 0

    indices = self.val_idx[value]

    # 1. 尋找第一個大於或等於 left 的位置 (對應 bisect_left)
    l = self.get_left_bound(indices, left)

    # 2. 尋找第一個大於 right 的位置 (對應 bisect_right)
    r = self.get_right_bound(indices, right)

    return r - l

  def get_left_bound(self, indices, target):
    # 尋找第一個 indices[mid] >= target 的索引
    l, r = 0, len(indices) - 1
    res = len(indices)  # 預設值：若所有元素都小則回傳陣列長度

    while r >= l:
      mid = l + (r - l) // 2
      if indices[mid] >= target:
        res = mid  # 記錄當前符合條件的位置
        r = mid - 1  # 嘗試往左邊繼續找更早出現的合規位置
      else:
        l = mid + 1  # 數值小於 target，往右找

    return res

  def get_right_bound(self, indices, target):
    # 尋找第一個 indices[mid] > target 的索引
    l, r = 0, len(indices) - 1
    res = len(indices)  # 預設值：若所有元素都小於或等於 target 則回傳陣列長度

    while r >= l:
      mid = l + (r - l) // 2
      if indices[mid] > target:
        res = mid  # 記錄當前大於 target 的位置
        r = mid - 1  # 嘗試往左邊找更前面有沒有符合條件的
      else:
        l = mid + 1  # 數值小於或等於 target，往右找

    return res



# V0
# IDEA : HASHMAP (VALUE -> SORTED LIST OF ITS INDICES) + BINARY SEARCH
#
#   one pass in __init__ records, for every value, the indices it sits at.
#   indices are appended left to right, so each list is ALREADY sorted.
#
#   a query (left, right, value) then asks: how many of value's indices
#   fall inside [left, right]? on a sorted list that is
#
#       (# indices <= right) - (# indices < left)
#     = bisect_right(idx, right) - bisect_left(idx, left)
#
#   e.g. arr = [12,33,4,56,22,2,34,33,22,12,34,56], value = 33
#        idx of 33 = [1, 7]
#        query(0, 11, 33) -> bisect_right([1,7], 11) = 2
#                            bisect_left([1,7], 0)   = 0   -> 2
#
#   brute force (scan arr[left..right] per query) is O(n) per query,
#   O(n * q) = 10^10 overall -> TLE; this is why the index lists exist.
#
# time = O(n) build, O(log n) per query, space = O(n)
import bisect
from collections import defaultdict


class RangeFreqQuery(object):

    def __init__(self, arr):
        """
        :type arr: List[int]
        """
        # value -> sorted list of every index it appears at
        self.positions = defaultdict(list)
        for idx, val in enumerate(arr):
            self.positions[val].append(idx)

    def query(self, left, right, value):
        """
        :type left: int
        :type right: int
        :type value: int
        :rtype: int
        """
        # a value never seen in arr occurs 0 times in any range
        # (check with `in` so the defaultdict does not grow an empty list)
        if value not in self.positions:
            return 0

        idx_list = self.positions[value]

        # first position whose index is >= left
        start = bisect.bisect_left(idx_list, left)
        # first position whose index is > right
        end = bisect.bisect_right(idx_list, right)

        # NOTE !!! idx_list[start:end] are exactly the indices in [left, right]
        return end - start


# V0-1
# IDEA : SAME AS V0, BUT THE BINARY SEARCH IS WRITTEN BY HAND
#
#   interviewers often say "no bisect". both bounds are one lower_bound:
#
#       lower_bound(idx, x) = first position whose index >= x
#
#       # indices in [left, right]
#     = lower_bound(idx, right + 1) - lower_bound(idx, left)
#
#   the `right + 1` turns "<= right" into "< right + 1", so a single
#   helper covers both ends (indices are integers, so nothing is skipped).
#
# time = O(n) build, O(log n) per query, space = O(n)
class RangeFreqQuery2(object):

    def __init__(self, arr):
        """
        :type arr: List[int]
        """
        # value -> sorted list of every index it appears at
        self.positions = {}
        for idx, val in enumerate(arr):
            if val not in self.positions:
                self.positions[val] = []
            self.positions[val].append(idx)

    def _lower_bound(self, idx_list, target):
        """Return the first position in idx_list whose index is >= target."""
        # search space is [lo, hi); hi = len means "every index < target"
        lo = 0
        hi = len(idx_list)
        while lo < hi:
            mid = (lo + hi) // 2
            if idx_list[mid] < target:
                # mid and everything left of it are too small
                lo = mid + 1
            else:
                # mid could be the answer, keep it in range
                hi = mid
        return lo

    def query(self, left, right, value):
        """
        :type left: int
        :type right: int
        :type value: int
        :rtype: int
        """
        if value not in self.positions:
            return 0

        idx_list = self.positions[value]
        start = self._lower_bound(idx_list, left)
        # NOTE !!! right + 1, so indices equal to `right` are counted
        end = self._lower_bound(idx_list, right + 1)
        return end - start


# Your RangeFreqQuery object will be instantiated and called as such:
# obj = RangeFreqQuery(arr)
# param_1 = obj.query(left,right,value)
