"""

3363. Find the Maximum Number of Fruits Collected
Hard

There is a game dungeon comprised of n x n rooms arranged in a grid.

You are given a 2D array fruits of size n x n, where fruits[i][j] represents the number of fruits in the room (i, j). Three children will play in the game dungeon, with initial positions at corner rooms (0, 0), (0, n - 1), and (n - 1, 0).

The children will make exactly n - 1 moves according to the following rules to reach the room (n - 1, n - 1):

The child starting from (0, 0) must move from their current room (i, j) to one of the rooms (i + 1, j + 1), (i + 1, j), and (i, j + 1) if the target room exists.
The child starting from (0, n - 1) must move from their current room (i, j) to one of the rooms (i + 1, j - 1), (i + 1, j), and (i + 1, j + 1) if the target room exists.
The child starting from (n - 1, 0) must move from their current room (i, j) to one of the rooms (i - 1, j + 1), (i, j + 1), and (i + 1, j + 1) if the target room exists.

When a child enters a room, they will collect all the fruits there. If two or more children enter the same room, only one child will collect the fruits, and the room will be emptied after they leave.

Return the maximum number of fruits the children can collect from the dungeon.


Example 1:

Input: fruits = [[1,2,3,4],[5,6,8,7],[9,10,11,12],[13,14,15,16]]
Output: 100
Explanation:
In this example:
The 1st child (from the top left corner) moves on this path (0,0) -> (1,1) -> (2,2) -> (3,3).
The 2nd child (from the top right corner) moves on this path (0,3) -> (1,2) -> (2,3) -> (3,3).
The 3rd child (from the bottom left corner) moves on this path (3,0) -> (3,1) -> (3,2) -> (3,3).
In total they collect 1 + 6 + 11 + 16 + 4 + 8 + 12 + 13 + 14 + 15 = 100 fruits.

Example 2:

Input: fruits = [[1,1],[1,1]]
Output: 4
Explanation:
In this example:
The 1st child moves on the path (0,0) -> (1,1).
The 2nd child moves on the path (0,1) -> (1,1).
The 3rd child moves on the path (1,0) -> (1,1).
In total they collect 1 + 1 + 1 + 1 = 4 fruits.


Constraints:

2 <= n == fruits.length == fruits[i].length <= 1000
0 <= fruits[i][j] <= 1000

"""

# V0
# IDEA : THE DIAGONAL CHILD IS FORCED; THE OTHER TWO ARE MIRRORED DPs
#
#   the child from (0, 0) has to make n - 1 moves and end at (n-1, n-1), and
#   only the (i+1, j+1) step advances both coordinates — so its path IS the
#   main diagonal, contributing a fixed sum.
#
#   the child from (0, n-1) always increases its row, so it stays strictly
#   above the diagonal until the final cell; symmetrically the one from
#   (n-1, 0) stays strictly below. they never meet except at the end, so
#   their two DPs are independent and neither may touch the diagonal.
#
#       dp[i][j] = best fruits reaching (i, j) from its own corner
#
#   the second child's DP is exactly the first's on the transposed grid, so
#   one routine serves both.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def maxCollectedFruits(self, fruits):
        n = len(fruits)
        NEG = float('-inf')

        total = sum(fruits[i][i] for i in range(n))

        def upper(grid):
            """child from (0, n-1) moving down, staying strictly right of the diagonal

            the final hop lands on (n-1, n-1), which the diagonal child already
            collected, so the walk is only tracked through row n-2.
            """
            dp = [[NEG] * n for _ in range(n)]
            dp[0][n - 1] = grid[0][n - 1]
            for i in range(1, n - 1):
                lo = max(i + 1, n - 1 - i)      # stay right of the diagonal, and reachable
                for j in range(lo, n):
                    best = NEG
                    for pj in (j - 1, j, j + 1):
                        if 0 <= pj < n and dp[i - 1][pj] > best:
                            best = dp[i - 1][pj]
                    if best != NEG:
                        dp[i][j] = best + grid[i][j]
            return dp[n - 2][n - 1]

        # the third child is the mirror image across the diagonal
        transposed = [[fruits[j][i] for j in range(n)] for i in range(n)]
        return total + upper(fruits) + upper(transposed)
