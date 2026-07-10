"""

542. 01 Matrix
Solved
Medium
Topics
premium lock icon
Companies
Given an m x n binary matrix mat, return the distance of the nearest 0 for each cell.

The distance between two cells sharing a common edge is 1.

 

Example 1:


Input: mat = [[0,0,0],[0,1,0],[0,0,0]]
Output: [[0,0,0],[0,1,0],[0,0,0]]
Example 2:


Input: mat = [[0,0,0],[0,1,0],[1,1,1]]
Output: [[0,0,0],[0,1,0],[1,2,1]]
 

Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 104
1 <= m * n <= 104
mat[i][j] is either 0 or 1.
There is at least one 0 in mat.
 

Note: This question is the same as 1765: https://leetcode.com/problems/map-of-highest-peak/


"""


"""

Q: Can we use `Dijkstra` for this LC ?


->

Yes, you **can** absolutely use Dijkstra's algorithm

to solve LC 542, but it is **suboptimal and considered overkill.**

Here is the breakdown of why it works, and why a 

standard BFS is preferred instead.

---

### 🟢 Why Dijkstra's Algorithm *Can* Be Used

Dijkstra's algorithm is designed to find the shortest path from a source node to other nodes in a graph, under one strict condition: **all edge weights must be non-negative ($\ge 0$).**

In this problem:

* The matrix cells are nodes.
* The steps (up, down, left, right) are edges.
* Every single step from one cell to an adjacent cell has a constant cost/weight of **`1`**.

Since `1` is non-negative, the mathematical constraints for Dijkstra are fully satisfied. You could initialize a priority queue (min-heap) with all the `0` cells at distance `0`, and then expand outward. It will yield the 100% correct answer.

---

### 🛑 Why You *Shouldn't* Use It (BFS vs. Dijkstra)

The deciding factor comes down to **uniform edge weights**.

* **Dijkstra uses a Min-Heap (Priority Queue):** It needs a heap to constantly sort and pick the absolute closest unvisited node. Pushing and popping from a min-heap costs **$\mathcal{O}(\log V)$** time.
* **BFS uses a standard FIFO Queue:** It processes elements strictly in the order they arrive. Pushing and popping from a deque costs **$\mathcal{O}(1)$** constant time.

> **The Golden Rule of Shortest Paths:** When all edges in a graph have the exact same weight (e.g., all are `1`), a standard FIFO queue naturally explores nodes in perfectly sorted, radiating layers of distance anyway ($0 \to 1 \to 2 \to 3 \dots$).

Because a regular queue naturally keeps the distances sorted for unweighted graphs, the sorting capability of Dijkstra's min-heap becomes entirely redundant.

---

### 📊 Complexity Comparison

If the matrix has $M$ rows and $N$ columns, let $V = M \cdot N$.

| Algorithm | Time Complexity | Why? |
| --- | --- | --- |
| **Multi-Source BFS** | **$\mathcal{O}(M \cdot N)$** | Every cell is pushed/popped from a FIFO queue exactly once at $\mathcal{O}(1)$ cost. |
| **Dijkstra's Algorithm** | **$\mathcal{O}((M \cdot N) \log (M \cdot N))$** | Every cell is pushed/popped from a Min-Heap, adding a heavy logarithmic overhead. |

**Summary:** You *can* use Dijkstra, but it adds unnecessary time complexity. Stick to Multi-Source BFS whenever edge weights are uniform!

"""







# V0
# IDEA: Multi-source BFS (GPT)
"""

NOTE !!!

 in this LC, we start from `0` cell (instead of `1` cell)
 e.g. the multi source BFS start from `0` cell

"""
# time = O(m * n)
# space = O(m * n)
from collections import deque

class Solution(object):
    def updateMatrix(self, mat):
        rows = len(mat)
        cols = len(mat[0])

        q = deque()

        # Distance matrix
        dist = [[-1] * cols for _ in range(rows)]

        # Put every 0 into the queue
        for r in range(rows):
            for c in range(cols):
                if mat[r][c] == 0:
                    q.append((r, c))
                    dist[r][c] = 0

        directions = [
            (1, 0),
            (-1, 0),
            (0, 1),
            (0, -1)
        ]

        # Multi-source BFS
        while q:
            r, c = q.popleft()

            for dr, dc in directions:
                nr = r + dr
                nc = c + dc

                if 0 <= nr < rows and 0 <= nc < cols and dist[nr][nc] == -1:
                    dist[nr][nc] = dist[r][c] + 1
                    q.append((nr, nc))

        return dist



# V0-1
# IDEA: Multi-source BFS (GEMINI)
# time = O(m * n)
# space = O(m * n)
from collections import deque

class Solution(object):
    def updateMatrix(self, mat):
        """
        :type mat: List[List[int]]
        :rtype: List[List[int]]
        """
        l = len(mat)
        w = len(mat[0])
        
        q = deque()
        visited = [[False] * w for _ in range(l)]
        
        # 1. Flip the perspective: Enqueue all '0' cells as our starting points
        for y in range(l):
            for x in range(w):
                if mat[y][x] == 0:
                    q.append([x, y, 0])  # [x, y, current_distance]
                    visited[y][x] = True
                    
        # Direction matrix for moving: Up, Down, Right, Left
        directions = [[0, 1], [0, -1], [1, 0], [-1, 0]]
        
        # 2. Radiate outwards layer-by-layer uniformly using BFS
        while q:
            # FIX: Use Python's native popleft() to pull elements from the queue
            x, y, dist = q.popleft()
            
            for d in directions:
                x_ = x + d[0]
                y_ = y + d[1]
                
                # Check matrix boundaries and verify if the neighbor hasn't been visited
                if 0 <= x_ < w and 0 <= y_ < l and not visited[y_][x_]:
                    visited[y_][x_] = True
                    # The distance to this cell is simply the previous step's distance + 1
                    mat[y_][x_] = dist + 1
                    q.append([x_, y_, dist + 1])
                    
        return mat

# V1 
# http://bookshadow.com/weblog/2017/03/19/leetcode-01-matrix/
# IDEA : QUEUE 
# time = O((m * n) ^ 2)  # multi-pass border peeling, worst case
# space = O(m * n)
class Solution(object):
    def updateMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        h, w = len(matrix), len(matrix[0])
        ans = [[0] * w for x in range(h)]
        queue = [(x, y) for x in range(h) for y in range(w) if matrix[x][y]]
        step = 0
        while queue:
            step += 1
            nqueue, mqueue = [], []
            for x, y in queue:
                zero = 0
                #for dx, dy in zip((1, 0, -1, 0), (0, 1, 0, -1)):
                for dx, dy in [[1,0], [0,1], [-1,0], [0,-1]]:
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < h and 0 <= ny < w and matrix[nx][ny] == 0:
                        zero += 1
                if zero:
                    ans[x][y] = step
                    mqueue.append((x, y))
                else: 
                    nqueue.append((x, y))
            for x, y in mqueue:
                matrix[x][y] = 0
            queue = nqueue
        return ans

# V1'
# https://www.jiuzhang.com/solution/01-matrix/#tag-highlight-lang-python
class Solution:
    """
    @param matrix: a 0-1 matrix
    @return: return a matrix
    """
    # time = O(m * n)
    # space = O(m * n)
    def updateMatrix(self, matrix):
        # write your code here
        n, m = len(matrix), len(matrix[0])
        dp = [[100000 for j in range(m)] for i in range(n)]
        for i in range(n):
            for j in range(m):
                if (matrix[i][j] == 0):
                    dp[i][j] = 0
                if (i > 0):
                    dp[i][j] = min(dp[i][j], dp[i - 1][j] + 1)
                if (j > 0):
                    dp[i][j] = min(dp[i][j], dp[i][j - 1] + 1)
        for i in range(n - 1, -1, -1):
            for j in range(m - 1, -1, -1):
                if (dp[i][j] > 0):
                    if (i < n - 1):
                        dp[i][j] = min(dp[i][j], dp[i + 1][j] + 1)
                    if (j < m - 1):
                        dp[i][j] = min(dp[i][j], dp[i][j + 1] + 1)
        return dp

# V2 
# time = O(m * n)
# space = O(m * n)
import collections
class Solution(object):
    def updateMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        queue = collections.deque()
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j] == 0:
                    queue.append((i, j))
                else:
                    matrix[i][j] = float("inf")

        dirs = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        while queue:
            cell = queue.popleft()
            for dir in dirs:
                i, j = cell[0]+dir[0], cell[1]+dir[1]
                if not (0 <= i < len(matrix)) or not (0 <= j < len(matrix[0])) or \
                   matrix[i][j] <= matrix[cell[0]][cell[1]]+1:
                        continue
                queue.append((i, j))
                matrix[i][j] = matrix[cell[0]][cell[1]]+1

        return matrix


# time = O(m * n)
# space = O(m * n)
# dp solution
class Solution2(object):
    def updateMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        dp = [[float("inf")]*len(matrix[0]) for _ in range(len(matrix))]
        for i in range(len(matrix)):
            for j in range(len(matrix[i])):
                if matrix[i][j] == 0:
                    dp[i][j] = 0
                else:
                    if i > 0:
                        dp[i][j] = min(dp[i][j], dp[i-1][j]+1)
                    if j > 0:
                        dp[i][j] = min(dp[i][j], dp[i][j-1]+1)
        for i in reversed(range(len(matrix))):
            for j in reversed(range(len(matrix[i]))):
                if matrix[i][j] == 0:
                    dp[i][j] = 0
                else:
                    if i < len(matrix)-1:
                        dp[i][j] = min(dp[i][j], dp[i+1][j]+1)
                    if j < len(matrix[i])-1:
                        dp[i][j] = min(dp[i][j], dp[i][j+1]+1)
        return dp
