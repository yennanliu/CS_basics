"""

4084. Last Visited Cell in a Directional Grid
Medium

You are given an m x n matrix grid where each cell contains one of the
following characters:

'L': move one cell left.
'R': move one cell right.
'U': move one cell up.
'D': move one cell down.

You start at cell (0, 0), which is considered visited. At each step, move
to the cell indicated by the character in your current cell.

The process ends if the indicated move would either leave the grid or
enter a cell that has already been visited. In either case, the move is
not made.

Return an integer array [r, c] representing the row and column indices of
the last visited cell. The indices are 0-based.


Example 1:

Input: grid = [["R","D"],["U","L"]]

Output: [1,0]

Explanation:

Start at cell (0, 0), which contains 'R', so move right to (0, 1).
At cell (0, 1), the character is 'D', so move down to (1, 1).
At cell (1, 1), the character is 'L', so move left to (1, 0).
At cell (1, 0), the character is 'U', which indicates moving to (0, 0).
However, (0, 0) has already been visited, so this move is not made.
Therefore, the process ends at the last visited cell (1, 0), so the
answer is [1, 0].

Example 2:

Input: grid = [["R","R","R"]]

Output: [0,2]

Explanation:

Start at cell (0, 0), which contains 'R', so move right to (0, 1).
At cell (0, 1), the character is 'R', so move right to (0, 2).
At cell (0, 2), the character is 'R', which indicates moving to (0, 3).
However, (0, 3) is outside the grid, so this move is not made.
Therefore, the process ends at the last visited cell (0, 2), so the
answer is [0, 2].


Constraints:

1 <= m == grid.length <= 10^5

1 <= n == grid[i].length <= 10^5

m * n <= 10^5

grid[i][j] is one of 'L', 'R', 'U', or 'D'.

"""

# V0
# IDEA : SIMULATION + VISITED SET
#
#   follow the arrows from (0, 0) one cell at a time; the CURRENT cell is
#   always the last visited one, so the answer is wherever we stand when the
#   next move is refused (out of the grid, or into a visited cell).
#
#   every step visits a NEW cell, so the walk is at most m * n steps long.
#
#   e.g. [["R","D"],["U","L"]] : (0,0) -> (0,1) -> (1,1) -> (1,0) -> 'U' hits
#        (0,0), already visited -> stop at [1, 0]
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def lastVisitedCell(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: List[int]
        """
        # edge case: empty grid has no cell to stand on
        if not grid or not grid[0]:
            return []

        rows = len(grid)
        cols = len(grid[0])

        # direction -> (row delta, col delta)
        # NOTE !!! rows grow DOWNWARD: 'U' is row - 1, 'D' is row + 1,
        #          and 'L' / 'R' change the COLUMN, not the row
        moves = {
            "U": (-1, 0),
            "D": (1, 0),
            "L": (0, -1),
            "R": (0, 1),
        }

        r, c = 0, 0
        visited = set()
        visited.add((r, c))

        while True:
            dr, dc = moves[grid[r][c]]
            next_r = r + dr
            next_c = c + dc

            # the move would leave the grid -> not made, stop here
            if next_r < 0 or next_r >= rows or next_c < 0 or next_c >= cols:
                return [r, c]

            # the move would revisit a cell -> not made, stop here
            if (next_r, next_c) in visited:
                return [r, c]

            r, c = next_r, next_c
            visited.add((r, c))
