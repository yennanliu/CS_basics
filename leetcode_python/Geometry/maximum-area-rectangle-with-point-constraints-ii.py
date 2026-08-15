"""

3382. Maximum Area Rectangle With Point Constraints II
Hard

There are n points on an infinite plane. You are given two integer arrays xCoord and yCoord where (xCoord[i], yCoord[i]) represents the coordinates of the ith point.

Your task is to find the maximum area of a rectangle that:

Can be formed using four of these points as its corners.
Does not contain any other point inside or on its border.
Has its edges parallel to the axes.

Return the maximum area that you can obtain or -1 if no such rectangle is possible.


Example 1:

Input: xCoord = [1,1,3,3], yCoord = [1,3,1,3]
Output: 4
Explanation:
We can make a rectangle with these 4 points as corners and there is no other point that lies inside or on the border. Hence, the maximum possible area would be 4.

Example 2:

Input: xCoord = [1,1,3,3,2], yCoord = [1,3,1,3,2]
Output: -1
Explanation:
There is only one rectangle possible is with points [1,1], [1,3], [3,1] and [3,3] but [2,2] will always lie inside it. Hence, returning -1.

Example 3:

Input: xCoord = [1,1,3,3,1,3], yCoord = [1,3,1,3,2,2]
Output: 2
Explanation:
The maximum area rectangle is formed by the points [1,3], [1,2], [3,2], [3,3], which has an area of 2. Additionally, the points [1,1], [1,2], [3,1], [3,2] also form a valid rectangle with the same area.


Constraints:

1 <= xCoord.length == yCoord.length <= 2 * 10^5
0 <= xCoord[i], yCoord[i] <= 8 * 10^7
All the given points are unique.

"""

# V0
# IDEA : SWEEP BY x, PAIRING *VERTICALLY ADJACENT* POINTS, COUNTING WITH A BIT
#
#   a valid rectangle's left edge holds two points with NOTHING between them
#   on that edge — otherwise the extra point sits on the border. so on each
#   vertical line only consecutive y's can be a rectangle's left (or right)
#   edge, which brings the candidate pairs down from quadratic to O(n).
#
#   sweeping x from small to large, a pair (y1, y2) that was last seen at
#   column x0 closes a rectangle now. it is empty exactly when no other point
#   entered the strip meanwhile, which a Fenwick tree over y answers by
#   comparing the counts in [y1, y2] then and now : the difference must be
#   exactly the 2 points just added on this column.
#
# time = O(n log n), space = O(n)
from collections import defaultdict


class Solution(object):
    def maxRectangleArea(self, xCoord, yCoord):
        pts = sorted(zip(xCoord, yCoord))
        ys = sorted(set(yCoord))
        rank = {y: i + 1 for i, y in enumerate(ys)}
        size = len(ys)

        tree = [0] * (size + 1)

        def add(i):
            while i <= size:
                tree[i] += 1
                i += i & -i

        def prefix(i):
            s = 0
            while i > 0:
                s += tree[i]
                i -= i & -i
            return s

        def count(y1, y2):                      # points with y in [y1, y2]
            return prefix(rank[y2]) - prefix(rank[y1] - 1)

        by_x = defaultdict(list)
        for x, y in pts:
            by_x[x].append(y)

        last = {}                               # (y1, y2) -> (x, count then)
        best = -1
        for x in sorted(by_x):
            column = by_x[x]                    # already ascending
            for y in column:
                add(rank[y])
            for a in range(len(column) - 1):
                y1, y2 = column[a], column[a + 1]
                cur = count(y1, y2)
                prev = last.get((y1, y2))
                if prev is not None:
                    x0, cnt0 = prev
                    if cur - cnt0 == 2:         # nothing entered the strip
                        area = (x - x0) * (y2 - y1)
                        if area > best:
                            best = area
                last[(y1, y2)] = (x, cur)
        return best
