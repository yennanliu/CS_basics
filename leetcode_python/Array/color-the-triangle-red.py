"""

2647. Color the Triangle Red
Hard

You are given an integer n. Consider an equilateral triangle of side length n, broken up into n^2 unit equilateral triangles. The triangle has n 1-indexed rows where the ith row has 2i - 1 unit equilateral triangles.

The triangles in the ith row are also 1-indexed with coordinates from (i, 1) to (i, 2i - 1).

Two triangles are neighbors if they share a side. For example:

Triangles (1,1) and (2,2) are neighbors
Triangles (3,2) and (3,3) are neighbors.
Triangles (2,2) and (3,3) are not neighbors because they do not share any side.

Initially, all the unit triangles are white. You want to choose k triangles and color them red. We will then run the following algorithm:

1. Choose a white triangle that has at least two red neighbors.
   - If there is no such triangle, stop the algorithm.
2. Color that triangle red.
3. Go to step 1.

Choose the minimum k possible and set k triangles red before running this algorithm such that after the algorithm stops, all unit triangles are colored red.

Return a 2D list of the coordinates of the triangles that you will color red initially. The answer has to be of the smallest size possible. If there are multiple valid solutions, return any.


Example 1:

Input: n = 3
Output: [[1,1],[2,1],[2,3],[3,1],[3,5]]
Explanation: Initially, we choose the shown 5 triangles to be red. Then, we run the algorithm:
- Choose (2,2) that has three red neighbors and color it red.
- Choose (3,2) that has two red neighbors and color it red.
- Choose (3,4) that has three red neighbors and color it red.
- Choose (3,3) that has three red neighbors and color it red.
It can be shown that choosing any 4 triangles and running the algorithm will not make all triangles red.

Example 2:

Input: n = 2
Output: [[1,1],[2,1],[2,3]]
Explanation: Initially, we choose the shown 3 triangles to be red. Then, we run the algorithm:
- Choose (2,2) that has three red neighbors and color it red.
It can be shown that choosing any 2 triangles and running the algorithm will not make all triangles red.


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : CONSTRUCTIVE / GREEDY PATTERN (walk the rows BOTTOM-UP in blocks of 4)
#
#   GEOMETRY.  in row i the odd-indexed cells (i, 1), (i, 3), ... point UP and
#   the even-indexed ones point DOWN. neighbours are (i, j) <-> (i, j + 1)
#   inside a row, and (i, j) <-> (i - 1, j - 1) for an even (downward) j.
#   so a downward cell is the only one with a link to the row above.
#
#   THE PATTERN.  the apex (1, 1) always has to be seeded (it has just one
#   neighbour, so it can never be inferred). apart from that, going from the
#   BOTTOM row n upwards, the cheapest seeding repeats with period 4 :
#
#     phase 0 : seed EVERY upward cell of the row      -> i    seeds
#               (the downward cells in between then have 2 red neighbours)
#     phase 1 : seed only (i, 2)                       -> 1    seed
#     phase 2 : seed the upward cells from j = 3 on    -> i-1  seeds
#     phase 3 : seed only (i, 1)                       -> 1    seed
#
#   each fully-red row feeds the row above it (and vice-versa), and the four
#   phases are arranged so every remaining white cell ends up with two red
#   neighbours once the cascade is run.
#
#   NOTE : the phase counter is driven by HOW MANY rows we already emitted,
#          not by the row index, so it must be advanced every iteration.
#
#   NOTE : n == 1 is the degenerate case -> the loop body never runs and the
#          answer is just the apex [[1, 1]].
#
# time = O(n^2) (that is the size of the output itself), space = O(1) extra
class Solution(object):
    def colorRed(self, n):
        ans = [[1, 1]]
        phase = 0
        for i in range(n, 1, -1):
            if phase == 0:
                for j in range(1, 2 * i, 2):
                    ans.append([i, j])
            elif phase == 1:
                ans.append([i, 2])
            elif phase == 2:
                for j in range(3, 2 * i, 2):
                    ans.append([i, j])
            else:
                ans.append([i, 1])
            phase = (phase + 1) % 4
        return ans
