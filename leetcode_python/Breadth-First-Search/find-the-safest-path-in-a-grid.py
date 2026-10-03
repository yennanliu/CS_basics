"""

# https://leetcode.cn/problems/find-the-safest-path-in-a-grid/



2812. Find the Safest Path in a Grid
Medium

You are given a 0-indexed 2D matrix grid of size n x n, where (r, c) represents:

A cell containing a thief if grid[r][c] = 1
An empty cell if grid[r][c] = 0

You are initially positioned at cell (0, 0). In one move, you can move to any adjacent cell in the grid, including cells containing thieves.

The safeness factor of a path on the grid is defined as the minimum manhattan distance from any cell in the path to any thief in the grid.

Return the maximum safeness factor of all paths leading to cell (n - 1, n - 1).

An adjacent cell of cell (r, c), is one of the cells (r, c + 1), (r, c - 1), (r + 1, c) and (r - 1, c) if it exists.

The Manhattan distance between two cells (a, b) and (x, y) is equal to |a - x| + |b - y|, where |val| denotes the absolute value of val.


Example 1:

Input: grid = [[1,0,0],[0,0,0],[0,0,1]]
Output: 0
Explanation: All paths from (0, 0) to (n - 1, n - 1) go through the thieves in cells (0, 0) and (n - 1, n - 1).

Example 2:

Input: grid = [[0,0,1],[0,0,0],[0,0,0]]
Output: 2
Explanation: The path depicted in the picture above has a safeness factor of 2 since:
- The closest cell of the path to the thief at cell (0, 2) is cell (0, 0). The distance between them is | 0 - 0 | + | 0 - 2 | = 2.
It can be shown that there are no other paths with a higher safeness factor.

Example 3:

Input: grid = [[0,0,0,1],[0,0,0,0],[0,0,0,0],[1,0,0,0]]
Output: 2
Explanation: The path depicted in the picture above has a safeness factor of 2 since:
- The closest cell of the path to the thief at cell (0, 3) is cell (1, 2). The distance between them is | 0 - 1 | + | 3 - 2 | = 2.
- The closest cell of the path to the thief at cell (3, 0) is cell (3, 2). The distance between them is | 3 - 3 | + | 0 - 2 | = 2.
It can be shown that there are no other paths with a higher safeness factor.


Constraints:

1 <= grid.length == n <= 400
grid[i].length == n
grid[i][j] is either 0 or 1.
There is at least one thief in the grid.

"""


"""

2812. 找出最安全路径

給你一個下標從 0 開始、大小為 n x n 的二維矩陣 grid ，其中 (r, c) 表示：

如果 grid[r][c] = 1 ，則表示一個存在小偷的單元格
如果 grid[r][c] = 0 ，則表示一個空白儲存格
你最開始位於單元格 (0, 0) 。在一步移動中，你可以移動到矩陣中的任一相鄰單元格，包括存在小偷的單元格。

矩陣中路徑的 安全係數 定義為：從路徑中任一單元格到矩陣中任一小偷所在單元格的 最小 曼哈頓距離。

傳回所有通往儲存格 (n - 1, n - 1) 的路徑中的 最大安全係數 。

單元格 (r, c) 的某個 相鄰 單元格，是指在矩陣中存在的 (r, c + 1)、(r, c - 1)、(r + 1, c) 和 (r - 1, c) 之一。

兩個單元格 (a, b) 和 (x, y) 之間的 曼哈頓距離 等於 | a - x | + | b - y | ，其中 |val| 表示 val 的絕對值。

"""


# V0-1
# IDEA: MULTI SOURCE BFS + Dijkstra (gemini)
"""

1. CORE IDEA:


        ```
        ① Multi-Source BFS
           ↓
        算每個 cell 到最近 thief 的距離

        ② Binary Search
           ↓
        猜 safeness = K

        ③ BFS
           ↓
        只走 dist >= K 的 cell
           ↓
        能到終點嗎？
        ```



2.


    所以 LC 2812 本質上是一個很典型的：

    Maximum Bottleneck Path / Max-Min Path

    而你前面看到的：

    Multi-Source BFS + Binary Search + BFS

S
"""
from collections import deque
import heapq


class Solution(object):

  def maximumSafenessFactor(self, grid):
    """:type grid: List[List[int]] :rtype: int"""
    n = len(grid)

    # 邊界特判：如果起點或終點本身就是小偷，安全係數直接為 0
    if grid[0][0] == 1 or grid[n - 1][n - 1] == 1:
      return 0

    # ----------------------------------------------------
    # 步驟 1：多源 BFS 計算每個格子到最近小偷的曼哈頓距離
    # ----------------------------------------------------
    dist = [[-1] * n for _ in range(n)]
    q = deque()

    for r in range(n):
      for c in range(n):
        if grid[r][c] == 1:
          dist[r][c] = 0
          q.append((r, c))

    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

    while q:
      r, c = q.popleft()
      for dr, dc in directions:
        nr, nc = r + dr, c + dc
        if 0 <= nr < n and 0 <= nc < n and dist[nr][nc] == -1:
          dist[nr][nc] = dist[r][c] + 1
          q.append((nr, nc))

    # ----------------------------------------------------
    # 步驟 2：Dijkstra 結合 Max-Heap 尋找最高安全路徑
    # ----------------------------------------------------
    # Python 的 heapq 是 Min-Heap，所以我們把安全係數取負數來模擬 Max-Heap
    # 儲存格式：(-safeness, r, c)
    max_heap = [(-dist[0][0], 0, 0)]
    visited = [[False] * n for _ in range(n)]
    visited[0][0] = True

    while max_heap:
      safe, r, c = heapq.heappop(max_heap)
      safe = -safe  # 轉回正數

      # 如果成功抵達右下角，由於是 Max-Heap 優先擴展，此時的 safe 即為最優解
      if r == n - 1 and c == n - 1:
        return safe

      for dr, dc in directions:
        nr, nc = r + dr, c + dc
        if 0 <= nr < n and 0 <= nc < n and not visited[nr][nc]:
          visited[nr][nc] = True
          # 新路徑的瓶頸安全係數為：當前安全係數與下一個格子距離的最小值
          next_safe = min(safe, dist[nr][nc])
          heapq.heappush(max_heap, (-next_safe, nr, nc))

    return 0


# V0-2
# IDEA: MULTI SOURCE BFS + Dijkstra (GPT)
from collections import deque


class Solution(object):
    def maximumSafenessFactor(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """

        n = len(grid)

        # --------------------------------------------------
        # Step 1:
        # Multi-Source BFS
        #
        # dist[r][c] = distance from (r, c)
        #               to the nearest thief.
        # --------------------------------------------------

        dist = [[-1] * n for _ in range(n)]

        q = deque()

        # Put ALL thieves into the queue first.
        #
        # This is the key idea of Multi-Source BFS.
        for r in range(n):
            for c in range(n):
                if grid[r][c] == 1:
                    dist[r][c] = 0
                    q.append((r, c))

        directions = [
            (1, 0),
            (-1, 0),
            (0, 1),
            (0, -1)
        ]

        # BFS from all thieves simultaneously.
        while q:
            r, c = q.popleft()

            for dr, dc in directions:
                nr = r + dr
                nc = c + dc

                # Check boundary
                if nr < 0 or nr >= n or nc < 0 or nc >= n:
                    continue

                # Already visited
                if dist[nr][nc] != -1:
                    continue

                dist[nr][nc] = dist[r][c] + 1
                q.append((nr, nc))

        # --------------------------------------------------
        # Step 2:
        # Binary Search on the answer.
        #
        # We want the MAXIMUM safeness factor.
        #
        # low  = known possible value
        # high = possible upper bound
        # --------------------------------------------------

        low = 0
        high = n * 2

        answer = 0

        while low <= high:

            mid = (low + high) // 2

            # Check whether we can reach the destination
            # while every cell has safety >= mid.
            if self.can_reach(dist, mid):

                # mid is possible.
                answer = mid

                # Try to get an even safer path.
                low = mid + 1

            else:

                # mid is impossible.
                # Need a smaller safeness factor.
                high = mid - 1

        return answer

    def can_reach(self, dist, safeness):
        """
        Return True if there exists a path from
        (0, 0) to (n-1, n-1) where every cell has:

            dist[r][c] >= safeness
        """

        n = len(dist)

        # Start itself must satisfy the requirement.
        if dist[0][0] < safeness:
            return False

        # BFS for path existence.
        q = deque()
        q.append((0, 0))

        visited = [[False] * n for _ in range(n)]
        visited[0][0] = True

        directions = [
            (1, 0),
            (-1, 0),
            (0, 1),
            (0, -1)
        ]

        while q:
            r, c = q.popleft()

            # Reached destination.
            if r == n - 1 and c == n - 1:
                return True

            for dr, dc in directions:
                nr = r + dr
                nc = c + dc

                # Boundary check
                if nr < 0 or nr >= n or nc < 0 or nc >= n:
                    continue

                # Already visited
                if visited[nr][nc]:
                    continue

                # This cell is too close to a thief.
                if dist[nr][nc] < safeness:
                    continue

                visited[nr][nc] = True
                q.append((nr, nc))

        return False


# V0-3
# IDEA : MULTI-SOURCE BFS + MAX-MIN (WIDEST PATH) DIJKSTRA (CLAUDE)
#
#   Step 1 : multi-source BFS starting from EVERY thief at once gives
#            dist[r][c] = manhattan distance from (r, c) to the nearest thief.
#            (On a 4-neighbour grid with no obstacles, BFS layers == manhattan
#             distance, so no extra work is needed.)
#
#   Step 2 : we now want the path (0,0) -> (n-1,n-1) whose MINIMUM dist value
#            is as large as possible - the classic "widest path" / bottleneck
#            shortest path problem. Run a Dijkstra variant with a max-heap
#            where the cost of a path is min(cost so far, dist[next]) and we
#            always expand the currently best (largest) bottleneck.
#            The first time we pop the destination, its bottleneck is optimal.
#
#   NOTE : the endpoints count too - if (0,0) or (n-1,n-1) holds a thief its
#          dist is 0 and the answer is 0. Seeding the heap with dist[0][0]
#          handles that for free.
#
#   NOTE : `best` doubles as the visited marker; a cell is only pushed when we
#          strictly improve its bottleneck, so each cell is finalised once.
#
# time = O(n^2 * log n), space = O(n^2)
import heapq
from collections import deque


class Solution(object):
    def maximumSafenessFactor(self, grid):
        n = len(grid)
        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))

        # --- step 1 : multi-source BFS from all thieves ---
        dist = [[-1] * n for _ in range(n)]
        q = deque()
        for i in range(n):
            for j in range(n):
                if grid[i][j] == 1:
                    dist[i][j] = 0
                    q.append((i, j))
        while q:
            i, j = q.popleft()
            d = dist[i][j] + 1
            for di, dj in dirs:
                x, y = i + di, j + dj
                if 0 <= x < n and 0 <= y < n and dist[x][y] == -1:
                    dist[x][y] = d
                    q.append((x, y))

        # --- step 2 : bottleneck (max-min) Dijkstra with a max-heap ---
        best = [[-1] * n for _ in range(n)]
        best[0][0] = dist[0][0]
        heap = [(-dist[0][0], 0, 0)]
        while heap:
            negd, i, j = heapq.heappop(heap)
            d = -negd
            if d < best[i][j]:
                continue
            if i == n - 1 and j == n - 1:
                return d
            for di, dj in dirs:
                x, y = i + di, j + dj
                if 0 <= x < n and 0 <= y < n:
                    nd = d if d < dist[x][y] else dist[x][y]
                    if nd > best[x][y]:
                        best[x][y] = nd
                        heapq.heappush(heap, (-nd, x, y))
        return 0
