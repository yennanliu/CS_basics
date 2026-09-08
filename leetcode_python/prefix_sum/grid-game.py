"""

2017. Grid Game
Medium

You are given a 0-indexed 2D array grid of size 2 x n, where grid[r][c]
represents the number of points at position (r, c) on the matrix.

Two robots are playing a game on this matrix.

Both robots initially start at (0, 0) and want to reach (1, n-1). Each robot
may only move to the right ((r, c) -> (r, c + 1)) or down ((r, c) -> (r + 1, c)).

At the start of the game, the first robot moves from (0, 0) to (1, n-1),
collecting all the points from the cells on its path. For all cells (r, c)
traversed on the path, grid[r][c] is set to 0. Then, the second robot moves
from (0, 0) to (1, n-1), collecting the points on its path. Note that their
paths may intersect with one another.

The first robot wants to minimize the number of points collected by the second
robot. In contrast, the second robot wants to maximize the number of points
collected. If both robots play optimally, return the number of points collected
by the second robot.

Example 1:

Input: grid = [[2,5,4],[1,5,1]]

Output: 4

Explanation:

the first robot turns down at column 1, so it takes 2 + 5 + 5 + 1 and zeroes
those cells. what is left for the second robot is 4 on the top row and 1 on the
bottom row, and one path cannot pick up both -> it takes 4.

Example 2:

Input: grid = [[3,3,1],[8,5,2]]

Output: 4

Explanation:

the first robot turns down at column 0 (3 + 8 + 5 + 2), leaving 3 + 1 on the
top row for the second robot.

Example 3:

Input: grid = [[1,3,1,15],[1,3,3,1]]

Output: 7

Explanation:

the first robot turns down at the last column, leaving 1 + 3 + 3 on the bottom
row for the second robot.

Constraints:

grid.length == 2
n == grid[r].length
1 <= n <= 5 * 10^4
1 <= grid[r][c] <= 10^5

"""

"""
NOTE !!! Dijkstra is NOT working for this LC


**不適用，Dijkstra / 最長路徑演算法無法解決 LC 2017。**

這想法無法成立的主要原因有兩個：

---

### 1. 為什麼 Dijkstra 會失敗？

1. **目標函數不符合（博弈論 Minimax vs. 最優路徑）**：
第一個機器人的目標**不是**讓自己的分數最大化，而是**讓第二個機器人能拿到的最高分數最小化**（典型的 Minimax 博弈問題）。如果機器人 1 貪心地拿走自己能得最高分的路線，可能會把剩餘地圖中分數更高的區域完整留給機器人 2。
2. **網格幾何限制（$M = 2$ 的特性）**：
因為網格只有 2 行，機器人 1 唯一的決策點就是**在哪一個列 $c$ 從第 0 行向下轉向第 1 行**。這意味著機器人 1 只有 $N$ 種固定的轉向選擇，完全不需要使用圖搜尋演算法。


"""



# V0
# IDEA: PREFIX (BOTTOM) + SUFFIX (TOP) SUM OVER THE TURNING COLUMN
#
#   a path in a 2 x n grid goes right along row 0, drops ONCE, then goes right
#   along row 1 -- so a path IS just its turning column i.
#
#   robot 1 turning at i zeroes row0[0..i] and row1[i..n-1]. that leaves robot 2
#   exactly two untouched blocks, and they sit on OPPOSITE sides of i :
#
#     top    row0[i+1 ... n-1]     (right of the drop)
#     bottom row1[0   ... i-1]     (left  of the drop)
#
#   robot 2 also drops once, so it can only ever reach ONE of the two blocks --
#   and since every point is >= 1, it takes that block whole (drop at n-1 for
#   the top, at 0 for the bottom). so its best is max(top, bottom), and the
#   answer is the i that minimises that max.
#
#   e.g. grid = [[2,5,4],[1,5,1]], i = 1 -> top = 4, bottom = 1 -> 4  (the min)
#
# time = O(n), space = O(1)
class Solution(object):
    def gridGame(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        # edge
        if not grid or not grid[0]:
            return 0

        top = sum(grid[0])       # will shrink to row0[i+1 ... n-1]
        bottom = 0               # will grow  to row1[0   ... i-1]
        res = float('inf')

        for i in range(len(grid[0])):
            # NOTE !!! column i is on robot 1's own path, so it belongs to
            #          NEITHER block : drop it from top BEFORE the compare,
            #          and add it to bottom only AFTER
            top -= grid[0][i]
            res = min(res, max(top, bottom))
            bottom += grid[1][i]

        return res


# V0-1
# IDEA: PREFIX (BOTTOM) + SUFFIX (TOP) SUM OVER THE TURNING COLUMN (gemini)
"""
CORE IDEA:


### 2. 正確的數學推導（前綴和 / 後綴和）

當機器人 1 選擇在第 $c$ 列往下轉向時：

1. 第一行的 $[0 \dots c]$ 和第二行的 $[c \dots N-1]$ 都會被清成 `0`。
2. 此時機器人 2 只剩下**兩種可獲得非 0 分數的最佳路線**：
* **選項 A（第一行後綴和）**：走第一行直到最後一格 $\rightarrow$ 獲得 $\text{grid}[0][c+1 \dots N-1]$。
* **選項 B（第二行前綴和）**：第一格立刻往下走第二行 $\rightarrow$ 獲得 $\text{grid}[1][0 \dots c-1]$。



機器人 2 會貪心選擇這兩者中的較大值 $\max(\text{top\_suffix}, \text{bottom\_prefix})$。
機器人 1 的目標則是選擇一個轉向點 $c$，使機器人 2 的最終得分最小：

$$\min_{0 \le c < N} \left( \max\left( \sum_{j=c+1}^{N-1} \text{grid}[0][j], \, \sum_{j=0}^{c-1} \text{grid}[1][j] \right) \right)$$


"""
class Solution(object):
    def gridGame(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        N = len(grid[0])

        # 初始狀態：機器人 1 若在第 0 列就往下，第一行剩餘的總和（後綴和）
        top_sum = sum(grid[0])
        bottom_sum = 0

        res = float('inf')

        for c in range(N):
            # 機器人 1 經過 grid[0][c]，第一行剩餘分數減少
            top_sum -= grid[0][c]

            # 若機器人 1 在第 c 列轉向，機器人 2 能拿到的最高分數
            robot2_score = max(top_sum, bottom_sum)

            # 機器人 1 最小化機器人 2 的最大得分
            res = min(res, robot2_score)

            # 將 grid[1][c] 累加至第二行前綴和，供下一個轉向點 (c + 1) 使用
            bottom_sum += grid[1][c]

        return res
