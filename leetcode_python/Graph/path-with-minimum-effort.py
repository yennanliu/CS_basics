"""

1631. Path With Minimum Effort
Medium

You are a hiker preparing for an upcoming hike. You are given heights, a 2D array of size rows x columns, where heights[row][col] represents the height of cell (row, col). You are situated in the top-left cell, (0, 0), and you hope to travel to the bottom-right cell, (rows-1, columns-1) (i.e., 0-indexed). You can move up, down, left, or right, and you wish to find a route that requires the minimum effort.

A route's effort is the maximum absolute difference in heights between two consecutive cells of the route.

Return the minimum effort required to travel from the top-left cell to the bottom-right cell.

 

Example 1:



Input: heights = [[1,2,2],[3,8,2],[5,3,5]]
Output: 2
Explanation: The route of [1,3,5,3,5] has a maximum absolute difference of 2 in consecutive cells.
This is better than the route of [1,2,2,2,5], where the maximum absolute difference is 3.
Example 2:



Input: heights = [[1,2,3],[3,8,4],[5,3,5]]
Output: 1
Explanation: The route of [1,2,3,4,5] has a maximum absolute difference of 1 in consecutive cells, which is better than route [1,3,5,3,5].
Example 3:


Input: heights = [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]
Output: 0
Explanation: This route does not require any effort.
 

Constraints:

rows == heights.length
columns == heights[i].length
1 <= rows, columns <= 100
1 <= heights[i][j] <= 106

"""

# V0
class Solution(object):
    def minimumEffortPath(self, heights):
        """
        :type heights: List[List[int]]
        :rtype: int
        """
        pass



# V0-0-1
# IDEA 1) Dijkstra's (gemini)
"""
NOTE !!!


    -> the `cost` in this LC is the `max height diff within neighbor cells`, but NOT accumulated cost

        -> e.g. 但這題的路徑成本不是累加高度，而是路徑上相鄰格子的「最大高度差」
"""
import heapq

class Solution(object):
    def minimumEffortPath(self, heights):
        """
        :type heights: List[List[int]]
        :rtype: int
        """
        if not heights or not heights[0]:
            return 0

        l = len(heights)
        w = len(heights[0])

        # Min Heap: [current_effort, x, y]
        pq = []
        heapq.heappush(pq, (0, 0, 0))

        # Four directions: right, left, down, up
        moves = [
            [0, 1],
            [0, -1],
            [1, 0],
            [-1, 0]
        ]

        # cost_matrix[y][x] = minimum known effort
        # required to reach cell (x, y).
        cost_matrix = [[float('inf')] * w for _ in range(l)]
        cost_matrix[0][0] = 0

        while pq:
            cost, x, y = heapq.heappop(pq)

            # Ignore outdated heap entries.
            if cost > cost_matrix[y][x]:
                continue

            # Reaching the destination with the smallest
            # effort means we have found the optimal answer.
            if x == w - 1 and y == l - 1:
                return cost

            for dx, dy in moves:
                _x = x + dx
                _y = y + dy

                # Check grid boundaries.
                if not (0 <= _x < w and 0 <= _y < l):
                    continue


                """
                NOTE !!! below
                """
                # Effort of this single move is the absolute
                # difference between the two cell heights.
                diff = abs(heights[_y][_x] - heights[y][x])

                """
                NOTE !!! below
                """
                # Path effort is the maximum single-step
                # difference encountered along the path.
                new_cost = max(cost, diff)

                if new_cost < cost_matrix[_y][_x]:
                    cost_matrix[_y][_x] = new_cost
                    heapq.heappush(pq, (new_cost, _x, _y))

        return 0



# V0-0-2
# IDEA 1) Dijkstra's (gemini)
import heapq


class Solution(object):

  def minimumEffortPath(self, heights):
    """:type heights: List[List[int]]

    :rtype: int
    """
    if not heights or not heights[0]:
      return 0

    l = len(heights)  # 總行數
    w = len(heights[0])  # 總列數

    # 優先佇列（Min-Heap），儲存元素格式：(effort, x, y)
    pq = [(0, 0, 0)]

    # 記錄從起點到每個座標所需的最小努力值，初始化為無限大
    dist = [[float("inf")] * w for _ in range(l)]
    dist[0][0] = 0

    # 四個移動方向：右、左、下、上
    moves = [[0, 1], [0, -1], [1, 0], [-1, 0]]

    while pq:
      effort, x, y = heapq.heappop(pq)

      # 如果已經到達右下角終點，直接回傳當前的最小努力值
      if x == w - 1 and y == l - 1:
        return effort

      # 如果當前記錄的努力值大於已知最佳解，跳過
      if effort > dist[y][x]:
        continue

      for m in moves:
        _x = x + m[0]
        _y = y + m[1]

        # 檢查是否在網格範圍內
        if 0 <= _x < w and 0 <= _y < l:
          # 計算到相鄰格子的努力值（路徑上的最大高度差）
          diff = abs(heights[_y][_x] - heights[y][x])
          new_effort = max(effort, diff)

          # 如果新努力值小於該點歷史紀錄，更新並推入 Heap
          if new_effort < dist[_y][_x]:
            dist[_y][_x] = new_effort
            heapq.heappush(pq, (new_effort, _x, _y))

    return 0


# V0-1
# IDEA 1) Dijkstra's (gemini)
import heapq

class Solution(object):
    def minimumEffortPath(self, heights):
        """
        :type heights: List[List[int]]
        :rtype: int
        """
        rows, cols = len(heights), len(heights[0])

        """

        NOTE !!!


        1. we need an `effore matrix` to record the min effort needed
           for reach each cell


        2. val init as float('-inf')

        """
        # effort_matrix[r][c] stores the minimum effort needed to reach cell (r, c)
        effort_matrix = [[float('inf')] * cols for _ in range(rows)]
        effort_matrix[0][0] = 0

        # Min-Heap stores tuples: (current_max_effort, row, col)
        min_heap = [(0, 0, 0)]

        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

        while min_heap:
            effort, r, c = heapq.heappop(min_heap)

            # Target reached: Dijkstra guarantees this is the minimum effort path
            if r == rows - 1 and c == cols - 1:
                return effort

            # Skip stale heap entries
            if effort > effort_matrix[r][c]:
                continue

            for dr, dc in directions:
                nr, nc = r + dr, c + dc

                if 0 <= nr < rows and 0 <= nc < cols:
                    
                    """
                    NOTE !!!

                    1. how we get `step effort` (abs diff between prev and cur cell)

                    2. how we get `next effort`


                    3. ONLY proceed if `new effort` < `cur effort`
                    """
                    # Effort to move to neighbor = max of path effort so far and edge step
                    step_effort = abs(heights[r][c] - heights[nr][nc])
                    next_effort = max(effort, step_effort)

                    # Relax edge if a less effort-intensive path is found
                    if next_effort < effort_matrix[nr][nc]:
                        effort_matrix[nr][nc] = next_effort
                        heapq.heappush(min_heap, (next_effort, nr, nc))

        return 0




# V0-2
# IDEA 1) Dijkstra's (gpt)
import heapq


class Solution(object):
    def minimumEffortPath(self, heights):
        """
        :type heights: List[List[int]]
        :rtype: int
        """
        l = len(heights)
        w = len(heights[0])

        # [effort, x, y]
        q = [(0, 0, 0)]

        # Minimum effort required to reach each cell
        dist = [[float("inf")] * w for _ in range(l)]
        dist[0][0] = 0

        moves = [
            (0, 1),
            (0, -1),
            (1, 0),
            (-1, 0)
        ]

        while q:
            effort, x, y = heapq.heappop(q)

            if x == w - 1 and y == l - 1:
                return effort

            # Skip stale heap entries
            if effort > dist[y][x]:
                continue

            for dx, dy in moves:
                x_ = x + dx
                y_ = y + dy

                if 0 <= x_ < w and 0 <= y_ < l:
                    edge_effort = abs(
                        heights[y_][x_] - heights[y][x]
                    )

                    # Path effort is the MAX edge difference
                    new_effort = max(effort, edge_effort)

                    if new_effort < dist[y_][x_]:
                        dist[y_][x_] = new_effort
                        heapq.heappush(
                            q,
                            (new_effort, x_, y_)
                        )

        return -1


# V1
