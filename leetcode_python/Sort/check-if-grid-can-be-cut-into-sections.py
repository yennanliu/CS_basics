"""

3394. Check if Grid can be Cut into Sections
Medium

You are given an integer n representing the dimensions of an n x n grid, with
the origin at the bottom-left corner of the grid. You are also given a 2D array
of coordinates rectangles, where rectangles[i] is in the form [start_x, start_y,
end_x, end_y], representing a rectangle on the grid. Each rectangle is defined
as follows:

(start_x, start_y): The bottom-left corner of the rectangle.
(end_x, end_y): The top-right corner of the rectangle.

Note that the rectangles do not overlap. Your task is to determine if it is
possible to make either two horizontal or two vertical cuts on the grid such
that:

Each of the three resulting sections formed by the cuts contains at least one
rectangle.
Every rectangle belongs to exactly one section.

Return true if such cuts can be made; otherwise, return false.

Example 1:

Input: n = 5, rectangles = [[1,0,5,2],[0,2,2,4],[3,2,5,3],[0,4,4,5]]

Output: true

Explanation:

The grid is shown in the diagram. We can make horizontal cuts at y = 2 and y =
4. Hence, output is true.

Example 2:

Input: n = 4, rectangles = [[0,0,1,1],[2,0,3,4],[0,2,2,3],[3,0,4,3]]

Output: true

Explanation:

We can make vertical cuts at x = 2 and x = 3. Hence, output is true.

Example 3:

Input: n = 4, rectangles = [[0,2,2,4],[1,0,3,2],[2,2,3,4],[3,0,4,2],[3,2,4,4]]

Output: false

Explanation:

We cannot make two horizontal or two vertical cuts that satisfy the conditions.
Hence, output is false.

Constraints:

3 <= n <= 10^9
3 <= rectangles.length <= 10^5
0 <= rectangles[i][0] < rectangles[i][2] <= n
0 <= rectangles[i][1] < rectangles[i][3] <= n
No two rectangles overlap.

"""

# V0
# IDEA : INTERVAL MERGING ON EACH AXIS INDEPENDENTLY
#
#   a horizontal cut is a line y = c that no rectangle straddles.  projecting
#   every rectangle onto the y axis turns each one into the interval
#   [start_y, end_y), and a legal cut is exactly a point where the union of
#   those intervals falls apart.  the rectangles are guaranteed not to overlap,
#   but their *projections* may, which is why merging is needed at all.
#
#   after merging, the number of connected blobs on an axis is the number of
#   pieces the grid can be split into along that axis.  three or more blobs
#   means we can put the two cuts in two of the gaps and every piece keeps at
#   least one rectangle — fewer than three and it is impossible.
#
#   the two axes are independent, so we just test both.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def checkValidCuts(self, n, rectangles):
        def sections(spans):
            spans.sort()
            cnt = 0
            end = -1
            for lo, hi in spans:
                if lo >= end:
                    cnt += 1
                    end = hi
                else:
                    end = max(end, hi)
            return cnt

        xs = [(r[0], r[2]) for r in rectangles]
        ys = [(r[1], r[3]) for r in rectangles]
        return sections(xs) >= 3 or sections(ys) >= 3
