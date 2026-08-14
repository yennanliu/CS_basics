"""

1465. Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
Medium

You are given a rectangular cake of size h x w and two arrays of integers horizontalCuts and verticalCuts where:

horizontalCuts[i] is the distance from the top of the rectangular cake to the ith horizontal cut and similarly, and
verticalCuts[j] is the distance from the left of the rectangular cake to the jth vertical cut.

Return the maximum area of a piece of cake after you cut at each horizontal and vertical position provided in the arrays horizontalCuts and verticalCuts. Since the answer can be a large number, return this modulo 10^9 + 7.


Example 1:

Input: h = 5, w = 4, horizontalCuts = [1,2,4], verticalCuts = [1,3]
Output: 4
Explanation: The figure above represents the given rectangular cake. Red lines are the horizontal and vertical cuts. After you cut the cake, the green piece of cake has the maximum area.

Example 2:

Input: h = 5, w = 4, horizontalCuts = [3,1], verticalCuts = [1]
Output: 6
Explanation: The figure above represents the given rectangular cake. Red lines are the horizontal and vertical cuts. After you cut the cake, the green and yellow pieces of cake have the maximum area.

Example 3:

Input: h = 5, w = 4, horizontalCuts = [3], verticalCuts = [3]
Output: 9


Constraints:

2 <= h, w <= 10^9
1 <= horizontalCuts.length <= min(h - 1, 10^5)
1 <= verticalCuts.length <= min(w - 1, 10^5)
1 <= horizontalCuts[i] < h
1 <= verticalCuts[i] < w
All the elements in horizontalCuts are distinct.
All the elements in verticalCuts are distinct.

"""

# V0
# IDEA : SORT + LARGEST GAP (height and width are independent)
#
#   the grid of cuts makes every piece a (gap in y) x (gap in x) rectangle,
#   and the two dimensions can be maximised separately, so the answer is
#   (largest horizontal gap) * (largest vertical gap).
#   NOTE : add the borders 0 and h (resp. 0 and w) before scanning, else the
#          first and last slices are missed.
#   NOTE : take the modulo only at the very end - maximising a value mod p
#          on the way would compare the wrong numbers.
#
# time = O(m log m + n log n), space = O(m + n)
class Solution(object):
    def maxArea(self, h, w, horizontalCuts, verticalCuts):
        MOD = 10 ** 9 + 7

        def widest(cuts, size):
            pts = sorted(cuts + [0, size])
            best = 0
            for i in range(1, len(pts)):
                best = max(best, pts[i] - pts[i - 1])
            return best

        return (widest(horizontalCuts, h) * widest(verticalCuts, w)) % MOD
