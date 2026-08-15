"""

3047. Find the Largest Area of Square Inside Two Rectangles
Medium

There exist n rectangles in a 2D plane with edges parallel to the x and y axis. You are given two 2D integer arrays bottomLeft and topRight where bottomLeft[i] = [a_i, b_i] and topRight[i] = [c_i, d_i] represent the bottom-left and top-right coordinates of the ith rectangle, respectively.

You need to find the maximum area of a square that can fit inside the intersecting region of at least two rectangles.

Return the maximum area of such a square, or 0 if such a square does not exist.


Example 1:

Input: bottomLeft = [[1,1],[2,2],[3,1]], topRight = [[3,3],[4,4],[6,6]]
Output: 1
Explanation: A square with side length 1 can fit inside either the intersecting region of rectangle 0 and rectangle 1, or the intersecting region of rectangle 1 and rectangle 2.
Hence the maximum area is 1. It can be shown that there is no square of side length larger than 1 that can fit inside any intersecting region.

Example 2:

Input: bottomLeft = [[1,1],[2,2],[1,2]], topRight = [[3,3],[4,4],[3,4]]
Output: 1
Explanation: A square with side length 1 can fit inside either the intersecting region of rectangle 0 and rectangle 1, the intersecting region of rectangle 1 and rectangle 2, or the intersection region of all 3 rectangles.
Hence the maximum area is 1.

Example 3:

Input: bottomLeft = [[1,1],[3,3],[3,1]], topRight = [[2,2],[4,4],[4,2]]
Output: 0
Explanation: No pair of rectangles intersect, hence, we return 0.


Constraints:

n == bottomLeft.length == topRight.length
2 <= n <= 10^3
bottomLeft[i].length == topRight[i].length == 2
1 <= bottomLeft[i][0], bottomLeft[i][1] <= 10^7
1 <= topRight[i][0], topRight[i][1] <= 10^7
bottomLeft[i][0] < topRight[i][0]
bottomLeft[i][1] < topRight[i][1]

"""

# V0
# IDEA : THE BEST SQUARE ALWAYS LIVES IN SOME *PAIRWISE* INTERSECTION
#
#   an intersection of three or more rectangles is contained in the
#   intersection of any two of them, so it can never beat the best pair. that
#   reduces the search to the n^2 / 2 pairs — 5 * 10^5 of them at n = 1000.
#
#   two axis-aligned rectangles intersect in another axis-aligned rectangle :
#       x-range [max(left), min(right)]   y-range [max(bottom), min(top)]
#   and the largest square inside it has side min(width, height), which is
#   0 or negative when they miss each other.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def largestSquareArea(self, bottomLeft, topRight):
        n = len(bottomLeft)
        best = 0
        for i in range(n):
            x1, y1 = bottomLeft[i]
            x2, y2 = topRight[i]
            for j in range(i + 1, n):
                a1, b1 = bottomLeft[j]
                a2, b2 = topRight[j]
                w = min(x2, a2) - max(x1, a1)
                h = min(y2, b2) - max(y1, b1)
                side = min(w, h)
                if side > best:
                    best = side
        return best * best
