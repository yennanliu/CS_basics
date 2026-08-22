"""

3000. Maximum Area of Longest Diagonal Rectangle
Easy

You are given a 2D 0-indexed integer array dimensions.

For all indices i, 0 <= i < dimensions.length, dimensions[i][0] represents the length and dimensions[i][1] represents the width of the rectangle i.

Return the area of the rectangle having the longest diagonal. If there are multiple rectangles with the longest diagonal, return the area of the rectangle having the maximum area.


Example 1:

Input: dimensions = [[9,3],[8,6]]
Output: 48
Explanation:
For index = 0, length = 9 and width = 3. Diagonal length = sqrt(9 * 9 + 3 * 3) = sqrt(90) = 9.487.
For index = 1, length = 8 and width = 6. Diagonal length = sqrt(8 * 8 + 6 * 6) = sqrt(100) = 10.
So, the rectangle at index 1 has a greater diagonal length therefore we return area = 8 * 6 = 48.

Example 2:

Input: dimensions = [[3,4],[4,3]]
Output: 12
Explanation: Length of diagonal is the same for both which is 5, so maximum area = 12.


Constraints:

1 <= dimensions.length <= 100
dimensions[i].length == 2
1 <= dimensions[i][0], dimensions[i][1] <= 100

"""

# V0
# IDEA : GREEDY ONE PASS ON (DIAGONAL^2, AREA)
#
#   compare rectangles by the tuple (l*l + w*w, l*w) and keep the best.
#
#   NOTE : compare the SQUARED diagonal, never sqrt(...) - the squared form
#          keeps everything in exact integer arithmetic, so two rectangles
#          with an equal diagonal really do compare equal (a float sqrt
#          could break the tie the wrong way).
#
# time = O(n), space = O(1)
class Solution(object):
    def areaOfMaxDiagonal(self, dimensions):
        best_diag = 0
        best_area = 0
        for l, w in dimensions:
            diag = l * l + w * w
            area = l * w
            if diag > best_diag or (diag == best_diag and area > best_area):
                best_diag = diag
                best_area = area
        return best_area


# V0-1
# IDEA : SORT ON THE KEY (DIAGONAL^2, AREA) AND READ THE LAST ENTRY
#
#   the tie-break rule of the problem *is* the lexicographic order of the
#   tuple (l*l + w*w, l*w), so building those tuples and sorting them makes
#   the hand-written comparison branch disappear : the winner is simply the
#   largest tuple, and its second component is the answer.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def areaOfMaxDiagonal(self, dimensions):
        ranked = sorted((l * l + w * w, l * w) for l, w in dimensions)
        return ranked[-1][1]


# V0-2
# IDEA : TWO PASS - LONGEST DIAGONAL FIRST, THEN BEST AREA AMONG THE TIES
#
#   split the two objectives instead of comparing them together : pass 1
#   maximises the squared diagonal only, pass 2 maximises the area over the
#   rectangles that attain it. neither pass needs a tie-break, and the same
#   shape answers "list every rectangle with the longest diagonal", which the
#   single-pass form of V0 cannot do without extra bookkeeping.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def areaOfMaxDiagonal(self, dimensions):
        top = max(l * l + w * w for l, w in dimensions)
        return max(l * w for l, w in dimensions if l * l + w * w == top)
