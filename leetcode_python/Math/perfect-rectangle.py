"""

391. Perfect Rectangle
Hard

Given an array rectangles where rectangles[i] = [xi, yi, ai, bi] represents an
axis-aligned rectangle. The bottom-left point of the rectangle is (xi, yi) and
the top-right point of it is (ai, bi).

Return true if all the rectangles together form an exact cover of a rectangular region.

Example 1:

Input: rectangles = [[1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]]
Output: true
Explanation: All 5 rectangles together form an exact cover of a rectangular region.

Example 2:

Input: rectangles = [[1,1,2,3],[1,3,2,4],[3,1,4,2],[3,2,4,4]]
Output: false
Explanation: Because there is a gap between the two rectangular regions.

Example 3:

Input: rectangles = [[1,1,3,3],[3,1,4,2],[1,3,2,4],[2,2,4,4]]
Output: false
Explanation: Because two of the rectangles overlap with each other.

Constraints:

1 <= rectangles.length <= 2 * 10^4
rectangles[i].length == 4
-10^5 <= xi < ai <= 10^5
-10^5 <= yi < bi <= 10^5

"""

# V0
# IDEA : AREA + CORNER COUNTING
#
#  A set of rectangles is a perfect cover iff BOTH hold:
#
#   1) sum of the small areas == area of the bounding box
#      (no gap, and no overlap can "hide" since overlap would make the sum bigger)
#
#   2) corner parity:
#      - the 4 corners of the bounding box appear EXACTLY once
#      - every other corner point appears an EVEN number of times (2 or 4)
#
#  (1) alone is not enough: an overlap + an equal-sized gap keeps the area right,
#  (2) alone is not enough either, so we need both.
#
# time = O(n)
# space = O(n)
from collections import defaultdict
class Solution(object):
    def isRectangleCover(self, rectangles):
        area = 0
        min_x, min_y = rectangles[0][0], rectangles[0][1]
        max_x, max_y = rectangles[0][2], rectangles[0][3]
        cnt = defaultdict(int)

        for x1, y1, x2, y2 in rectangles:
            area += (x2 - x1) * (y2 - y1)

            min_x = min(min_x, x1)
            min_y = min(min_y, y1)
            max_x = max(max_x, x2)
            max_y = max(max_y, y2)

            # count the 4 corners of this small rectangle
            for p in ((x1, y1), (x1, y2), (x2, y1), (x2, y2)):
                cnt[p] += 1

        # check 1 : total area must match the bounding box
        if area != (max_x - min_x) * (max_y - min_y):
            return False

        # check 2 : the 4 outer corners must appear exactly once
        corners = [(min_x, min_y), (min_x, max_y), (max_x, min_y), (max_x, max_y)]
        for c in corners:
            if cnt[c] != 1:
                return False
        for c in corners:
            del cnt[c]

        # every remaining point must be shared by an even number of rectangles
        return all(v == 2 or v == 4 for v in cnt.values())
