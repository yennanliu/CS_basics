"""

427. Construct Quad Tree
Medium

Given a n * n matrix grid of 0's and 1's only. We want to represent grid with a Quad-Tree.

Return the root of the Quad-Tree representing grid.

A Quad-Tree is a tree data structure in which each internal node has exactly four children.
Besides, each node has two attributes:

- val: True if the node represents a grid of 1's or False if the node represents a grid of 0's.
  Notice that you can assign the val to True or False when isLeaf is False, and both are
  accepted in the answer.
- isLeaf: True if the node is a leaf node on the tree or False if the node has four children.

class Node {
    public boolean val;
    public boolean isLeaf;
    public Node topLeft;
    public Node topRight;
    public Node bottomLeft;
    public Node bottomRight;
}

We can construct a Quad-Tree from a two-dimensional area using the following steps:

1. If the current grid has the same value (i.e all 1's or all 0's) set isLeaf True and set val
   to the value of the grid and set the four children to Null and stop.
2. If the current grid has different values, set isLeaf to False and set val to any value
   and divide the current grid into four sub-grids as shown in the photo.
3. Recurse for each of the children with the proper sub-grid.

Quad-Tree format:

The output represents the serialized format of a Quad-Tree using level order traversal,
where null signifies a path terminator where no node exists below.

It is very similar to the serialization of the binary tree. The only difference is that the node
is represented as a list [isLeaf, val].

If the value of isLeaf or val is True we represent it as 1 in the list [isLeaf, val]
and if the value of isLeaf or val is False we represent it as 0.

Example 1:

Input: grid = [[0,1],[1,0]]
Output: [[0,1],[1,0],[1,1],[1,1],[1,0]]
Explanation: The explanation of this example is shown below:
Notice that 0 represents False and 1 represents True in the photo representing the Quad-Tree.

Example 2:

Input: grid = [[1,1,1,1,0,0,0,0],[1,1,1,1,0,0,0,0],[1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1],
               [1,1,1,1,0,0,0,0],[1,1,1,1,0,0,0,0],[1,1,1,1,0,0,0,0],[1,1,1,1,0,0,0,0]]
Output: [[0,1],[1,1],[0,1],[1,1],[1,0],null,null,null,null,[1,0],[1,0],[1,1],[1,1]]
Explanation: All values in the grid are not the same. We divide the grid into four sub-grids.
The topLeft, bottomLeft and bottomRight each has the same value.
The topRight have different values so we divide it into 4 sub-grids where each has the same value.

Constraints:

n == grid.length == grid[i].length
n == 2^x where 0 <= x <= 6

"""

# Definition for a QuadTree node.
# class Node(object):
#     def __init__(self, val, isLeaf, topLeft, topRight, bottomLeft, bottomRight):
#         self.val = val
#         self.isLeaf = isLeaf
#         self.topLeft = topLeft
#         self.topRight = topRight
#         self.bottomLeft = bottomLeft
#         self.bottomRight = bottomRight

# V0
# IDEA : RECURSION, TOP-DOWN (check the square is uniform -> leaf, else split into 4)
#
#   a square is described by its top-left corner (row, col) and its side length.
#   - if every cell in it equals the top-left cell -> it is ONE leaf with that value
#   - otherwise halve the side and recurse on the 4 quadrants, in the order
#     topLeft, topRight, bottomLeft, bottomRight
#
#   e.g. grid = [[0,1],[1,0]] -> not uniform -> split into 4 squares of side 1,
#        each a leaf: 0, 1, 1, 0  -> [[0,1],[1,0],[1,1],[1,1],[1,0]]
#
#   each level of recursion scans all n^2 cells once in total, and there are
#   log n levels -> O(n^2 log n). Good enough for n <= 64; the one to say first.
#
# time = O(n^2 log n), space = O(log n)
class Solution(object):
    def construct(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: Node
        """
        return self._build(grid, 0, 0, len(grid))

    def _build(self, grid, row, col, size):
        """Build the quad tree for the size x size square whose top-left is (row, col)."""
        if self._is_uniform(grid, row, col, size):
            # whole square is one value -> a leaf, no children
            return Node(grid[row][col] == 1, True, None, None, None, None)

        half = size // 2
        top_left = self._build(grid, row, col, half)
        top_right = self._build(grid, row, col + half, half)
        bottom_left = self._build(grid, row + half, col, half)
        bottom_right = self._build(grid, row + half, col + half, half)

        # NOTE !!! an internal node's val can be anything (LC accepts both); use True
        return Node(True, False, top_left, top_right, bottom_left, bottom_right)

    def _is_uniform(self, grid, row, col, size):
        """True when every cell of the square equals its top-left cell."""
        first = grid[row][col]
        for r in range(row, row + size):
            for c in range(col, col + size):
                if grid[r][c] != first:
                    return False
        return True


# V0-1
# IDEA : RECURSION, TOP-DOWN + 2D PREFIX SUM (O(1) uniform check)
#
#   same recursion as V0, but "is this square uniform?" is answered in O(1):
#   the number of 1s in the square is either 0 (all 0s) or size * size (all 1s).
#
#   prefix[r][c] = number of 1s in grid[0..r-1][0..c-1]   (1-indexed, row/col 0 = padding)
#   ones in square = prefix[r2][c2] - prefix[r1][c2] - prefix[r2][c1] + prefix[r1][c1]
#
#   every node of the quad tree is now O(1) work, and there are at most
#   O(n^2) nodes -> O(n^2) overall, paid for with an O(n^2) prefix table.
#
# time = O(n^2), space = O(n^2)
class Solution2(object):
    def construct(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: Node
        """
        n = len(grid)

        # prefix has one extra row/col of zeros so no bounds checks are needed
        prefix = [[0] * (n + 1) for _ in range(n + 1)]
        for r in range(n):
            for c in range(n):
                prefix[r + 1][c + 1] = (grid[r][c]
                                        + prefix[r][c + 1]
                                        + prefix[r + 1][c]
                                        - prefix[r][c])

        self.prefix = prefix
        return self._build(0, 0, n)

    def _count_ones(self, row, col, size):
        """Number of 1s in the size x size square whose top-left is (row, col)."""
        p = self.prefix
        r1, c1 = row, col
        r2, c2 = row + size, col + size
        return p[r2][c2] - p[r1][c2] - p[r2][c1] + p[r1][c1]

    def _build(self, row, col, size):
        """Build the quad tree for the square at (row, col) with side size."""
        ones = self._count_ones(row, col, size)

        if ones == 0:
            return Node(False, True, None, None, None, None)
        if ones == size * size:
            return Node(True, True, None, None, None, None)

        half = size // 2
        top_left = self._build(row, col, half)
        top_right = self._build(row, col + half, half)
        bottom_left = self._build(row + half, col, half)
        bottom_right = self._build(row + half, col + half, half)
        return Node(True, False, top_left, top_right, bottom_left, bottom_right)


# V1
# IDEA : RECURSION, BOTTOM-UP MERGE (build the 4 children first, then collapse)
#
#   instead of checking uniformity before splitting, always split down to
#   single cells (each a leaf), then on the way back up:
#   - if all 4 children are leaves with the SAME val -> the square is uniform,
#     so replace them with ONE leaf of that val
#   - otherwise keep them as the 4 children of an internal node
#
#   each cell is visited exactly once and each merge is O(1) -> O(n^2),
#   with no extra table. Slightly harder to explain than V0, but optimal.
#
# time = O(n^2), space = O(log n)
class Solution3(object):
    def construct(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: Node
        """
        return self._build(grid, 0, 0, len(grid))

    def _build(self, grid, row, col, size):
        """Build the quad tree for the square at (row, col) with side size."""
        # base case: a single cell is always a leaf
        if size == 1:
            return Node(grid[row][col] == 1, True, None, None, None, None)

        half = size // 2
        top_left = self._build(grid, row, col, half)
        top_right = self._build(grid, row, col + half, half)
        bottom_left = self._build(grid, row + half, col, half)
        bottom_right = self._build(grid, row + half, col + half, half)

        children = [top_left, top_right, bottom_left, bottom_right]

        # NOTE !!! merge only when ALL 4 are leaves AND share one value;
        #          4 leaves with mixed values must stay split
        all_leaves = True
        for child in children:
            if not child.isLeaf:
                all_leaves = False
                break

        if all_leaves:
            same_val = True
            for child in children:
                if child.val != top_left.val:
                    same_val = False
                    break
            if same_val:
                return Node(top_left.val, True, None, None, None, None)

        return Node(True, False, top_left, top_right, bottom_left, bottom_right)
