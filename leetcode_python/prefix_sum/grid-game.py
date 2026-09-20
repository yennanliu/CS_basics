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


"""
NOTE !!!



the robot can ONLY change direction once

->


since it's a 2 x n matrix, we move down once and reach the bottom,
there is NO way to move down again 


答案是：不行，Robot 1 不能多次切換上下方向（如 →↓→↓）。
它在整趟路程中只能「往下轉折一次」，這也完全對應你給的第二個範例


e.g.

```
Row 0: → → → → ↓ 
Row 1:           → → →
```


->

(實際上在網格上視覺化就是：在某個欄位 $i$ 從第一列往下掉到第二列，然後一路向右)


->

bot 只能轉折一次


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


# V0-0-1
# IDEA: PREFIX + SUFFIX SUM + min-max greedy (gpt)
"""

Q:

1 why max() then min() ?

    -> KEY !!!

    ```
    robot_2_score = max(top_right, bottom_left)
    ```

    
    -> robot 2 wants higgest val

        -> given

            ```
            Top:     [ X X X | X X ]
                               ↑
                             Robot 1
            Bottom:  [ X X X | X X ]
            ```


        -> robot 2 can ONLY choose from

            ```
            A. 右邊 top
            B. 左邊 bottom
            ```


        -> so robot 2 will choose

            ```
            max(top_right, bottom_left)
            ```

        -> but, robot 1 knows above, so robot 1 will choose:

            ```
            min(robot_2_score)
            ```


        -> so overall, for this LC: (min-max greedy)


            ```
                Robot 1:
                    min(
                        Robot 2:
                            max(top-right, bottom-left)
                    )
            ```
        
"""
class Solution(object):
    def gridGame(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """

        n = len(grid[0])

        # Robot 1 initially has all top-row points
        top_sum = sum(grid[0])

        # Robot 1 wants to minimize Robot 2's maximum score
        answer = float('inf')

        bottom_left = 0

        for x in range(n):

            # Robot 1 takes grid[0][x]
            # So these top cells are no longer available to Robot 2
            top_sum -= grid[0][x]

            # Robot 2 can go:
            #
            # Option 1:
            # Stay on top row and collect everything to the right
            top_right = top_sum

            # Option 2:
            # Go down earlier and collect bottom row on the left
            bottom_left_score = bottom_left

            # Robot 2 chooses the better option
            robot_2_score = max(
                top_right,
                bottom_left_score
            )

            # Robot 1 chooses the column that minimizes
            # Robot 2's score
            answer = min(answer, robot_2_score)

            # Prepare bottom prefix for next iteration
            bottom_left += grid[1][x]

        return answer


# V0-0-X
# IDEA: MATRIX + prblem understading (gpt)
"""
NOTE !!!

    - the grid is ALWAYS `2 x n` size
        -> so the robot can ONLY `change direction` as once

    e.g.
        -> 

            Row 0: → → → → ↓ 
            Row 1:           → → →

"""
class Solution(object):
    def gridGame(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """

        n = len(grid[0])

        # Robot 1 initially has the sum of the top row
        top_sum = sum(grid[0])

        # Robot 2 will collect the maximum remaining points
        robot_2_max = float('inf')

        # Points already passed by Robot 1 on the top row
        top_left = 0

        # Try every possible column where Robot 1 goes down
        for x in range(n):
            # Remove current top cell because Robot 1 takes it
            top_sum -= grid[0][x]

            # Robot 2 can choose:
            # 1. remaining top row on the right
            robot_2_top = top_sum

            # 2. bottom row on the left
            robot_2_bottom = sum(grid[1][:x])

            # Robot 2 will choose the better of the two
            robot_2_score = max(robot_2_top, robot_2_bottom)

            # Robot 1 wants to minimize Robot 2's score
            robot_2_max = min(robot_2_max, robot_2_score)

            # Move to next column
            top_left += grid[0][x]

        return robot_2_max


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

        """
        NOTE !!!



        Robot 1, 2 不能多次切換上下方向（如 →↓→↓）。
        
            -> 它在整趟路程中只能「往下轉折一次」

        """
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


# V0-2
# IDEA: PREFIX SUM (gpt)
"""
CORE IDEA:

### 核心概念

Robot 1 的 path 在 2-row grid 中一定長這樣：

```text
→ → → → ↓
          ↓
          → → →
```

也就是只需要決定：

> **在哪一個 column 從 top row 往下走？**

假設 Robot 1 在 `x` 轉彎：

```text
Top:     [拿走] [拿走] [拿走] | [剩下給 Robot 2]
Bottom:  [剩下給 Robot 2]      | [拿走]
                            ↑
                         turn here
```

所以 Robot 2 只能從兩個區域拿：

```text
top_sum    = turn 後面的 top row
bottom_sum = turn 前面的 bottom row
```

Robot 2 當然會選比較大的：

```python
robot2 = max(top_sum, bottom_sum)
```

而 Robot 1 要讓 Robot 2 越少越好：

```python
res = min(res, robot2)
```

---

### Dry run

例如：

```text
grid =
[
 [2, 5, 4],
 [1, 5, 1]
]
```

一開始：

```text
top_sum = 2 + 5 + 4 = 11
bottom_sum = 0
```

#### x = 0

Robot 1 拿走 `2`：

```text
top_sum = 9
bottom_sum = 0
```

Robot 2：

```text
max(9, 0) = 9
```

所以：

```text
res = 9
```

然後：

```text
bottom_sum += 1
```

變成：

```text
bottom_sum = 1
```

---

#### x = 1

Robot 1 拿走 `5`：

```text
top_sum = 4
bottom_sum = 1
```

Robot 2：

```text
max(4, 1) = 4
```

所以：

```text
res = min(9, 4) = 4
```

---

#### x = 2

Robot 1 拿走 `4`：

```text
top_sum = 0
bottom_sum = 6
```

Robot 2：

```text
max(0, 6) = 6
```

所以：

```text
res = min(4, 6) = 4
```

答案：

```text
4
```

---

這題真正需要的是：

```text
假設 Robot 1 在每個可能的 column 下去
→ 計算 Robot 2 最多可以拿多少
→ 找 minimum
```

不需要真的修改 grid。

---

### Complexity

只需要掃一次 columns：

```text
Time:  O(N)
Space: O(1)
```

這也是這題最重要的面試解法。

### 面試記憶點

**LC 2017 = 2 rows + choose turning point**

看到：

```text
top
bottom
```

直接想：

```text
Robot 1 的 path
→ → → ↓ → → →
```

每個 column 都試一次：

```python
top_sum -= grid[0][x]

robot2 = max(
    top_sum,
    bottom_sum
)

res = min(res, robot2)

bottom_sum += grid[1][x]
```

一句話記：

> **Robot 1 選轉彎點；Robot 2 選上下較大的剩餘區域；所以是 `min(max(top, bottom))`。**



"""
class Solution(object):
    def gridGame(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or not grid[0]:
            return 0

        n = len(grid[0])

        # Robot 1 initially takes the entire top row
        top_sum = sum(grid[0])

        # Robot 1 will turn down at some column.
        # Before turning down, remove the current top cell.
        #
        # Robot 2 can then get:
        # 1. top row cells AFTER Robot 1 turns down
        # 2. bottom row cells BEFORE Robot 1 turns down
        bottom_sum = 0

        res = float("inf")

        for x in range(n):
            # Robot 1 takes grid[0][x]
            top_sum -= grid[0][x]

            # If Robot 1 turns down at x:
            #
            # Robot 2's best choice is either:
            # - remaining top row
            # - accumulated bottom row
            robot2 = max(top_sum, bottom_sum)

            # Robot 1 wants to minimize Robot 2's score
            res = min(res, robot2)

            # For the next turning point, bottom[x] becomes available
            bottom_sum += grid[1][x]

        return res


# V0-3
# IDEA: PREFIX SUM (gemini)
class Solution(object):

  def gridGame(self, grid):
    n = len(grid[0])

    # 計算第一列與第二列的總和（或透過前綴和維護）
    top_sum = sum(grid[0])
    bottom_sum = sum(grid[1])

    top_curr = 0  # 第一列累積和 (0 到 i)
    bottom_curr = 0  # 第二列累積和 (0 到 i-1)

    min_robot2_score = float("inf")

    for i in range(n):
      # 掉落點在 column i 時：
      # 第一列剩餘給 Robot 2 的分數在 i 右側 = 總和 - 前 i+1 個元素
      top_remaining = top_sum - top_curr - grid[0][i]

      # 第二列剩餘給 Robot 2 的分數在 i 左側 = i 以前累積的和
      bottom_remaining = bottom_curr

      # Robot 2 會採取最優策略（拿兩者中的較大值）
      robot2_score = max(top_remaining, bottom_remaining)

      # Robot 1 要使 Robot 2 的得分最小化
      min_robot2_score = min(min_robot2_score, robot2_score)

      # 更新累積和給下一輪迭代使用
      top_curr += grid[0][i]
      bottom_curr += grid[1][i]

    return min_robot2_score
