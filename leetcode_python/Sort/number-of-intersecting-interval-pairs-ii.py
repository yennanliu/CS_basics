"""

4057. Number of Intersecting Interval Pairs II
Medium

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

2 <= n == intervals.length <= 10^5
intervals[i] = [starti, endi]
0 <= starti <= endi <= 10^9

"""

# V0
# IDEA : SCAN LINE — EACH NEW START MEETS EVERY INTERVAL STILL OPEN
#
#   n = 10^5, so the double loop of LC 4056 is out. turn every interval into two
#   events, (start, +1) and (end, -1), and sweep left -> right keeping `active`
#   = how many intervals are open right now.
#
#   two intervals intersect iff, when the LATER-starting one opens, the other
#   is still open. so every pair is counted exactly once, at the moment its
#   second interval starts :  res += active, then active += 1.
#
#   NOT max(C(active, 2)) over the sweep — that is only the largest group
#   overlapping at a single point, e.g. [[1,2],[2,3],[3,4]] peaks at 2 open
#   (C = 1) but has 2 pairs.
#
# NOTE !!! at the same coordinate, starts must be processed BEFORE ends —
#          closed intervals touching at an endpoint intersect, so [1,2] must
#          still be open when [2,3] starts. sort key (pos, -delta) does it.
#
#   e.g. [[1,5],[2,4],[3,6]] : start 1 (+0) start 2 (+1) start 3 (+2) -> 3
#
# time = O(n log n), space = O(n)
class Solution(object):
    def countIntersectingIntervals(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: int
        """
        # edge
        if not intervals or len(intervals) <= 1:
            return 0

        events = []
        for s, e in intervals:
            events.append((s, 1))
            events.append((e, -1))

        # same pos : start (+1) before end (-1)
        events.sort(key=lambda x: (x[0], -x[1]))

        res = 0
        active = 0

        for _pos, delta in events:
            if delta == 1:
                # the new interval meets every one still open
                res += active
                active += 1
            else:
                active -= 1

        return res


# V0-1
# IDEA: SCAN LINE (gpt)
class Solution(object):
    def countIntersectingIntervals(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: int
        """

        if not intervals or len(intervals) < 2:
            return 0

        events = []

        for start, end in intervals:
            events.append((start, 1))   # start
            events.append((end, -1))    # end

        # IMPORTANT:
        # If start == end, start must be processed BEFORE end
        # because intervals are inclusive.
        events.sort(key=lambda x: (x[0], -x[1]))

        active = 0
        res = 0

        for time, status in events:

            if status == 1:
                # Current interval intersects with
                # all currently active intervals.
                res += active

                active += 1

            else:
                active -= 1

        return res


# V0-2
# IDEA: 排序 + 樹狀陣列（Binary Indexed Tree / Fenwick Tree）(gemini)
class BIT:
  """ 樹狀陣列（Binary Indexed Tree）支援動態頻率統計與前綴和查詢 """

  def __init__(self, size):
    self.size = size
    self.tree = [0] * (size + 1)

  def update(self, i, delta):
    while i <= self.size:
      self.tree[i] += delta
      i += i & (-i)

  def query(self, i):
    s = 0
    while i > 0:
      s += self.tree[i]
      i -= i & (-i)
    return s


class Solution(object):

  def countIntersectingIntervals(self, intervals):
    """
        :type intervals: List[List[int]]
        :rtype: int
        """
    if not intervals or len(intervals) <= 1:
      return 0

    # 1. 依照區間起點由小到大排序
    intervals.sort(key=lambda x: (x[0], x[1]))

    # 2. 座標離散化（Coordinate Compression），將大數值對應到樹狀陣列的索引
    coords = set()
    for s, e in intervals:
      coords.add(s)
      coords.add(e)
    sorted_coords = sorted(list(coords))
    coord_map = {val: idx + 1 for idx, val in enumerate(sorted_coords)}

    n_coords = len(sorted_coords)
    bit = BIT(n_coords)
    total_pairs = 0
    active_count = 0

    # 3. 掃描與查詢相交對數
    for s, e in intervals:
      # 查詢先前已開始的區間中，其結束時間小於當前起點 s 的數量
      less_than_s = bit.query(coord_map[s] - 1)

      # 與當前區間相交的先前區間數 = 總已開始區間數 - 結束時間小於 s 的區間數
      intersect_count = active_count - less_than_s
      total_pairs += intersect_count

      # 將當前區間的結束時間加入樹狀陣列，並遞增已開始區間計數
      bit.update(coord_map[e], 1)
      active_count += 1

    return total_pairs
